package org.slomka.blackjack.actions;

import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.Out;
import org.slomka.blackjack.Player;

/**
 *
 * Activates 'insurance mode'.
 * 
 * If the Dealear reveals a Blackjack in the next turn, insurance mode reward you.
 * 
 * @author Slomka
 */
public class InsureAction implements Action {

    @Override
    public ActionResponse takeAction(BlackjackTable game, Player player) {
         if(player.isInsured()) {
            Out.print("You are already insured");
            return ActionResponse.CONTINUE;
        }

        int insuranceCost = player.getHand().getBet() / 2;
        if (insuranceCost > player.getCash()) {
            Out.print("You dont have " + insuranceCost + " pickles to insure");
            return ActionResponse.CONTINUE;
        }

        player.setCash(player.getCash() - insuranceCost);
        player.setInsured(true);
        Out.print("You have insured the game for " + insuranceCost + " pickles, leaving you with " + player.getCash() + " pickles.");
        return ActionResponse.CONTINUE;
    }
    
}
