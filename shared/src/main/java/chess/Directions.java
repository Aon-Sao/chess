package chess;

public enum Directions {
    UP        (new int[]{ 1,  0}),
    DOWN      (new int[]{-1,  0}),
    LEFT      (new int[]{ 0, -1}),
    RIGHT     (new int[]{ 0,  1}),
    UP_LEFT   (new int[]{ 1, -1}),
    UP_RIGHT  (new int[]{ 1,  1}),
    DOWN_LEFT (new int[]{-1, -1}),
    DOWN_RIGHT(new int[]{-1,  1});

    Directions(int[] vector) {
        this.vector = vector;
    }
    private final int[] vector;
    public int[] getVector() {
        return this.vector;
    }
}
