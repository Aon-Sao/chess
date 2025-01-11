package chess;

import java.util.ArrayList;

public class SearchRay {
    private int x;
    private int y;
    private ArrayList<ChessPosition> tiles = new ArrayList<>();
    private ChessBoard board;
    public SearchRay(ChessPosition startPosition, ChessDirection direction, int maxLen, ChessBoard board) {
        int[] dir_vec = direction.getVector();
        this.x = dir_vec[0];
        this.y = dir_vec[1];
        this.board = board;
        populate_ray(startPosition, maxLen);
    }

    public ArrayList<ChessPosition> getTiles() {
        return this.tiles;
    }

    private ChessPosition advance(ChessPosition pos) {
        return new ChessPosition(pos.getRow() + this.x, pos.getColumn() + this.y);
    }

    private void populate_ray(ChessPosition startPosition, int maxLen) {
        ChessPosition current = advance(startPosition);
        int len = 1;
        while (len <= maxLen) {
            if (this.board.isInBoundsPosition(current)) {
                if (this.board.isEmptyPosition(current)) {
                    tiles.add(current);
                }
                else if (board.getPiece(current).getTeamColor()
                        != board.getPiece(startPosition).getTeamColor()) {
                    tiles.add(current);
                    break;
                }
                else {
                    break;
                }
            }
            current = advance(current);
            len++;
        }
    }

}
