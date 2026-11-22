
package uno;

import uno.model.*;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");
        Player player3 = new Player("Игрок 3");

        Deck deck = DeckFactory.createStandardDeck(new Random());

        DiscardPile discardPile = new DiscardPile();

        GameState state = new GameState(
                List.of(player1, player2, player3),
                deck,
                discardPile
        );

        Game game = new Game(state);

        game.dealCards();

        Scanner scanner = new Scanner(System.in);

        GameRunner runner = new GameRunner(game, scanner);

        runner.start();
    }
}
