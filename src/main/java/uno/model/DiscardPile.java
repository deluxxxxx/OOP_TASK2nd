package uno.model;

import java.util.ArrayList;
import java.util.List;


public class DiscardPile {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }

        cards.add(card);
    }

    public Card getTopCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Стопка сброса пуста");
        }

        return cards.get(cards.size() - 1);
    }

    public Card takeTopCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Стопка сброса пуста");
        }

        return cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }
}