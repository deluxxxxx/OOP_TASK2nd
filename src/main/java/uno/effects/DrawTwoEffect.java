package uno.effects;

import uno.model.CardColor;

import java.util.Optional;

public final class DrawTwoEffect implements CardEffect {

    @Override
    public void apply(GameActions game,
                      Optional<CardColor> chosenColor) {
        game.drawCards(2);
    }
}