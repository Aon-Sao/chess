package chess;

import java.util.ArrayList;

public class SearchRay {
    private final ChessBoard board;
    private final ChessPosition startPosition;
    private final Directions direction;
    private final int maxLen;
    private final ArrayList<ChessPosition> tiles;
    private ChessPosition threatPos;

    SearchRay(ChessBoard board, ChessPosition startPosition, Directions direction, int maxLen) {
        this.board = board;
        this.startPosition = startPosition;
        this.direction = direction;
        this.maxLen = maxLen;
        tiles = new ArrayList<>();
        populateRay();
    }

    private void populateRay() {
        var currentPos = advance(startPosition);
        int len = 1;
        while (len <= maxLen) {
            if (!board.isInBoundsPos(currentPos)) {
                break;
            }
            if (board.isEmptyPos(currentPos)) {
                tiles.add(currentPos);
            } else if (board.notEmptyPos(currentPos) && board.getPiece(currentPos).isFriendly(board.getPiece(startPosition))) {
                break;
            } else if (board.notEmptyPos(currentPos) && board.getPiece(currentPos).isEnemy(board.getPiece(startPosition))) {
                tiles.add(currentPos);
                threatPos = currentPos;
                break;
            }
            len++;
            currentPos = advance(currentPos);
        }
    }

    private ChessPosition advance(ChessPosition pos) {
        var row_offset = direction.getVector()[0];
        var col_offset = direction.getVector()[1];
        return new ChessPosition(pos.getRow() + row_offset, pos.getColumn() + col_offset);
    }

    public ChessPosition getThreatPos() {
        return threatPos;
    }

    public ArrayList<ChessPosition> getTiles() {
        return tiles;
    }
}
