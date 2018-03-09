package org.slomka.blackjack.actions;

import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.Hand;
import org.slomka.blackjack.Out;
import org.slomka.blackjack.Player;
import org.slomka.blackjack.deck.Card;

/**
 *
 * Splits your hand in two hands
 *
 * @author Slomka
 */
public class SplitAction implements Action {

    @Override
    public ActionResponse takeAction(BlackjackTable game, Player player) {
        Hand hand = player.getHand();
        
        int bet = hand.getBet();
        if (bet > player.getCash()) {
            Out.print("You dont have " + bet + " pickles to split this hand");
            return ActionResponse.CONTINUE;
        }
        
        player.setCash(player.getCash() - bet);
        
        Out.print("You split this hand into two hands for the price of "+bet+" pickles, still having "+player.getCash()+" pickles.");
        
        Card removedCard = hand.remove(1);
        Hand splitHand = new Hand();
        splitHand.setBet(bet);
        splitHand.add(removedCard);
        game.dealAndConsumeCard(player, splitHand);
        Out.print("Now your split hand have "+splitHand.toString());
        player.getSpliHands().add(splitHand);
        
        game.dealAndConsumeCard(player, hand);
        Out.print("Now your main hand have "+hand.toString());
        player.setSplit(true);
   
        Out.print("Now you will keep playng with your main hand");
        return ActionResponse.CONTINUE;
    }

}
