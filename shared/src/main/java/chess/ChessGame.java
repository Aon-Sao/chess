package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame)) {
            return false;
        }
        ChessGame game = (ChessGame) o;
        return Objects.equals(board, game.board) && teamTurn == game.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
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
        if (board.isEmptyPos(startPosition)) {
            return null;
        } else {
            TeamColor movingPieceColor = board.getPiece(startPosition).getTeamColor();
            ArrayList<ChessMove> moves = new ArrayList<>();
            for (var move : board.getPiece(startPosition).pieceMoves(board, startPosition)) {
                var originalBoard = board;
                board = new ChessBoard(board);
                board.movePiece(move.getStartPosition(), move.getEndPosition());
                if (!isInCheck(movingPieceColor)) {
                    moves.add(move);
                }
                board = originalBoard;
            }
            return moves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var startPos = move.getStartPosition();
        var endPos = move.getEndPosition();
        if (board.isEmptyPos(startPos)) {
            throw new InvalidMoveException();
        }
        boolean moveIsValid = validMoves(startPos).contains(move);
        TeamColor pieceColor = board.getPiece(startPos).getTeamColor();
        boolean movingPieceHasTurn = pieceColor.equals(getTeamTurn());
        if (moveIsValid && movingPieceHasTurn) {
            board.movePiece(startPos, endPos);
            var promotionType = move.getPromotionPiece();
            if (promotionType != null) {
                var replacementPiece = new ChessPiece(pieceColor, promotionType);
                board.addPiece(endPos, replacementPiece);
            }
        } else {
            throw new InvalidMoveException();
        }
        advanceTurn();
    }

    private void advanceTurn() {
        teamTurn = oppositeTeam(getTeamTurn());
    }

    private TeamColor oppositeTeam(TeamColor teamColor) {
        if (teamColor.equals(TeamColor.WHITE)) {
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = getKingPos(teamColor);
        for (var direction : AllDirections.values()) {
            var ray = new SearchRay(board, kingPos, direction.getDirection(), 8);
            var finalPos = ray.getFinalPos();
            if (finalPos != null) {
                var threatPiece = board.getPiece(finalPos);
                if (threatPiece.getTeamColor() != teamColor) {
                    for (var move : threatPiece.pieceMoves(board, finalPos)) {
                        if (move.getEndPosition().equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition getKingPos(TeamColor teamColor) {
        for (Iterator<ChessPosition> it = board.positionIterator(); it.hasNext(); ) {
            var pos = it.next();
            if (board.notEmptyPos(pos)) {
                var piece = board.getPiece(pos);
                if (piece.getPieceType().equals(ChessPiece.PieceType.KING) && piece.getTeamColor().equals(teamColor)) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noMovesLeft(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return (!isInCheck(teamColor)) && noMovesLeft(teamColor);
    }

    private boolean noMovesLeft(TeamColor teamColor) {
        for (Iterator<ChessPosition> it = board.positionIterator(); it.hasNext(); ) {
            var pos = it.next();
            var piece = board.getPiece(pos);
            if (piece != null && piece.getTeamColor().equals(teamColor)
                    && (!(validMoves(pos).isEmpty()))) {
                return false;
            }
        }
        return true;
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
        return board;
    }
}
