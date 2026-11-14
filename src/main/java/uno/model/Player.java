package uno.model;

public class Player {
    private final String name;
    private final Hand hand;

    public Player(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя игрока не может быть пустым");
        }

        this.name = name;
        this.hand = new Hand();
    }
    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }
}