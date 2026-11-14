package uno.model;

public enum CardValue {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,

    SKIP,
    REVERSE,
    DRAW_TWO,

    WILD,
    WILD_DRAW_FOUR;

    public boolean isWild() {
        return this == WILD || this == WILD_DRAW_FOUR;
    }
}
