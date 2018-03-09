package org.slomka.blackjack.actions;

import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.Player;

/**
 *
 * @author Slomka
 */
public interface Action {
    
    public abstract ActionResponse takeAction(BlackjackTable game, Player player);
    
}
