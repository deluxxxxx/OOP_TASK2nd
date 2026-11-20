package uno.model;

import java.util.ArrayList;
import java.util.List;
import uno.effects.GameActions;
import java.util.Optional;

public class Game implements GameActions {
    private final GameState state;
    private boolean skipNextPlayer = false;
    private Player unoPlayer;

    public Game(GameState state) {
        if (state == null) {
            throw new IllegalArgumentException("Состояние игры не может быть null");
        }

        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void dealCards() {
        Deck deck = state.getDeck();

        if (deck.size() < state.getPlayers().size() * 7 + 1) {
            throw new IllegalStateException("Недостаточно карт для раздачи");
        }

        for (int i = 0; i < 7; i++) {
            for (Player player : state.getPlayers()) {
                player.getHand().addCard(deck.drawCard());
            }
        }

        Card firstCard = deck.drawCard();
        state.getDiscardPile().addCard(firstCard);

        if (firstCard.getColor().isPresent()) {
            state.setCurrentColor(firstCard.getColor().get());
        } else {
            state.setCurrentColor(CardColor.RED);
        }
    }
    /**
     * Выполняет ход текущего игрока.
     */
    public void playCard(Card card, CardColor chosenColor) {
        if (card == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }
        if (isFinished()) {
            throw new IllegalStateException(
                    "Игра уже завершена"
            );
        }

        Player player = state.getCurrentPlayer();

        if (!player.getHand().getCards().contains(card)) {
            throw new IllegalArgumentException("У игрока нет такой карты");
        }

        if (!GameRules.isLegalMove(card, state)) {
            throw new IllegalArgumentException("Эту карту нельзя положить");
        }

        if (card.getValue().isWild() && chosenColor == null) {
            throw new IllegalArgumentException("Необходимо выбрать цвет");
        }

        player.getHand().removeCard(card);
        state.getDiscardPile().addCard(card);

        if (player.getHand().isEmpty()) {
            unoPlayer = null;
            return;
        }
        if (player.getHand().size() == 1) {
            unoPlayer = player;
        } else {
            unoPlayer = null;
        }


        if (!card.getValue().isWild()) {
            state.setCurrentColor(card.getColor().orElseThrow());
        }

        card.applyEffect(this, Optional.ofNullable(chosenColor));

        state.moveToNextPlayer();

        if (skipNextPlayer) {
            state.moveToNextPlayer();
            skipNextPlayer = false;
        }
    }
    /**
     * Берёт одну карту из колоды для текущего игрока.
     */
    public Card drawCard() {
        if (isFinished()) {
            throw new IllegalStateException(
                    "Игра уже завершена"
            );
        }
        Player player = state.getCurrentPlayer();

        Card card = drawFromDeck();

        player.getHand().addCard(card);

        return card;
    }
    @Override
    public void skipNextPlayer() {
        skipNextPlayer = true;
    }

    @Override
    public void reverseDirection() {
        state.reverseDirection();
    }

    @Override
    public void drawCards(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException(
                    "Количество карт должно быть положительным"
            );
        }
        Player player = state.getNextPlayer();

        for (int i = 0; i < count; i++) {
            if (state.getDeck().isEmpty()) {
                throw new IllegalStateException(
                        "Колода закончилась"
                );
            }

            player.getHand().addCard(drawFromDeck());
        }

        skipNextPlayer = true;
    }

    @Override
    public void changeColor(CardColor color) {
        state.setCurrentColor(color);
    }
    /**
     * Берёт карту из колоды.
     * Если колода пуста, восстанавливает её из стопки сброса.
     */
    private Card drawFromDeck() {
        Deck deck = state.getDeck();
        DiscardPile discardPile = state.getDiscardPile();

        if (deck.isEmpty()) {
            if (discardPile.size() <= 1) {
                throw new IllegalStateException(
                        "Нет карт для восстановления колоды"
                );
            }

            Card topCard = discardPile.takeTopCard();

            while (!discardPile.isEmpty()) {
                deck.addCard(discardPile.takeTopCard());
            }

            discardPile.addCard(topCard);
            deck.shuffle();
        }

        return deck.drawCard();
    }
    /**
     * Игрок объявляет UNO.
     */
    public void callUno(Player player) {
        if (player == null || player != unoPlayer) {
            throw new IllegalArgumentException(
                    "Этот игрок не должен объявлять UNO"
            );
        }

        unoPlayer = null;
    }
    /**
     * Наказывает игрока за забытое объявление UNO.
     */
    public void catchUno(Player player) {
        if (player == null || player != unoPlayer) {
            throw new IllegalArgumentException(
                    "Игрок не нарушил правило UNO"
            );
        }

        for (int i = 0; i < 2; i++) {
            player.getHand().addCard(drawFromDeck());
        }

        unoPlayer = null;
    }
    /**
     * Проверяет, закончилась ли партия.
     */
    public boolean isFinished() {
        for (Player player : state.getPlayers()) {
            if (player.getHand().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Возвращает победителя.
     */
    public Player getWinner() {
        for (Player player : state.getPlayers()) {
            if (player.getHand().isEmpty()) {
                return player;
            }
        }

        return null;
    }
    /**
     * Подсчитывает очки победителя.
     */
    public int calculateWinnerScore() {
        if (!isFinished()) {
            throw new IllegalStateException(
                    "Игра ещё не закончена"
            );
        }

        Player winner = getWinner();
        int score = 0;

        for (Player player : state.getPlayers()) {
            if (player != winner) {
                for (Card card : player.getHand().getCards()) {
                    score += card.getValue().getPoints();
                }
            }
        }

        return score;
    }
}
