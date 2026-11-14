package uno.effects;

import uno.model.CardColor;

import java.util.Optional;

@FunctionalInterface
public interface CardEffect {

    void apply(GameActions game, Optional<CardColor> chosenColor);
}