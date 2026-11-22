
package uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uno.effects.NoEffect;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameRulesTest {

    private GameState state;

    @BeforeEach
    void setUp() {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");

        Deck deck = new Deck(new Random(1));
        DiscardPile discardPile = new DiscardPile();

        state = new GameState(
                List.of(player1, player2),
                deck,
                discardPile
        );

        Card topCard = new Card(
                Optional.of(CardColor.RED),
                CardValue.FIVE,
                new NoEffect()
        );

        discardPile.addCard(topCard);
        state.setCurrentColor(CardColor.RED);
    }

    @Test
    void shouldAllowCardWithSameColor() {
        Card card = new Card(
                Optional.of(CardColor.RED),
                CardValue.TWO,
                new NoEffect()
        );

        assertTrue(GameRules.isLegalMove(card, state));
    }

    @Test
    void shouldAllowCardWithSameValue() {
        Card card = new Card(
                Optional.of(CardColor.BLUE),
                CardValue.FIVE,
                new NoEffect()
        );

        assertTrue(GameRules.isLegalMove(card, state));
    }

    @Test
    void shouldRejectCardWithDifferentColorAndValue() {
        Card card = new Card(
                Optional.of(CardColor.GREEN),
                CardValue.TWO,
                new NoEffect()
        );

        assertFalse(GameRules.isLegalMove(card, state));
    }

    @Test
    void shouldAllowWildCard() {
        Card card = new Card(
                Optional.empty(),
                CardValue.WILD,
                new NoEffect()
        );

        assertTrue(GameRules.isLegalMove(card, state));
    }

    @Test
    void shouldRejectNullCard() {
        assertFalse(GameRules.isLegalMove(null, state));
    }

    @Test
    void shouldRejectNullState() {
        Card card = new Card(
                Optional.of(CardColor.RED),
                CardValue.TWO,
                new NoEffect()
        );

        assertFalse(GameRules.isLegalMove(card, null));
    }
}
