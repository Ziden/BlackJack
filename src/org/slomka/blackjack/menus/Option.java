package org.slomka.blackjack.menus;

import org.slomka.blackjack.BlackjackTable;
import org.slomka.blackjack.Player;
import org.slomka.blackjack.actions.Action;
import org.slomka.blackjack.actions.ActionResponse;
import org.slomka.blackjack.actions.DoubleAction;
import org.slomka.blackjack.actions.HitAction;
import org.slomka.blackjack.actions.InsureAction;
import org.slomka.blackjack.actions.SplitAction;
import org.slomka.blackjack.actions.StandAction;
import org.slomka.blackjack.actions.SurrenderAction;

/**
 *
 * Possible opions in a Blackjack game
 * 
 * @author Slomka
 */
public enum Option {

    // TODO- add generic classloader
    SURRENDER(new SurrenderAction()),
    SPLIT(new SplitAction()),
    STAND(new StandAction()),
    HIT(new HitAction()),
    DOUBLE(new DoubleAction()),
    INSURE(new InsureAction()),
    PLAY,
    QUIT;
    
    private Action action;
    
    public ActionResponse run(BlackjackTable game, Player player) {
        if(action != null) {
            return action.takeAction(game, player);
        }
        return null;
    }
    
    private Option(Action action) {
        this.action = action;
    }
    
    private Option() {
        
    }
    
}
