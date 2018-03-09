package org.slomka.blackjack;

import java.util.ArrayList;
import java.util.List;
import org.slomka.blackjack.deck.Card;

/**
 *
 * @author Slomka
 */
public class Hand extends ArrayList<Card> {

    /*
    * As splitting rules, you bet on a hand not on a game.
    */
    private int bet = 0;
    
    public boolean hasPair() {
        return size() == 2 && get(0).getValue() == get(1).getValue();
    }

    public boolean hasAce() {
        return stream().anyMatch(card -> card.getNumber() == Card.CardNumber.A);
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
    
    public String toString() {
        String s = "";
        for(int x = 0 ; x < size() ; x++) {
            Card c = get(x);
            s += c.toString();
            if(x==size()-2) {
                s+=" and a ";
            } else if(x<size()-2) {
                s+=", a ";
            }
        }
        return s;
    }

    
}
