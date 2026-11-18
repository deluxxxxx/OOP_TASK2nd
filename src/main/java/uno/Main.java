package uno;

import uno.model.Deck;
import uno.model.DeckFactory;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random(42);

        Deck deck = DeckFactory.createStandardDeck(random);

        System.out.println("Колода создана!");
        System.out.println("Количество карт: " + deck.size());

        System.out.println("Проверка вытягивания:");
        System.out.println(deck.drawCard());
        System.out.println("Осталось карт: " + deck.size());
    }
}