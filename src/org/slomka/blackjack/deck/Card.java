package org.slomka.blackjack.deck;

import org.slomka.blackjack.Out;

/**
 *
 * @author Slomka
 */
public class Card {

    private CardNumber number;
    private CardSuit suit;
    
    public static enum CardNumber {

        A, N2, N3, N4, N5, N6, N7, N8, N9, N10, K, Q, J;
    }

    public static enum CardSuit {

        CLUBS, HEARTS, SPADES, DIAMONDS;
    }
    
    public Card(CardNumber number, CardSuit suit) {
        this.suit = suit;
        this.number = number;
    }
    
    /**
     * Get the card value in Blackjack Game
     * @return the card value
     */
    public int getValue() {
        if (number.name().startsWith("N")) {
            return Integer.valueOf(number.name().substring(1, number.name().length()));
        }
        if (number == CardNumber.A) {
            return 11;
        } else {
            return 10;
        }
    }

    public CardNumber getNumber() {
        return number;
    }

    public CardSuit getSuit() {
        return suit;
    }

    /**
     * Format the card to display in a letter and the suit name.
     * @return a string with the card number and suit name
     */
    public String toString() {
        String number = this.number.name();
        if (number.charAt(0) == 'N') {
            number = number.substring(1, number.length());
        }
        String name = this.suit.name();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return number + " of " + name;
    }
}
