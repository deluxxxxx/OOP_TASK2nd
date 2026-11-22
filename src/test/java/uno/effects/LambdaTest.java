
package uno.effects;

import org.junit.jupiter.api.Test;
import uno.model.CardColor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LambdaTest {

    @Test
    void shouldUseLambdaAsCardEffect() {

        CardEffect effect = (game, color) ->
                game.changeColor(CardColor.BLUE);

        RecordingGameActions game = new RecordingGameActions();

        effect.apply(game, Optional.empty());

        assertEquals(CardColor.BLUE, game.changedColor);
    }

    private static class RecordingGameActions
            implements GameActions {

        private CardColor changedColor;

        @Override
        public void skipNextPlayer() {}

        @Override
        public void reverseDirection() {}

        @Override
        public void drawCards(int count) {}

        @Override
        public void changeColor(CardColor color) {
            changedColor = color;
        }
    }
}
