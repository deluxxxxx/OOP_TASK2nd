package uno.model;

import java.util.ArrayList;
import java.util.List;


public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }

        cards.add(card);
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public List<Card> getCards() {
        return List.copyOf(cards);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}