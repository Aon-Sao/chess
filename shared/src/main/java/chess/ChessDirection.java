package chess;

public enum ChessDirection {

        LEFT      (new int[]{-1,  0}),
        RIGHT     (new int[]{ 1,  0}),
        UP        (new int[]{ 0,  1}),
        DOWN      (new int[]{ 0, -1}),
        UP_LEFT   (new int[]{-1,  1}),
        DOWN_LEFT (new int[]{-1, -1}),
        UP_RIGHT  (new int[]{ 1,  1}),
        DOWN_RIGHT(new int[]{ 1, -1});

        private int[] vector;
        public int[] getVector() {
                return this.vector;
        }
        ChessDirection(int[] vector) {
                this.vector = vector;
        }
}
