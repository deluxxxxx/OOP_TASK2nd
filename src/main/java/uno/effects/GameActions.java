package uno.effects;

import uno.model.CardColor;

public interface GameActions {

    void skipNextPlayer();

    void reverseDirection();

    void drawCards(int count);

    void changeColor(CardColor color);
}