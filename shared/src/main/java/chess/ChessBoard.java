package chess;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] grid = new ChessPiece[8][8];

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Objects.deepEquals(grid, that.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        grid[position.getColumn() - 1][position.getRow() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return grid[position.getColumn() - 1][position.getRow() - 1];
    }

    public boolean isEmptyPos(ChessPosition position) {
        return getPiece(position) == null;
    }

    public boolean notEmptyPos(ChessPosition position) {
        return !isEmptyPos(position);
    }

    public boolean isInBoundsPos(ChessPosition position) {
        var row = position.getRow();
        var col = position.getColumn();
        return (row <= 8) && (row >= 1) && (col <= 8) && (col >= 1);
    }

    public static final Map<Character, ChessPiece.PieceType> CHAR_TO_TYPE_MAP = Map.of(
            'p', ChessPiece.PieceType.PAWN,
            'n', ChessPiece.PieceType.KNIGHT,
            'r', ChessPiece.PieceType.ROOK,
            'q', ChessPiece.PieceType.QUEEN,
            'k', ChessPiece.PieceType.KING,
            'b', ChessPiece.PieceType.BISHOP);

    public static ChessBoard fromString(String boardText) {
        var board = new ChessBoard();
        int row = 8;
        int column = 1;
        for (var c : boardText.toCharArray()) {
            switch (c) {
                case '\n' -> {
                    column = 1;
                    row--;
                }
                case ' ' -> column++;
                case '|' -> {
                }
                default -> {
                    ChessGame.TeamColor color = Character.isLowerCase(c) ? ChessGame.TeamColor.BLACK
                            : ChessGame.TeamColor.WHITE;
                    var type = CHAR_TO_TYPE_MAP.get(Character.toLowerCase(c));
                    var position = new ChessPosition(row, column);
                    var piece = new ChessPiece(color, type);
                    board.addPiece(position, piece);
                    column++;
                }
            }
        }
        return board;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        var board = fromString("""
                |r|n|b|q|k|b|n|r|
                |p|p|p|p|p|p|p|p|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                |P|P|P|P|P|P|P|P|
                |R|N|B|Q|K|B|N|R|
                """);
        var tmp = board.grid;
        board.grid = null;
        this.grid = tmp;
    }

    public String toString() {
        var boardText = """
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                """;
        int row = 8;
        int column = 1;
        var boardArr = boardText.toCharArray();
        int i = 0;
        for (var c : boardArr) {
            switch (c) {
                case '\n' -> {
                    column = 1;
                    row--;
                }
                case ' ' -> {
                    boardArr[i] = getPiece(new ChessPosition(row, column)).toString().toCharArray()[0];
                    column++;
                }
                case '|' -> {
                }
                default -> {
                    column++;
                }
            }
            i++;
        }
        return boardArr.toString();
    }
}
