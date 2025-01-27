package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static chess.ChessDirection.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

    public ArrayList<ChessDirection> getMoveDirections() {
        return moveDirections;
    }

    public int getMaxMoveDistance() {
        return maxMoveDistance;
    }
    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }
    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    public boolean isEnemyPiece(ChessPiece other) {
        return other.getTeamColor() != this.getTeamColor();
    }

    private ArrayList<ChessDirection> moveDirections = new ArrayList<>();
    private int maxMoveDistance = 0;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
        determineMovePattern();
    }

    public ChessPiece(ChessPiece copy) {
        this.pieceColor = copy.pieceColor;
        this.pieceType = copy.pieceType;
        this.maxMoveDistance = copy.maxMoveDistance;
        // directions may be copied shallowly because they're enums,
        // which follow the singleton pattern
        this.moveDirections.addAll(copy.moveDirections);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return maxMoveDistance == that.maxMoveDistance
                && pieceColor == that.pieceColor
                && pieceType == that.pieceType
                && Objects.equals(moveDirections, that.moveDirections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType, moveDirections, maxMoveDistance);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public String toString() {
        for (var entry : ChessBoard.CHARACTER_PIECE_TYPE_MAP.entrySet()) {
            if (this.pieceType == entry.getValue()) {
                String s = entry.getKey().toString();
                if (this.pieceColor == ChessGame.TeamColor.WHITE) {
                    return s.toUpperCase();
                } else {
                    return s.toLowerCase();
                }
            }
        }
        return "?";
    }

    private void determineMovePattern() {
        List<ChessDirection> diagonal = List.of(
                UP_LEFT,
                DOWN_LEFT,
                UP_RIGHT,
                DOWN_RIGHT
        );
        List<ChessDirection> orthogonal = List.of(
                LEFT,
                RIGHT,
                UP,
                DOWN
        );
        if (this.pieceType == PieceType.KING) {
            this.maxMoveDistance = 1;
            this.moveDirections.addAll(diagonal);
            this.moveDirections.addAll(orthogonal);
        } else if (this.pieceType == PieceType.QUEEN) {
            this.maxMoveDistance = 8;
            this.moveDirections.addAll(diagonal);
            this.moveDirections.addAll(orthogonal);
        } else if (this.pieceType == PieceType.BISHOP) {
            this.maxMoveDistance = 8;
            this.moveDirections.addAll(diagonal);
        } else if (this.pieceType == PieceType.ROOK) {
            this.maxMoveDistance = 8;
            this.moveDirections.addAll(orthogonal);
        } else if (this.pieceType == PieceType.PAWN) {
            this.maxMoveDistance = 1;
            if (this.pieceColor == ChessGame.TeamColor.WHITE) {
                this.moveDirections.add(UP_LEFT);
                this.moveDirections.add(UP_RIGHT);
            } else {
                this.moveDirections.add(DOWN_LEFT);
                this.moveDirections.add(DOWN_RIGHT);
            }
        }
    }

    private Collection<ChessPosition> knightPattern(ChessPosition startPosition) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        ArrayList<ChessPosition> positions = new ArrayList<>();
        positions.add(new ChessPosition(row + 1, col + 2));
        positions.add(new ChessPosition(row + 1, col - 2));
        positions.add(new ChessPosition(row - 1, col + 2));
        positions.add(new ChessPosition(row - 1, col - 2));
        positions.add(new ChessPosition(row + 2, col + 1));
        positions.add(new ChessPosition(row - 2, col + 1));
        positions.add(new ChessPosition(row + 2, col - 1));
        positions.add(new ChessPosition(row - 2, col - 1));
        return positions;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (pieceType == PieceType.PAWN) {
            int startRow;
            int promotionRow;
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            int offset;
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                // Forward is UP (+)
                offset = UP.getVector()[1];
                startRow = 2;
                promotionRow = 8;
            } else {
                // Forward is DOWN (-)
                offset = DOWN.getVector()[1];
                startRow = 7;
                promotionRow = 1;
            }
            ChessPosition oneAhead = new ChessPosition(row + offset, col);
            ChessPosition leftAhead = new ChessPosition(row + offset, col - 1);
            ChessPosition rightAhead = new ChessPosition(row + offset, col + 1);
            ChessPosition twoAhead = new ChessPosition(row + (2 * offset), col);
            if (board.isEmptyPosition(oneAhead)) {
                moves.add(new ChessMove(myPosition, oneAhead));
                if ((row == startRow) && board.isEmptyPosition(twoAhead)) {
                    moves.add(new ChessMove(myPosition, twoAhead));
                }
            }
            for (ChessPosition pos : List.of(leftAhead, rightAhead)) {
                if (board.isInBoundsPosition(pos) && (!board.isEmptyPosition(pos)) && (this.isEnemyPiece(board.getPiece(pos)))) {
                    moves.add(new ChessMove(myPosition, pos));
                }
            }
            if (row + offset == promotionRow) {
                ArrayList<ChessMove> tmp = new ArrayList<>();
                for (ChessMove move : moves) {
                    for (PieceType promotionPiece : List.of(PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK)) {
                        tmp.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), promotionPiece));
                    }
                }
                moves = tmp;
            }
        } else if ((!moveDirections.isEmpty()) && (maxMoveDistance > 0)) {
            for (ChessDirection dir : moveDirections) {
                SearchRay ray = new SearchRay(myPosition, dir, maxMoveDistance, board);
                for (ChessPosition pos : ray.getTiles()) {
                    moves.add(new ChessMove(myPosition, pos));
                }
            }
        } else if (pieceType == PieceType.KNIGHT) {
            for (ChessPosition pos : knightPattern(myPosition)) {
                if (board.isInBoundsPosition(pos)) {
                    if ((board.isEmptyPosition(pos)) ||
                            (this.isEnemyPiece(board.getPiece(pos)))) {
                        moves.add(new ChessMove(myPosition, pos));
                    }
                }
            }
        }
        return moves;
    }
}
