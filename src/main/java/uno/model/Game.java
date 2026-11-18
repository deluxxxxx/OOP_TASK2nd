package uno.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final GameState state;

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
}