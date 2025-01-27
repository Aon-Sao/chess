package chess;

public enum ChessDirection {

        LEFT      (new int[]{ 0, -1}),
        RIGHT     (new int[]{ 0,  1}),
        UP        (new int[]{ 1,  0}),
        DOWN      (new int[]{-1,  0}),
        UP_LEFT   (new int[]{ 1, -1}),
        DOWN_LEFT (new int[]{-1, -1}),
        UP_RIGHT  (new int[]{ 1,  1}),
        DOWN_RIGHT(new int[]{-1,  1});

        private final int[] vector;
        public int[] getVector() {
                return this.vector;
        }
        public ChessDirection getOppositeDirection() {
                ChessDirection opposite = LEFT;
                switch (this) {
                        case LEFT -> opposite = RIGHT;
                        case RIGHT -> opposite = LEFT;
                        case UP -> opposite = DOWN;
                        case DOWN -> opposite = UP;
                        case UP_LEFT -> opposite = DOWN_RIGHT;
                        case DOWN_LEFT -> opposite = UP_RIGHT;
                        case UP_RIGHT -> opposite = DOWN_LEFT;
                        case DOWN_RIGHT -> opposite = UP_LEFT;
                }
                return opposite;
        }
        ChessDirection(int[] vector) {
                this.vector = vector;
        }
}
