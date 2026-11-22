
package uno.commands;

import uno.model.Card;
import uno.model.Game;

public class DrawCardCommand implements Command {

    private final Game game;
    private Card drawnCard;

    public DrawCardCommand(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Игра не может быть null");
        }
        this.game = game;
    }

    @Override
    public void execute() {
        drawnCard = game.drawCard();
    }

    public Card getDrawnCard() {
        return drawnCard;
    }
}
