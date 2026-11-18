package uno.model;
/**
 * Содержит основные правила допустимости ходов.
 */
public class GameRules {
    /**
     * Проверяет, можно ли положить выбранную карту.
     */
    public static boolean isLegalMove(
            Card card,
            GameState state
    ) {
        if (card == null || state == null) {
            return false;
        }

        if (card.getValue().isWild()) {
            return true;
        }

        CardColor currentColor = state.getCurrentColor();

        Card topCard = state.getDiscardPile().getTopCard();

        if (card.getColor().orElseThrow() == currentColor) {
            return true;
        }

        return card.getValue() == topCard.getValue();
    }
}