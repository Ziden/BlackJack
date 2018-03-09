package org.slomka.blackjack;


import org.slomka.blackjack.BlackjackTable;

/**
 *
 * @author Slomka
 */
public class LauncherClass {

    public static void main(String[] args) {

        // default as the rules stated
        int numberOfDecks = 3;
        
        BlackjackTable game = new BlackjackTable(numberOfDecks);

        // will ask for another game after it finishes
        game.setAutoloop(true);

        // start the game round !
        game.doRound();

    }
}
