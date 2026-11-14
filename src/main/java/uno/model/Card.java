package uno.model;

import uno.effects.CardEffect;
import uno.effects.GameActions;

import java.util.Objects;
import java.util.Optional;

public class Card {

    private final Optional<CardColor> color;
    private final CardValue value;
    private final CardEffect effect;

    public Card(Optional<CardColor> color,
                CardValue value,
                CardEffect effect) {

        this.color = Objects.requireNonNull(color);
        this.value = Objects.requireNonNull(value);
        this.effect = Objects.requireNonNull(effect);

        if (value.isWild() && color.isPresent()) {
            throw new IllegalArgumentException(
                    "У карты смены цвета не должно быть своего цвета"
            );
        }

        if (!value.isWild() && color.isEmpty()) {
            throw new IllegalArgumentException(
                    "У обычной карты должен быть цвет"
            );
        }
    }

    public Optional<CardColor> getColor() {
        return color;
    }

    public CardValue getValue() {
        return value;
    }

    public void applyEffect(GameActions game, CardColor chosenColor) {
        effect.apply(game, chosenColor);
    }

    @Override
    public String toString() {
        return color.map(Enum::name).orElse("WILD")
                + " " + value;
    }
}