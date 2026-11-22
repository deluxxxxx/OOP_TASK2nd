
package uno.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uno.effects.NoEffect;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Player player1;
    private Player player2;
    private Game game;
    private GameState state;



    @BeforeEach
    void setUp() {
        player1 = new Player("Игрок 1");
        player2 = new Player("Игрок 2");

        Deck deck = new Deck(new Random(1));
        DiscardPile discardPile = new DiscardPile();

        for (int i = 0; i < 30; i++) {
            deck.addCard(new Card(
                    Optional.of(CardColor.BLUE),
                    CardValue.ONE,
                    new NoEffect()
            ));
        }

        state = new GameState(
                List.of(player1, player2),
                deck,
                discardPile
        );

        game = new Game(state);
    }


    @Test
    void shouldDealSevenCardsToEachPlayer() {
        game.dealCards();

        assertEquals(7, player1.getHand().size());
        assertEquals(7, player2.getHand().size());
        assertEquals(1, state.getDiscardPile().size());
    }

    @Test
    void shouldDrawCardForCurrentPlayer() {
        game.dealCards();

        int oldSize = player1.getHand().size();

        game.drawCard();

        assertEquals(oldSize + 1, player1.getHand().size());
    }

    @Test
    void shouldRejectNullCard() {
        game.dealCards();

        assertThrows(
                IllegalArgumentException.class,
                () -> game.playCard(null, null)
        );
    }


    @Test
    void shouldFinishGameWhenPlayerHasNoCards() {
        Card card = new Card(
                Optional.of(CardColor.RED),
                CardValue.ONE,
                new NoEffect()
        );

        player1.getHand().addCard(card);

        player2.getHand().addCard(new Card(
                Optional.of(CardColor.BLUE),
                CardValue.TWO,
                new NoEffect()
        ));

        state.getDiscardPile().addCard(new Card(
                Optional.of(CardColor.RED),
                CardValue.FIVE,
                new NoEffect()
        ));

        state.setCurrentColor(CardColor.RED);

        game.playCard(card, null);

        assertTrue(game.isFinished());
        assertEquals(player1, game.getWinner());
    }


    @Test
    void shouldCalculateWinnerScore() {
        Card winningCard = new Card(
                Optional.of(CardColor.RED),
                CardValue.ONE,
                new NoEffect()
        );

        player1.getHand().addCard(winningCard);

        player2.getHand().addCard(
                new Card(
                        Optional.of(CardColor.BLUE),
                        CardValue.FIVE,
                        new NoEffect()
                )
        );

        player2.getHand().addCard(
                new Card(
                        Optional.of(CardColor.GREEN),
                        CardValue.SKIP,
                        new NoEffect()
                )
        );

        state.getDiscardPile().addCard(
                new Card(
                        Optional.of(CardColor.RED),
                        CardValue.TWO,
                        new NoEffect()
                )
        );

        state.setCurrentColor(CardColor.RED);

        game.playCard(winningCard, null);

        assertEquals(25, game.calculateWinnerScore());
    }


    @Test
    void shouldRejectIllegalMove() {
        Card illegalCard = new Card(
                Optional.of(CardColor.BLUE),
                CardValue.THREE,
                new NoEffect()
        );

        player1.getHand().addCard(illegalCard);

        player2.getHand().addCard(new Card(
                Optional.of(CardColor.GREEN),
                CardValue.TWO,
                new NoEffect()
        ));

        state.getDiscardPile().addCard(new Card(
                Optional.of(CardColor.RED),
                CardValue.FIVE,
                new NoEffect()
        ));

        state.setCurrentColor(CardColor.RED);

        assertThrows(
                IllegalArgumentException.class,
                () -> game.playCard(illegalCard, null)
        );
    }
}
