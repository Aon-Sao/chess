package chess;

import java.util.ArrayList;

public class SearchRay {
    private int x;
    private int y;
    private ArrayList<ChessPosition> tiles = new ArrayList<>();
    private ChessBoard board;
    public SearchRay(ChessPosition startPosition, ChessDirection direction, ChessBoard board) {
        int[] dir_vec = direction.getVector();
        this.x = dir_vec[0];
        this.y = dir_vec[1];
        this.board = board;
        populate_ray(startPosition);
    }

    public ArrayList<ChessPosition> getTiles() {
        return this.tiles;
    }

    private ChessPosition advance(ChessPosition pos) {
        return new ChessPosition(pos.getRow() + this.x, pos.getColumn() + this.y);
    }

    private void populate_ray(ChessPosition startPosition) {
        ChessPosition current = advance(startPosition);
        boolean in_bounds = this.board.isInBoundsPosition(current);
        boolean unimpeded = this.board.isEmptyPosition(current);

        while (in_bounds && unimpeded) {
            tiles.add(current);
            current = advance(current);
            in_bounds = this.board.isInBoundsPosition(current);
            unimpeded = this.board.isEmptyPosition(current);
        }
    }

}
