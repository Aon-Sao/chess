package chess;

import java.util.Collection;
import java.util.List;

public enum AllDirections {
    UP        (new Direction(1, 0)),
    DOWN      (UP.direction.times(-1)),
    LEFT      (new Direction(0, -1)),
    RIGHT     (LEFT.direction.times(-1)),
    UP_LEFT   (UP.direction.plus(LEFT.direction)),
    UP_RIGHT  (UP.direction.plus(RIGHT.direction)),
    DOWN_LEFT (DOWN.direction.plus(LEFT.direction)),
    DOWN_RIGHT(DOWN.direction.plus(RIGHT.direction)),
    UUL       (UP.direction.plus(UP_LEFT.direction)),
    UUR       (UP.direction.plus(UP_RIGHT.direction)),
    ULL       (UP_LEFT.direction.plus(LEFT.direction)),
    URR       (UP_RIGHT.direction.plus(RIGHT.direction)),
    DDL       (UUR.direction.times(-1)),
    DDR       (UUL.direction.times(-1)),
    DLL       (URR.direction.times(-1)),
    DRR       (ULL.direction.times(-1));

    private final Direction direction;

    AllDirections(Direction direction) {
        this.direction = direction;
    }

    public int getRow() {
        return direction.getRow();
    }

    public int getCol() {
        return direction.getCol();
    }

    public static Collection<AllDirections> getOrthogonals() {
        return List.of(
                LEFT,
                RIGHT,
                UP,
                DOWN
        );
    }

    public static Collection<AllDirections> getDiagonals() {
        return List.of(
                UP_LEFT,
                UP_RIGHT,
                DOWN_LEFT,
                DOWN_RIGHT
        );
    }

    public static Collection<AllDirections> getCardinals() {
        var ortho = new java.util.ArrayList<>(getOrthogonals());
        ortho.addAll(getDiagonals());
        return ortho;
    }

    public static Collection<AllDirections> getKnights() {
        return List.of(
                UUL,
                UUR,
                ULL,
                URR,
                DDL,
                DDR,
                DLL,
                DRR
        );
    }
}
