package org.slomka.blackjack.actions;

import org.slomka.blackjack.Dealer;
import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.BlackjackTable.GameResult;
import org.slomka.blackjack.Out;
import org.slomka.blackjack.Player;

/**
 *
 * Asks for another card. 
 * May 'burst' if your points hits bigger then 21
 * 
 * @author Slomka
 */
public class HitAction implements Action {

    @Override
    public ActionResponse takeAction(BlackjackTable game, Player player) {
        game.dealAndConsumeCard(player);

        if (game.getConfig().isFiveCharlieEnabled() && !(player instanceof Dealer)) {
            if (player.getHand().size() == 5) {
                player.setCash(player.getCash() + player.getHand().getBet());
                Out.print("As Five Card Charlie rule states, you got your bet of "+player.getHand().getBet()+" back for hitting 5 cards, now you have "+player.getCash()+" pickles ");
            }
        }
        int points = player.getPoints();
        if (points > 21) {
            if (player instanceof Dealer) {
                Out.print("The Dealer has busted!");
                game.endGame(GameResult.WIN);
                return ActionResponse.CLOSE_MENU;
            } else {
                Out.print("You have busted!");
                game.endGame(GameResult.LOOSE);
                return ActionResponse.CLOSE_MENU;
            }
        } else {
            Out.print("Now " + player.getName() + " have " + player.getPoints() + " points and have " + player.getHand().size() + " cards");
        }
        if(player.isDoubled())
            return ActionResponse.CLOSE_MENU;
        else
            return ActionResponse.CONTINUE;
    }

}
