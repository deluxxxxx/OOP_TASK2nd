package uno;

import uno.effects.SkipEffect;
import uno.model.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");
        Player player3 = new Player("Игрок 3");

        Deck deck = DeckFactory.createStandardDeck(new Random(42));

        DiscardPile discardPile = new DiscardPile();

        Card topCard = new Card(
                Optional.of(CardColor.RED),
                CardValue.FIVE,
                new uno.effects.NoEffect()
        );

        discardPile.addCard(topCard);

        GameState state = new GameState(
                List.of(player1, player2, player3),
                deck,
                discardPile
        );

        state.setCurrentColor(CardColor.RED);

        Card skipCard = new Card(
                Optional.of(CardColor.RED),
                CardValue.SKIP,
                new SkipEffect()
        );

        player1.getHand().addCard(skipCard);

        Game game = new Game(state);

        System.out.println("До хода: "
                + state.getCurrentPlayer().getName());

        game.playCard(skipCard, null);

        System.out.println("После хода: "
                + state.getCurrentPlayer().getName());
    }
}