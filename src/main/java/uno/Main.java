package uno;

import uno.model.Card;
import uno.model.CardColor;
import uno.model.CardValue;
import uno.model.Player;
import uno.effects.NoEffect;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("Игрок 1");

        Card card = new Card(
                Optional.of(CardColor.RED),
                CardValue.FIVE,
                new NoEffect()
        );

        player.getHand().addCard(card);

        System.out.println("Игрок: " + player.getName());
        System.out.println("Карты в руке: " + player.getHand().size());
        System.out.println("Карты: " + player.getHand().getCards());
    }
}