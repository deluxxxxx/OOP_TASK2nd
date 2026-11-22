
package uno.model;

import org.junit.jupiter.api.Test;
import uno.effects.NoEffect;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ScoreCalculationTest {

    @Test
    void shouldCalculateWinnerScore() {
        Player winner = new Player("Победитель");
        Player player2 = new Player("Игрок 2");
        Player player3 = new Player("Игрок 3");

        player2.getHand().addCard(
                createCard(CardColor.RED, CardValue.FIVE)
        );
        player2.getHand().addCard(
                createCard(CardColor.BLUE, CardValue.SKIP)
        );

        player3.getHand().addCard(
                new Card(
                        Optional.empty(),
                        CardValue.WILD,
                        new NoEffect()
                )
        );

        GameState state = new GameState(
                List.of(winner, player2, player3),
                new Deck(new Random(1)),
                new DiscardPile()
        );

        Game game = new Game(state);

        assertTrue(game.isFinished());
        assertEquals(75, game.calculateWinnerScore());
        assertSame(winner, game.getWinner());
    }

    @Test
    void shouldNotCalculateScoreBeforeGameEnds() {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");

        player1.getHand().addCard(
                createCard(CardColor.RED, CardValue.ONE)
        );
        player2.getHand().addCard(
                createCard(CardColor.BLUE, CardValue.TWO)
        );

        GameState state = new GameState(
                List.of(player1, player2),
                new Deck(new Random(1)),
                new DiscardPile()
        );

        Game game = new Game(state);

        assertThrows(
                IllegalStateException.class,
                game::calculateWinnerScore
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
