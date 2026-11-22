package uno.commands;

import uno.model.Game;

public class PassTurnCommand implements Command {

    private final Game game;

    public PassTurnCommand(Game game) {
        if (game == null) {
            throw new IllegalArgumentException(
                    "Игра не может быть null"
            );
        }

        this.game = game;
    }

    @Override
    public void execute() {
        game.passTurn();
    }
}
