package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Iterable {

    private ChessPiece[][] grid = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    public ChessBoard(ChessBoard copy) {
        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                var pos = new ChessPosition(row, col);
                if (!copy.isEmptyPosition(pos)) {
                    this.addPiece(pos, copy.getPiece(pos));
                }
            }
        }
    }

    public Iterator<ChessPiece> iterator() {
        var flatList = new ArrayList<>(Arrays.stream(this.grid).flatMap(Arrays::stream).toList());
        return Collections.unmodifiableList(flatList).iterator();
    }

    public ChessPosition findPiece(ChessPiece piece) {
        var col = 1;
        var row = 1;
        for (var p : this) {
            if (piece.equals(p)) {
                return new ChessPosition(row, col);
            }
            row++;
            if (row == 9) {
                row = 1;
                col++;
            }
        }
        return null;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.grid[position.getColumn() - 1][position.getRow() - 1] = piece;
    }

    public void removePiece(ChessPosition position) {
        addPiece(position, null);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.grid[position.getColumn() - 1][position.getRow() - 1];
    }

    public boolean isEmptyPosition(ChessPosition position) {
        return (isInBoundsPosition(position)) && (getPiece(position) == null);
    }

    public boolean isInBoundsPosition(ChessPosition position) {
        int x = position.getColumn();
        int y = position.getRow();
        return ((x >= 1) && (x <= 8)) && ((y >= 1) && (y <= 8));
    }

    public static final Map<Character, ChessPiece.PieceType> CHARACTER_PIECE_TYPE_MAP = Map.of(
            'p', ChessPiece.PieceType.PAWN,
            'n', ChessPiece.PieceType.KNIGHT,
            'r', ChessPiece.PieceType.ROOK,
            'q', ChessPiece.PieceType.QUEEN,
            'k', ChessPiece.PieceType.KING,
            'b', ChessPiece.PieceType.BISHOP);

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public static ChessBoard fromString(String boardString) {
        var board = new ChessBoard();
        int row = 8;
        int col = 1;
        for (char c : boardString.toCharArray()) {
            if (c == '\n') {
                col = 1;
                row -= 1;
            } else if (c == ' ') {
                col += 1;
            } else if (c != '|'){
                ChessGame.TeamColor team = Character.isUpperCase(c) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                ChessPiece.PieceType pieceType = CHARACTER_PIECE_TYPE_MAP.get(Character.toLowerCase(c));
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = new ChessPiece(team, pieceType);
                board.addPiece(pos, piece);
                col += 1;
            }
        }
        return board;
    }

    public void resetBoard() {
        String defaultBoard = """
                |r|n|b|q|k|b|n|r|
                |p|p|p|p|p|p|p|p|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                |P|P|P|P|P|P|P|P|
                |R|N|B|Q|K|B|N|R|
                """;
        var b = ChessBoard.fromString(defaultBoard);
        var tmp = b.grid;
        b.grid = null;
        this.grid = tmp;
    }

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

    @Override
    public String toString() {
        var emptyBoard = """
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                """.toCharArray();
        int row = 8;
        int col = 1;
        for (int i = 0; i < emptyBoard.length; i++) {
            var c = emptyBoard[i];
            if (c == '\n') {
                col = 1;
                row --;
            }
            else if (c == ' ') {
                var pos = new ChessPosition(row, col);
                if (!isEmptyPosition(pos)) {
                    emptyBoard[i] = this.getPiece(pos).toString().toCharArray()[0];
                }
                col++;
            }
        }
        return new String(emptyBoard);
    }
}
