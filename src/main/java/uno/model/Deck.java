package uno.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final Random random;


    public Deck(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Генератор не может быть null");
        }

        this.random = random;
    }

    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }

        cards.add(card);
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Колода пуста");
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