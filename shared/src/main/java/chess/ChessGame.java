package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private ChessGame.TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }
    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }
    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }
    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    private ChessPosition getKingPos(TeamColor teamColor) {
        return getBoard().findPiece(new ChessPiece(teamColor, ChessPiece.PieceType.KING));
    }
    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (getBoard().isEmptyPosition(startPosition)) {
            return null;
        } else {
            var originalBoard = getBoard();
            Collection<ChessMove> moves = new ArrayList<>();
            for (var move : getBoard().getPiece(startPosition).pieceMoves(getBoard(), startPosition)) {
                var futureBoard = new ChessBoard(getBoard());
                setBoard(futureBoard);
                if (!isInCheck(getTeamTurn())) {
                    moves.add(move);
                }
            }
            setBoard(originalBoard);
            return moves;
        }
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var board = getBoard();
        var startPosition = move.getStartPosition();
        var movingPiece = board.getPiece(startPosition);
        if (getTeamTurn() == movingPiece.getTeamColor()
                && validMoves(startPosition).contains(move)) {
            board.addPiece(startPosition, movingPiece);
            board.removePiece(startPosition);
        } else {
            throw new InvalidMoveException();
        }
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        var kingPos = getKingPos(teamColor);
        board = getBoard();
        for (var searchDirection : ChessDirection.values()) {
            var ray = new SearchRay(kingPos, searchDirection, 8, board);
            var threatPiece = ray.getThreat();
            if (threatPiece != null) {
                if (threatPiece.getMoveDirections().contains(searchDirection.getOppositeDirection())
                        && ((ray.getTiles().size()) <= threatPiece.getMaxMoveDistance())) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        var check = isInCheck(teamColor);
        var noMoves = noMovesLeft(teamColor);
        return check && noMoves;
//        return isInCheck(teamColor) && noMovesLeft(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return noMovesLeft(teamColor) && (!isInCheck(teamColor));
    }

    private boolean noMovesLeft(TeamColor teamColor) {
        var x = 1;
        var y = 1;
        var moves = new ArrayList<>();
        for (ChessPiece piece : (Iterable<ChessPiece>) getBoard()) {
            var pos = new ChessPosition(x, y);
            if ((!getBoard().isEmptyPosition(pos))
                    && piece.getTeamColor() == teamColor) {
                moves.addAll(validMoves(pos));
            }
            x++;
            if (x == 9) {
                x = 1;
                y++;
            }
        }
        return moves.isEmpty();
    }
}
