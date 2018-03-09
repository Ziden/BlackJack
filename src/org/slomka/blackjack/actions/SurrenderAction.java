package org.slomka.blackjack.actions;

import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.Out;
import org.slomka.blackjack.Player;

/**
 *
 * End the game but saves 50% of your bet.
 * 
 * @author Slomka
 */
public class SurrenderAction implements Action {

    @Override
    public ActionResponse takeAction(BlackjackTable game, Player player) {
        Out.print("You surrendered the game and got "+(player.getHand().getBet()/2)+" pickles back!");
        // payng half his bet back
        player.setCash(player.getCash() + (player.getHand().getBet()/2));
        game.endGame(BlackjackTable.GameResult.LOOSE);
        return ActionResponse.CLOSE_MENU;
    }
    
}
