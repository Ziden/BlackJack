package org.slomka.blackjack.actions;

import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.Player;

/**
 *
 * End the game, as there is only 1 player playng.
 * 
 * @author Slomka
 */
public class StandAction implements Action {

    @Override
    public ActionResponse takeAction(BlackjackTable game, Player player) {
        return ActionResponse.CLOSE_MENU;
    }

}
