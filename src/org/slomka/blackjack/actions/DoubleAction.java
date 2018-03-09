package org.slomka.blackjack.actions;

import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.Out;
import org.slomka.blackjack.Player;
import org.slomka.blackjack.menus.Option;

/**
 *
 * Doubles the bet. 
 * 
 * @author Slomka
 */
public class DoubleAction implements Action {

    @Override
    public ActionResponse takeAction(BlackjackTable game, Player player) {
         if(player.isDoubled()) {
            Out.print("You have already doubled down");
            return ActionResponse.CONTINUE;
        }

        int bet = player.getHand().getBet();
        if (bet > player.getCash()) {
            Out.print("You dont have " + bet + " pickles to double down");
            return ActionResponse.CONTINUE;
        }

        player.setCash(player.getCash() - bet);
        player.setDoubled(true);
        player.getHand().setBet(player.getHand().getBet()+bet);
        Out.print("You doubled down the bet to " + bet*2 + " pickles. The more pickles, the better!");
        Option.HIT.run(game, player);
        return ActionResponse.CLOSE_MENU;
    }
    
}
