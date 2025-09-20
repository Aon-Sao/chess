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

    public Direction getDirection() {
        return direction;
    }

    public static Collection<Direction> getOrthogonals() {
        return List.of(
                LEFT.direction,
                RIGHT.direction,
                UP.direction,
                DOWN.direction
        );
    }

    public static Collection<Direction> getDiagonals() {
        return List.of(
                UP_LEFT.direction,
                UP_RIGHT.direction,
                DOWN_LEFT.direction,
                DOWN_RIGHT.direction
        );
    }

    public static Collection<Direction> getKnights() {
        return List.of(
                UUL.direction,
                UUR.direction,
                ULL.direction,
                URR.direction,
                DDL.direction,
                DDR.direction,
                DLL.direction,
                DRR.direction
        );
    }
}
