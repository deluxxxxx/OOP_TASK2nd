
package uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uno.effects.NoEffect;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UnoMechanicsTest {

    private Player player1;
    private Player player2;
    private Game game;
    private GameState state;

    private Card unoCard;

    @BeforeEach
    void setUp() {
        player1 = new Player("Игрок 1");
        player2 = new Player("Игрок 2");

        Deck deck = new Deck(new Random(1));
        DiscardPile discardPile = new DiscardPile();

        // Карты для штрафа и дальнейшей игры.
        for (int i = 0; i < 20; i++) {
            deck.addCard(createCard(CardColor.BLUE, CardValue.ONE));
        }

        discardPile.addCard(
                createCard(CardColor.RED, CardValue.FIVE)
        );

        state = new GameState(
                List.of(player1, player2),
                deck,
                discardPile
        );

        state.setCurrentColor(CardColor.RED);

        game = new Game(state);

        // У первого игрока две карты.
        unoCard = createCard(CardColor.RED, CardValue.TWO);

        player1.getHand().addCard(unoCard);
        player1.getHand().addCard(
                createCard(CardColor.GREEN, CardValue.THREE)
        );

        // У второго игрока тоже есть карта.
        player2.getHand().addCard(
                createCard(CardColor.YELLOW, CardValue.FOUR)
        );
    }

    @Test
    void shouldRequireUnoWhenOneCardRemains() {
        game.playCard(unoCard, null);

        assertEquals(1, player1.getHand().size());
        assertEquals(player1, game.getPendingUnoPlayer());
    }

    @Test
    void shouldClearUnoAfterCallingUno() {
        game.playCard(unoCard, null);

        game.callUno(player1);

        assertNull(game.getPendingUnoPlayer());
    }

    @Test
    void shouldGiveTwoPenaltyCardsWhenCaught() {
        game.playCard(unoCard, null);

        int oldSize = player1.getHand().size();

        game.catchUno(player2);

        assertEquals(oldSize + 2, player1.getHand().size());
        assertNull(game.getPendingUnoPlayer());
    }

    private Card createCard(CardColor color, CardValue value) {
        return new Card(
                Optional.of(color),
                value,
                new NoEffect()
        );
    }
}
