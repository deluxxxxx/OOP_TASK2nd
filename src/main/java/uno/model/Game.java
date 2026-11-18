package uno.model;

import java.util.ArrayList;
import java.util.List;
import uno.effects.GameActions;
import java.util.Optional;

public class Game implements GameActions {
    private final GameState state;
    private boolean skipNextPlayer = false;

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

            player.getHand().addCard(state.getDeck().drawCard());
        }

        skipNextPlayer = true;
    }

    @Override
    public void changeColor(CardColor color) {
        state.setCurrentColor(color);
    }
}
git commit -m "Add card playing mechanics, legal move validation, connect card effects to game mechanics"