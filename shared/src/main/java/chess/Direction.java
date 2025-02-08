package chess;

class Direction {
    private final int row_offset;
    private final int col_offset;

    public int getRow() {
        return row_offset;
    }

    public int getCol() {
        return col_offset;
    }

    public Direction(int row, int col) {
        row_offset = row;
        col_offset = col;
    }

    public Direction plus(Direction other) {
        return new Direction(row_offset + other.row_offset, col_offset + other.col_offset);
    }

    public Direction times(int scalar) {
        return new Direction(row_offset * scalar, col_offset * scalar);
    }
}