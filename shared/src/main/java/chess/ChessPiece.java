package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.ChessDirection.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;
    private List<ChessDirection> moveDirections;
    private int maxMoveDistance;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
        determineMovePattern();
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

    private void determineMovePattern() {
        if (this.pieceType == PieceType.KING) {
            this.maxMoveDistance = 1;
            this.moveDirections = List.of(
                    LEFT,
                    RIGHT,
                    UP,
                    DOWN,
                    UP_LEFT,
                    DOWN_LEFT,
                    UP_RIGHT,
                    DOWN_RIGHT
            );
        } else if (this.pieceType == PieceType.QUEEN) {
            this.maxMoveDistance = 8;
            this.moveDirections = List.of(
                    LEFT,
                    RIGHT,
                    UP,
                    DOWN,
                    UP_LEFT,
                    DOWN_LEFT,
                    UP_RIGHT,
                    DOWN_RIGHT
            );
        } else if (this.pieceType == PieceType.BISHOP) {
            this.maxMoveDistance = 8;
            this.moveDirections = List.of(
                    UP_LEFT,
                    DOWN_LEFT,
                    UP_RIGHT,
                    DOWN_RIGHT
            );
        } else if (this.pieceType == PieceType.ROOK) {
            this.maxMoveDistance = 8;
            this.moveDirections = List.of(
                    LEFT,
                    RIGHT,
                    UP,
                    DOWN
            );
        }
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (ChessDirection dir : this.moveDirections) {
            SearchRay ray = new SearchRay(myPosition, dir, board);
            for (ChessPosition pos : ray.getTiles()) {
                moves.add(new ChessMove(myPosition, pos));
            }
        }
        return moves;
//        throw new RuntimeException("Not implemented");
    }
}
