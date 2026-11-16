package uno;

import uno.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");
        Player player3 = new Player("Игрок 3");

        Deck deck = new Deck(new Random(42));
        DiscardPile discardPile = new DiscardPile();

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        GameState game = new GameState(players, deck, discardPile);

        System.out.println("Сейчас ходит: "
                + game.getCurrentPlayer().getName());

        game.moveToNextPlayer();

        System.out.println("После перехода: "
                + game.getCurrentPlayer().getName());

        game.reverseDirection();
        game.moveToNextPlayer();

        System.out.println("После смены направления: "
                + game.getCurrentPlayer().getName());
    }
}