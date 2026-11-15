package uno;

import uno.model.Card;
import uno.model.CardColor;
import uno.model.CardValue;
import uno.model.Deck;
import uno.model.DiscardPile;
import uno.effects.NoEffect;

import java.util.Optional;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck(new Random(42));
        DiscardPile discardPile = new DiscardPile();

        Card card1 = new Card(
                Optional.of(CardColor.RED),
                CardValue.FIVE,
                new NoEffect()
        );

        Card card2 = new Card(
                Optional.of(CardColor.BLUE),
                CardValue.TWO,
                new NoEffect()
        );

        deck.addCard(card1);
        deck.addCard(card2);

        System.out.println("Карт в колоде: " + deck.size());

        deck.shuffle();

        Card drawnCard = deck.drawCard();
        discardPile.addCard(drawnCard);

        System.out.println("Карт в колоде: " + deck.size());
        System.out.println("Верхняя карта сброса: " + discardPile.getTopCard());
    }
}