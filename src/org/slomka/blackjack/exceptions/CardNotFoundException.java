package org.slomka.blackjack.exceptions;

import org.slomka.blackjack.deck.Card;

/**
 *
 * @author Slomka
 */
public class CardNotFoundException extends Exception {

    private Card card;
    
    public CardNotFoundException(Card c) {
        this.card = c;
    }
    
    public Card getCard() {
        return card;
    }
    
}
