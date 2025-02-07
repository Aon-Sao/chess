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
    private final ArrayList<AllDirections> moveDirections = new ArrayList<>();
    private int maxMoveDistance;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        determineMoveDirections();
    }

    private void determineMoveDirections() {
        var orthogonals = List.of(AllDirections.UP, AllDirections.DOWN, AllDirections.LEFT, AllDirections.RIGHT);
        var diagonals = List.of(AllDirections.DOWN_LEFT, AllDirections.DOWN_RIGHT, AllDirections.UP_LEFT, AllDirections.UP_RIGHT);
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
        switch (type) {
            case BISHOP, ROOK, KING, QUEEN -> {
                for (var direction : moveDirections) {
                    var ray = new SearchRay(board, myPosition, direction, maxMoveDistance);
                    for (var pos : ray.getTiles()) {
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
                }
            }
            case KNIGHT -> {
                var knightOffsets = List.of(
                        new int[]{ 1,  2},
                        new int[]{ 1, -2},
                        new int[]{ 2,  1},
                        new int[]{ 2, -1},
                        new int[]{-1,  2},
                        new int[]{-1, -2},
                        new int[]{-2,  1},
                        new int[]{-2, -1});
                for (var offset : knightOffsets) {
                    var pos = new ChessPosition(myPosition.getRow() + offset[0], myPosition.getColumn() + offset[1]);
                    if (board.isInBoundsPos(pos) && (board.isEmptyPos(pos) || board.getPiece(pos).isEnemy(this))) {
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
                }
            }
            case PAWN -> {
                int row = myPosition.getRow();
                int col = myPosition.getColumn();
                boolean first_move = false;
                boolean promotion_imminent = false;
                if (getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    // UP is forward
                    moveDirections.add(AllDirections.UP);
                    first_move = row == 2;
                    promotion_imminent = row == 7;
                } else {
                    // DOWN is forward
                    moveDirections.add(AllDirections.DOWN);
                    first_move = row == 7;
                    promotion_imminent = row == 2;
                }
                int row_offset = moveDirections.getFirst().getRow();
                int col_offset = moveDirections.getFirst().getCol();
                var one_ahead = new ChessPosition(row + row_offset, col + col_offset);
                var two_ahead = new ChessPosition(row + (2 * row_offset), col + (2 * col_offset));
                var left_ahead = new ChessPosition(row + row_offset, col + col_offset - 1);
                var right_ahead = new ChessPosition(row + row_offset, col + col_offset + 1);

                if (board.isInBoundsPos(one_ahead) && board.isEmptyPos(one_ahead)) {
                    moves.add(new ChessMove(myPosition, one_ahead, null));
                    if (first_move && board.isEmptyPos(two_ahead)) {
                        moves.add(new ChessMove(myPosition, two_ahead, null));
                    }
                }

                for (var pos : List.of(left_ahead, right_ahead)) {
                    if (board.isInBoundsPos(pos) && board.notEmptyPos(pos) && board.getPiece(pos).isEnemy(this)) {
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
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
