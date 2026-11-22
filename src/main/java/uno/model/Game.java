
package uno.model;

import uno.effects.GameActions;
import java.util.Optional;

public class Game implements GameActions {

    private final GameState state;
    private boolean skipNextPlayer = false;
    private Player pendingUnoPlayer;

    public Game(GameState state) {
        if (state == null) {
            throw new IllegalArgumentException(
                    "Состояние игры не может быть null"
            );
        }

        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    /**
     * Раздаёт каждому игроку по 7 карт.
     */
    public void dealCards() {
        Deck deck = state.getDeck();

        if (deck.size() < state.getPlayers().size() * 7 + 1) {
            throw new IllegalStateException(
                    "Недостаточно карт для раздачи"
            );
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
            throw new IllegalArgumentException(
                    "Карта не может быть null"
            );
        }

        if (isFinished()) {
            throw new IllegalStateException(
                    "Игра уже завершена"
            );
        }

        Player player = state.getCurrentPlayer();

        if (!player.getHand().getCards().contains(card)) {
            throw new IllegalArgumentException(
                    "У игрока нет такой карты"
            );
        }

        if (!GameRules.isLegalMove(card, state)) {
            throw new IllegalArgumentException(
                    "Эту карту нельзя положить"
            );
        }

        if (card.getValue().isWild() && chosenColor == null) {
            throw new IllegalArgumentException(
                    "Необходимо выбрать цвет"
            );
        }

        if (pendingUnoPlayer != null && pendingUnoPlayer != player) {
            pendingUnoPlayer = null;
        }

        player.getHand().removeCard(card);
        state.getDiscardPile().addCard(card);

        if (!card.getValue().isWild()) {
            state.setCurrentColor(card.getColor().orElseThrow());
        }

        // Применяем эффект даже в том случае,
        // если эта карта последняя у игрока.
        card.applyEffect(this, Optional.ofNullable(chosenColor));

        // Если карт больше нет, игрок побеждает.
        if (player.getHand().isEmpty()) {
            pendingUnoPlayer = null;
            return;
        }

        // Если осталась одна карта, нужно объявить UNO.
        if (player.getHand().size() == 1) {
            pendingUnoPlayer = player;
        }

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

        if (state.getPlayers().size() == 2) {
            skipNextPlayer = true;
        } else {
            state.reverseDirection();
        }
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
            player.getHand().addCard(drawFromDeck());
        }

        skipNextPlayer = true;
    }

    @Override
    public void changeColor(CardColor color) {

        if (color == null) {
            throw new IllegalArgumentException(
                    "Цвет не может быть null"
            );
        }

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

        if (player == null || player != pendingUnoPlayer) {
            throw new IllegalArgumentException(
                    "Этот игрок не должен объявлять UNO"
            );
        }

        pendingUnoPlayer = null;
    }

    /**
     * Другой игрок ловит нарушителя.
     */
    public void catchUno(Player catcher) {

        if (pendingUnoPlayer == null) {
            throw new IllegalStateException(
                    "Сейчас никто не нарушил правило UNO"
            );
        }

        if (catcher == null || catcher == pendingUnoPlayer) {
            throw new IllegalArgumentException(
                    "Нельзя поймать самого себя"
            );
        }

        if (catcher != state.getCurrentPlayer()) {
            throw new IllegalArgumentException(
                    "Поймать нарушителя может только текущий игрок"
            );
        }

        Player offender = pendingUnoPlayer;

        for (int i = 0; i < 2; i++) {
            offender.getHand().addCard(drawFromDeck());
        }

        pendingUnoPlayer = null;
    }

    /**
     * Возвращает игрока, который должен объявить UNO.
     */
    public Player getPendingUnoPlayer() {
        return pendingUnoPlayer;
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
    /**
     * Завершает ход текущего игрока без выкладывания карты.
     */
    public void passTurn() {
        if (isFinished()) {
            throw new IllegalStateException("Игра уже завершена");
        }

        state.moveToNextPlayer();
    }
    /**
     * Проверяет, есть ли у текущего игрока допустимый ход.
     */
    public boolean hasLegalMove() {

        Player player = state.getCurrentPlayer();

        for (Card card : player.getHand().getCards()) {
            if (GameRules.isLegalMove(card, state)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Завершает возможность поймать игрока,
     * забывшего объявить UNO.
     */
    public void expireUnoOpportunity() {
        pendingUnoPlayer = null;
    }
}
