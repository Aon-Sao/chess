package chess;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

public class SearchRay {
    private final ChessBoard board;
    private final ChessPosition startPosition;
    private final Direction direction;
    private final int maxLen;
    private final ArrayList<ChessPosition> tiles;
    private final Function<ChessPosition, Boolean> breakCon;
    private final Function<ChessPosition, Boolean> addCon;
    private ChessPosition finalPos;

    SearchRay(ChessBoard board, ChessPosition startPosition, Direction direction, int maxLen, String breakCondition, String addCondition) {
        this.board = board;
        this.startPosition = startPosition;
        this.direction = direction;
        this.maxLen = maxLen;

        Map<String, Function<ChessPosition, Boolean>> rayConds = Map.of(
                "empty", this.board::isEmptyPos,
                "notEmpty", this.board::notEmptyPos,
                "enemy", pos -> this.board.notEmptyPos(pos) && this.board.getPiece(pos).isEnemy(this.board.getPiece(this.startPosition)),
                "friend", pos -> this.board.notEmptyPos(pos) && this.board.getPiece(pos).isFriendly(this.board.getPiece(this.startPosition)),
                "enemyOrEmpty", pos -> this.board.isEmptyPos(pos)
                        || this.board.getPiece(pos).isEnemy(this.board.getPiece(this.startPosition))
        );

        this.breakCon = rayConds.get(breakCondition);
        this.addCon = rayConds.get(addCondition);
        tiles = new ArrayList<>();
        populateRay();
    }

    SearchRay(ChessBoard board, ChessPosition startPosition, Direction direction, int maxLen) {
        this(board, startPosition, direction, maxLen, "notEmpty", "enemyOrEmpty");
    }

    private void populateRay() {
        var currentPos = advance(startPosition);
        int len = 1;
        while (len <= maxLen && board.isInBoundsPos(currentPos)) {
            if (addCon.apply(currentPos)) {
                tiles.add(currentPos);
            }
            if (breakCon.apply(currentPos)) {
                finalPos = currentPos;
                break;
            }
            len++;
            currentPos = advance(currentPos);
        }
    }

    private ChessPosition advance(ChessPosition pos) {
        var rowOffset = direction.getRow();
        var colOffset = direction.getCol();
        return new ChessPosition(pos.getRow() + rowOffset, pos.getColumn() + colOffset);
    }

    public ChessPosition getFinalPos() {
        return finalPos;
    }

    public ArrayList<ChessPosition> getTiles() {
        return tiles;
    }
}
