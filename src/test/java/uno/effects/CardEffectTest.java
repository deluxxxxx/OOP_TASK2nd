package uno.effects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uno.model.CardColor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CardEffectTest {

    private RecordingGameActions game;

    @BeforeEach
    void setUp() {
        game = new RecordingGameActions();
    }

    @Test
    void shouldDoNothing() {
        CardEffect effect = new NoEffect();

        effect.apply(game, Optional.empty());

        assertEquals(0, game.skipCount);
        assertEquals(0, game.reverseCount);
        assertEquals(0, game.drawCount);
    }

    @Test
    void shouldSkipNextPlayer() {
        CardEffect effect = new SkipEffect();

        effect.apply(game, Optional.empty());

        assertEquals(1, game.skipCount);
    }

    @Test
    void shouldReverseDirection() {
        CardEffect effect = new ReverseEffect();

        effect.apply(game, Optional.empty());

        assertEquals(1, game.reverseCount);
    }

    @Test
    void shouldDrawTwoCards() {
        CardEffect effect = new DrawTwoEffect();

        effect.apply(game, Optional.empty());

        assertEquals(2, game.drawCount);
    }

    @Test
    void shouldChangeColor() {
        CardEffect effect = new WildEffect();

        effect.apply(game, Optional.of(CardColor.BLUE));

        assertEquals(CardColor.BLUE, game.changedColor);
    }

    @Test
    void shouldChangeColorAndDrawFourCards() {
        CardEffect effect = new WildDrawFourEffect();

        effect.apply(game, Optional.of(CardColor.GREEN));

        assertEquals(CardColor.GREEN, game.changedColor);
        assertEquals(4, game.drawCount);
    }

    // Вспомогательный класс для проверки вызовов эффектов.
    private static class RecordingGameActions implements GameActions {

        private int skipCount = 0;
        private int reverseCount = 0;
        private int drawCount = 0;
        private CardColor changedColor;

        @Override
        public void skipNextPlayer() {
            skipCount++;
        }

        @Override
        public void reverseDirection() {
            reverseCount++;
        }

        @Override
        public void drawCards(int count) {
            drawCount += count;
        }

        @Override
        public void changeColor(CardColor color) {
            changedColor = color;
        }
    }
}
