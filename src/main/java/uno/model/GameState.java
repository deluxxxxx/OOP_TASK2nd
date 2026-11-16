package uno.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final List<Player> players;
    private final Deck deck;
    private final DiscardPile discardPile;

    private int currentPlayerIndex;
    private Direction direction;
    private CardColor currentColor;

    public GameState(List<Player> players, Deck deck, DiscardPile discardPile) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException(
                    "Для игры необходимо минимум два игрока"
            );
        }

        if (deck == null || discardPile == null) {
            throw new IllegalArgumentException(
                    "Колода и стопка сброса не могут быть null"
            );
        }

        this.players = new ArrayList<>(players);
        this.deck = deck;
        this.discardPile = discardPile;

        this.currentPlayerIndex = 0;
        this.direction = Direction.CLOCKWISE;
        this.currentColor = null;
    }

    public List<Player> getPlayers() {
        return List.copyOf(players);
    }

    public Deck getDeck() {
        return deck;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Direction getDirection() {
        return direction;
    }

    public CardColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(CardColor color) {
        if (color == null) {
            throw new IllegalArgumentException("Цвет не может быть null");
        }

        this.currentColor = color;
    }

    public void reverseDirection() {
        direction = direction.reverse();
    }

    public void moveToNextPlayer() {
        currentPlayerIndex += direction == Direction.CLOCKWISE ? 1 : -1;

        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }

        if (currentPlayerIndex < 0) {
            currentPlayerIndex = players.size() - 1;
        }
    }


    public void skipNextPlayer() {
        moveToNextPlayer();
        moveToNextPlayer();
    }
}