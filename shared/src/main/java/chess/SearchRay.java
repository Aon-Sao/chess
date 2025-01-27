package chess;

import java.util.ArrayList;

public class SearchRay {
    private final int row;
    private final int column;
    private final ArrayList<ChessPosition> tiles = new ArrayList<>();
    private final ChessBoard board;
    private ChessPosition threat = null;
    public SearchRay(ChessPosition startPosition, ChessDirection direction, int maxLen, ChessBoard board) {
        int[] dirVec = direction.getVector();
        this.row = dirVec[0];
        this.column = dirVec[1];
        this.board = board;
        populateRay(startPosition, maxLen);
    }

    public ArrayList<ChessPosition> getTiles() {
        return this.tiles;
    }

    private ChessPosition advance(ChessPosition pos) {
        return new ChessPosition(pos.getRow() + this.row, pos.getColumn() + this.column);
    }

    public ChessPosition getThreatPos() {
        return this.threat;
    }

    private void populateRay(ChessPosition startPosition, int maxLen) {
        ChessPosition current = advance(startPosition);
        ChessPiece originPiece = board.getPiece(startPosition);
        int len = 1;
        while (len <= maxLen) {
            if (this.board.isInBoundsPosition(current)) {
                if (this.board.isEmptyPosition(current)) {
                    tiles.add(current);
                } else if (originPiece.isEnemyPiece(board.getPiece(current))) {
                    tiles.add(current);
                    this.threat = current;
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
            current = advance(current);
            len++;
        }
    }

}
