package uno.commands;

import org.junit.jupiter.api.Test;
import uno.effects.NoEffect;
import uno.model.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void shouldDrawCardUsingCommand() {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");

        Deck deck = new Deck(new Random(1));
        Card card = createCard(CardColor.RED, CardValue.FIVE);
        deck.addCard(card);

        GameState state = new GameState(
                List.of(player1, player2),
                deck,
                new DiscardPile()
        );

        Game game = new Game(state);

        player1.getHand().addCard(
                createCard(CardColor.BLUE, CardValue.ONE)
        );
        player2.getHand().addCard(
                createCard(CardColor.GREEN, CardValue.TWO)
        );

        DrawCardCommand command = new DrawCardCommand(game);
        command.execute();

        assertSame(card, command.getDrawnCard());
        assertEquals(2, player1.getHand().size());
    }

    @Test
    void shouldPlayCardUsingCommand() {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");

        Card card = createCard(CardColor.RED, CardValue.FIVE);

        player1.getHand().addCard(card);
        player1.getHand().addCard(
                createCard(CardColor.BLUE, CardValue.ONE)
        );
        player2.getHand().addCard(
                createCard(CardColor.GREEN, CardValue.TWO)
        );

        DiscardPile discardPile = new DiscardPile();
        discardPile.addCard(
                createCard(CardColor.RED, CardValue.THREE)
        );

        GameState state = new GameState(
                List.of(player1, player2),
                new Deck(new Random(1)),
                discardPile
        );

        state.setCurrentColor(CardColor.RED);

        Game game = new Game(state);

        PlayCardCommand command =
                new PlayCardCommand(game, card, null);

        command.execute();

        assertFalse(player1.getHand().getCards().contains(card));
        assertSame(card, discardPile.getTopCard());
    }

    @Test
    void shouldPassTurnUsingCommand() {
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

        PassTurnCommand command = new PassTurnCommand(game);
        command.execute();

        assertSame(player2, state.getCurrentPlayer());
    }

    private Card createCard(CardColor color, CardValue value) {
        return new Card(
                Optional.of(color),
                value,
                new NoEffect()
        );
    }
}
