package uno;

import uno.model.*;

import java.util.List;
import java.util.Scanner;

public class GameRunner {

    private final Game game;
    private final Scanner scanner;

    public GameRunner(Game game, Scanner scanner) {
        this.game = game;
        this.scanner = scanner;
    }

    public void start() {

        GameState state = game.getState();

        while (!game.isFinished()) {

            Player player = state.getCurrentPlayer();

            // Проверяем, не забыл ли предыдущий игрок объявить UNO
            Player pendingPlayer = game.getPendingUnoPlayer();

            if (pendingPlayer != null && pendingPlayer != player) {

                System.out.println("\nИгрок " + pendingPlayer.getName()
                        + " забыл объявить UNO!");

                System.out.println(
                        "Введите CATCH, чтобы поймать его, или Enter, чтобы пропустить:"
                );

                String answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("CATCH")) {

                    game.catchUno(player);

                    System.out.println(
                            pendingPlayer.getName()
                                    + " получает штрафные 2 карты!"
                    );

                } else {
                    game.expireUnoOpportunity();
                }
            }

            System.out.println("\n======================");
            System.out.println("Ход игрока: " + player.getName());

            System.out.println("Верхняя карта: "
                    + state.getDiscardPile().getTopCard());

            System.out.println("Текущий цвет: "
                    + state.getCurrentColor());

            List<Card> cards = player.getHand().getCards();

            System.out.println("\nВаши карты:");

            for (int i = 0; i < cards.size(); i++) {
                System.out.println(i + " — " + cards.get(i));
            }

            boolean hasMove = game.hasLegalMove();

            if (!hasMove) {
                System.out.println("\nНет подходящих карт.");
                System.out.println("Нужно взять карту.");
            } else {
                System.out.println("\nВыберите номер карты.");
                System.out.println("-1 — взять карту.");
            }

            System.out.print("Ваш выбор: ");

            String input = scanner.nextLine();

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Введите число.");
                continue;
            }

            try {

                if (choice == -1) {

                    Card drawnCard = game.drawCard();

                    System.out.println("Вы взяли: " + drawnCard);

                    if (GameRules.isLegalMove(drawnCard, state)) {

                        System.out.println(
                                "Эту карту можно выложить. Играть? (да/нет)"
                        );

                        String answer = scanner.nextLine();

                        if (answer.equalsIgnoreCase("да")) {

                            CardColor chosenColor = chooseColor(drawnCard);

                            game.playCard(drawnCard, chosenColor);

                            handleUno(player);

                        } else {

                            game.passTurn();
                        }

                    } else {

                        System.out.println(
                                "Карта не подходит. Ход переходит дальше."
                        );

                        game.passTurn();
                    }

                } else if (choice >= 0 && choice < cards.size()) {

                    Card selectedCard = cards.get(choice);

                    if (!GameRules.isLegalMove(selectedCard, state)) {
                        System.out.println("Эту карту нельзя выложить.");
                        continue;
                    }

                    CardColor chosenColor = chooseColor(selectedCard);

                    game.playCard(selectedCard, chosenColor);

                    handleUno(player);

                } else {

                    System.out.println("Неверный номер карты.");
                }

            } catch (IllegalArgumentException |
                     IllegalStateException exception) {

                System.out.println(exception.getMessage());
            }
        }

        System.out.println("\n========== ИГРА ОКОНЧЕНА ==========");

        System.out.println("Победитель: "
                + game.getWinner().getName());

        System.out.println("Очки победителя: "
                + game.calculateWinnerScore());
    }

    /**
     * Обработка объявления UNO.
     */
    private void handleUno(Player player) {

        if (game.getPendingUnoPlayer() == player) {

            System.out.println(
                    "\nУ вас осталась одна карта! Напишите UNO:"
            );

            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("UNO")) {

                game.callUno(player);

                System.out.println("UNO объявлено!");

            } else {

                System.out.println("Вы не объявили UNO!");
            }
        }
    }

    /**
     * Выбор цвета для карты смены цвета.
     */
    private CardColor chooseColor(Card card) {

        if (!card.getValue().isWild()) {
            return null;
        }

        while (true) {

            System.out.println(
                    "Выберите цвет: RED, YELLOW, GREEN, BLUE"
            );

            String input = scanner.nextLine();

            try {

                return CardColor.valueOf(input.toUpperCase());

            } catch (IllegalArgumentException exception) {

                System.out.println("Такого цвета нет.");
            }
        }
    }
}
