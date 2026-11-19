
package uno;

import uno.model.*;

import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Player player1 = new Player("Игрок 1");
        Player player2 = new Player("Игрок 2");
        Player player3 = new Player("Игрок 3");

        Deck deck = DeckFactory.createStandardDeck(new Random(42));

        DiscardPile discardPile = new DiscardPile();

        GameState state = new GameState(
                List.of(player1, player2, player3),
                deck,
                discardPile
        );

        Game game = new Game(state);

        game.dealCards();

        System.out.println("========== UNO ==========");

        for (Player player : state.getPlayers()) {
            System.out.println("\n" + player.getName());
            System.out.println("Карты: " + player.getHand().getCards());
        }

        System.out.println("\nКарта на столе: "
                + discardPile.getTopCard());

        System.out.println("Текущий цвет: "
                + state.getCurrentColor());

        Player currentPlayer = state.getCurrentPlayer();

        System.out.println("\nХодит: "
                + currentPlayer.getName());

        List<Card> legalCards = currentPlayer.getHand()
                .getCards()
                .stream()
                .filter(card -> GameRules.isLegalMove(card, state))
                .toList();

        if (legalCards.isEmpty()) {

            System.out.println("Нет подходящих карт.");

            Card drawnCard = game.drawCard();

            System.out.println("Взятая карта: " + drawnCard);

        } else {

            Card selectedCard = legalCards.get(0);

            System.out.println("Игрок выкладывает: "
                    + selectedCard);

            CardColor chosenColor = selectedCard.getValue().isWild()
                    ? CardColor.RED
                    : null;

            game.playCard(selectedCard, chosenColor);
        }

        // Показываем результат хода
        System.out.println("\n========== РЕЗУЛЬТАТ ==========");

        for (Player player : state.getPlayers()) {
            System.out.println(
                    player.getName()
                            + ": "
                            + player.getHand().size()
                            + " карт"
            );
        }

        System.out.println("Следующий игрок: "
                + state.getCurrentPlayer().getName());

        if (game.isFinished()) {

            System.out.println("\nПобедитель: "
                    + game.getWinner().getName());

            System.out.println("Очки победителя: "
                    + game.calculateWinnerScore());

        } else {
            System.out.println("\nИгра продолжается.");
        }
    }
}
