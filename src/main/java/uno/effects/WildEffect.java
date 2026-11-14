package uno.effects;

import uno.model.CardColor;

import java.util.Optional;
import java.util.Objects;

public final class WildEffect implements CardEffect {

    @Override
    public void apply(GameActions game,
                      Optional<CardColor> chosenColor) {

        Objects.requireNonNull(game);
        Objects.requireNonNull(chosenColor);

        CardColor color = chosenColor.orElseThrow(
                () -> new IllegalArgumentException(
                        "Необходимо выбрать цвет"
                )
        );

        game.changeColor(color);
    }
}