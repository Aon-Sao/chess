package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    private final ArrayList<Direction> moveDirections = new ArrayList<>();
    private int maxMoveDistance;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        determineMoveDirections();
    }

    public ChessPiece(ChessPiece copy) {
        this(copy.getTeamColor(), copy.getPieceType());
    }

    private void determineMoveDirections() {
        var orthogonals = AllDirections.getOrthogonals();
        var diagonals = AllDirections.getDiagonals();
        switch (type) {
            case BISHOP -> {
                maxMoveDistance = 8;
                moveDirections.addAll(diagonals);
            }
            case ROOK -> {
                maxMoveDistance = 8;
                moveDirections.addAll(orthogonals);
            }
            case KING -> {
                maxMoveDistance = 1;
                moveDirections.addAll(diagonals);
                moveDirections.addAll(orthogonals);
            }
            case QUEEN -> {
                maxMoveDistance = 8;
                moveDirections.addAll(diagonals);
                moveDirections.addAll(orthogonals);
            }
            case KNIGHT -> {
                maxMoveDistance = 1;
                moveDirections.addAll(AllDirections.getKnights());
            }
            case PAWN -> {
                AllDirections forward;
                if (getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    forward = AllDirections.UP;
                } else {
                    forward = AllDirections.DOWN;
                }
                moveDirections.add(forward.getDirection());
//                var forwardLeft = forward.getDirection().plus(AllDirections.LEFT.getDirection());
//                var forwardRight = forward.getDirection().plus(AllDirections.RIGHT.getDirection());
//                moveDirections.add(forwardLeft);
//                moveDirections.add(forwardRight);
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type, moveDirections, maxMoveDistance);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return maxMoveDistance == that.maxMoveDistance
                && color == that.color
                && type == that.type
                && Objects.equals(moveDirections, that.moveDirections);
    }

    @Override
    public String toString() {
        String s = "?";
        for (var pair : ChessBoard.CHAR_TO_TYPE_MAP.entrySet()) {
            if (pair.getValue().equals(type)) {
                s = pair.getKey().toString();
                if (color == ChessGame.TeamColor.WHITE) {
                    s = s.toUpperCase();
                } else {
                    s = s.toLowerCase();
                }
            }
        }
        return s;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moves = new ArrayList<ChessMove>();
        if (type.equals(PieceType.PAWN)) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            boolean promotion_imminent;

            if (getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                // UP is forward
                promotion_imminent = row == 7;
            } else {
                // DOWN is forward
                promotion_imminent = row == 2;
            }

            if ((row == 2) || (row == 7)) {
                maxMoveDistance = 2;
            } else {
                maxMoveDistance = 1;
            }

            var forward = moveDirections.getFirst();
            var forwardLeft = forward.plus(AllDirections.LEFT.getDirection());
            var forwardRight = forward.plus(AllDirections.RIGHT.getDirection());

            var forwardRay = new SearchRay(board, myPosition, forward, maxMoveDistance, "notEmpty", "empty");
            var forwardLeftRay = new SearchRay(board, myPosition, forwardLeft, 1, "enemyOrEmpty", "enemy");
            var forwardRightRay = new SearchRay(board, myPosition, forwardRight, 1, "enemyOrEmpty", "enemy");

            var tiles = forwardRay.getTiles();
            tiles.addAll(forwardLeftRay.getTiles());
            tiles.addAll(forwardRightRay.getTiles());

            for (var tile : tiles) {
                moves.add(new ChessMove(myPosition, tile, null));
            }

            if (promotion_imminent) {
                var tmp = new ArrayList<ChessMove>();
                for (var move : moves) {
                    for (var type : PieceType.values()) {
                        if (!(type.equals(PieceType.PAWN) || type.equals(PieceType.KING))) {
                            tmp.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), type));
                        }
                    }
                }
                moves = tmp;
            }


        } else {
            for (var direction : moveDirections) {
                var ray = new SearchRay(board, myPosition, direction, maxMoveDistance);
                for (var pos : ray.getTiles()) {
                    moves.add(new ChessMove(myPosition, pos, null));
                }
            }
        }
        return moves;
    }

    public boolean isEnemy(ChessPiece other) {
        return !isFriendly(other);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    public boolean isFriendly(ChessPiece other) {
        return getTeamColor() == other.getTeamColor();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }
}
