package uno.model;

/**
 * Направление хода игроков.
 */
public enum Direction {
    CLOCKWISE,
    COUNTER_CLOCKWISE;

    /**
     * Меняет направление на противоположное.
     */
    public Direction reverse() {
        if (this == CLOCKWISE) {
            return COUNTER_CLOCKWISE;
        }

        return CLOCKWISE;
    }
}