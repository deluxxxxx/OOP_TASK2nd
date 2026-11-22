package uno.commands;

import uno.model.Card;
import uno.model.CardColor;
import uno.model.Game;

public class PlayCardCommand implements Command {

    private final Game game;
    private final Card card;
    private final CardColor chosenColor;

    public PlayCardCommand(
            Game game,
            Card card,
            CardColor chosenColor
    ) {
        if (game == null || card == null) {
            throw new IllegalArgumentException(
                    "Игра и карта не могут быть null"
            );
        }

        this.game = game;
        this.card = card;
        this.chosenColor = chosenColor;
    }

    @Override
    public void execute() {
        game.playCard(card, chosenColor);
    }
}
