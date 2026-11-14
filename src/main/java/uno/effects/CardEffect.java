package uno.effects;

import uno.model.CardColor;

@FunctionalInterface
public interface CardEffect {

    void apply(GameActions game, CardColor chosenColor);
}