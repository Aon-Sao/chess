package chess;

class Direction {
    private final int rowOffset;
    private final int colOffset;

    public int getRow() {
        return rowOffset;
    }

    public int getCol() {
        return colOffset;
    }

    public Direction(int row, int col) {
        rowOffset = row;
        colOffset = col;
    }

    public Direction plus(Direction other) {
        return new Direction(rowOffset + other.rowOffset, colOffset + other.colOffset);
    }

    public Direction times(int scalar) {
        return new Direction(rowOffset * scalar, colOffset * scalar);
    }
}