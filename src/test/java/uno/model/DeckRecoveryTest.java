
package uno.model;

import org.junit.jupiter.api.Test;
import uno.effects.NoEffect;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeckRecoveryTest {

    @Test
    void shouldRestoreDeckFromDiscardPile() {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");
        player1.getHand().addCard(
                createCard(CardColor.YELLOW, CardValue.ONE)
        );
        player2.getHand().addCard(
                createCard(CardColor.GREEN, CardValue.TWO)
        );
        Deck deck = new Deck(new Random(1));
        DiscardPile discardPile = new DiscardPile();

        Card topCard = createCard(CardColor.RED, CardValue.FIVE);
        Card oldCard1 = createCard(CardColor.BLUE, CardValue.ONE);
        Card oldCard2 = createCard(CardColor.GREEN, CardValue.TWO);

        discardPile.addCard(oldCard1);
        discardPile.addCard(oldCard2);
        discardPile.addCard(topCard);

        GameState state = new GameState(
                List.of(player1, player2),
                deck,
                discardPile
        );

        state.setCurrentColor(CardColor.RED);

        Game game = new Game(state);

        Card drawnCard = game.drawCard();

        assertNotNull(drawnCard);
        assertEquals(1, discardPile.size());
        assertSame(topCard, discardPile.getTopCard());
        assertEquals(1, deck.size());
    }

    @Test
    void shouldThrowWhenNoCardsAvailableForRecovery() {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");

        Deck deck = new Deck(new Random(1));
        DiscardPile discardPile = new DiscardPile();

        discardPile.addCard(
                createCard(CardColor.RED, CardValue.FIVE)
        );

        GameState state = new GameState(
                List.of(player1, player2),
                deck,
                discardPile
        );

        Game game = new Game(state);

        assertThrows(
                IllegalStateException.class,
                game::drawCard
        );
    }

    private Card createCard(CardColor color, CardValue value) {
        return new Card(
                Optional.of(color),
                value,
                new NoEffect()
        );
    }
}
