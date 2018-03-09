package org.slomka.blackjack;

import org.slomka.blackjack.actions.ActionResponse;
import org.slomka.blackjack.deck.Card;
import org.slomka.blackjack.deck.Deck;
import org.slomka.blackjack.menus.Option;

/**
 *
 * @author Slomka
 */
public class Dealer extends Player {

    // A Iddle dealer wont hit cards on his turn
    private boolean iddle = false;
    
    public String getName() {
        return "The Dealer";
    }
    
    public void takeTurn(BlackjackTable table) {
        if(iddle || table.getPlayer().isDoubled())
            return;
        int playerPoints = table.getPlayer().getPoints();
        int dealerPoints = table.getDealer().getPoints();
        // can be null to make testing easyer
        if (table.getFaceDownCard() != null) {
            Out.print("The Dealer turn the faced down card up. Its a " + table.getFaceDownCard().toString());
        }
        Out.print("The dealer have " + dealerPoints + " points.");
        Out.print("Dealer is thinking.");
        dealerPoints = getPoints();
        if (playerPoints > dealerPoints || playerPoints == dealerPoints && playerPoints <= 17) {
            Out.print("Dealer decided to take a pickle hit.");
            ActionResponse result = Option.HIT.run(table, this);
            dealerPoints = getPoints();
            while (result != ActionResponse.CLOSE_MENU) {
                dealerPoints = getPoints();
                if (playerPoints > dealerPoints) {
                    Out.print("Dealer decided to take a pickle hit.");
                    result = Option.HIT.run(table, this);
                } else {
                    break;
                }
            }
        } 
        if (table.getGameResult() == null) {
            Out.print("The Dealer has called Stand");
        }
    }

    public boolean isIddle() {
        return iddle;
    }

    public void setIddle(boolean iddle) {
        this.iddle = iddle;
    }
    
    

}
