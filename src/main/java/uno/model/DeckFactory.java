package uno.model;

import uno.effects.DrawTwoEffect;
import uno.effects.NoEffect;
import uno.effects.ReverseEffect;
import uno.effects.SkipEffect;
import uno.effects.WildDrawFourEffect;
import uno.effects.WildEffect;

import java.util.Random;

public class DeckFactory {

    public static Deck createStandardDeck(Random random) {
        Deck deck = new Deck(random);

        for (CardColor color : CardColor.values()) {

            deck.addCard(new Card(
                    java.util.Optional.of(color),
                    CardValue.ZERO,
                    new NoEffect()
            ));

            for (int i = 1; i <= 9; i++) {
                CardValue value = CardValue.values()[i];

                deck.addCard(new Card(
                        java.util.Optional.of(color),
                        value,
                        new NoEffect()
                ));

                deck.addCard(new Card(
                        java.util.Optional.of(color),
                        value,
                        new NoEffect()
                ));
            }

            for (int i = 0; i < 2; i++) {
                deck.addCard(new Card(
                        java.util.Optional.of(color),
                        CardValue.SKIP,
                        new SkipEffect()
                ));

                deck.addCard(new Card(
                        java.util.Optional.of(color),
                        CardValue.REVERSE,
                        new ReverseEffect()
                ));

                deck.addCard(new Card(
                        java.util.Optional.of(color),
                        CardValue.DRAW_TWO,
                        new DrawTwoEffect()
                ));
            }
        }
        for (int i = 0; i < 4; i++) {
            deck.addCard(new Card(
                    java.util.Optional.empty(),
                    CardValue.WILD,
                    new WildEffect()
            ));

            deck.addCard(new Card(
                    java.util.Optional.empty(),
                    CardValue.WILD_DRAW_FOUR,
                    new WildDrawFourEffect()
            ));
        }

        deck.shuffle();

        return deck;
    }
}