package org.slomka.blackjack;

import java.util.ArrayList;
import java.util.List;
import org.slomka.blackjack.actions.ActionResponse;
import org.slomka.blackjack.deck.Card;
import org.slomka.blackjack.deck.Card.CardNumber;
import org.slomka.blackjack.menus.MenuResponse;
import org.slomka.blackjack.menus.Option;
import org.slomka.blackjack.menus.OptionBuilder;
import org.slomka.blackjack.menus.OptionMenu;

/**
 *
 * @author Slomka
 */
public class Player {

    private int cash = 0;

    private boolean insured;
    private boolean doubled;
    private boolean split;

    private Hand hand = new Hand();

    private List<Hand> splitHands = new ArrayList<Hand>();

    /**
     * Plays the turn
     *
     * @param game the table this player is playng
     */
    public void takeTurn(BlackjackTable game) {

        // check for the auto-win BJ hand
        // check if we have a starting blackjack before any actions
        if (getPoints() == 21) {
            if (game.getDealer().getPoints() == 21) {
                game.endGame(BlackjackTable.GameResult.PUSH);
                return;
            } else {
                game.endGame(BlackjackTable.GameResult.BJWIN);
                return;
            }
        }

        OptionBuilder options = new OptionBuilder();
        options.message("Select what you want to do.").option(Option.STAND).option(Option.HIT);

        if (!isSplit()) {
            options.option(Option.SURRENDER).option(Option.DOUBLE);
        } else {
            // can't hit when you split off aces
            if(getHand().get(0).getNumber()==CardNumber.A) {
                options.removeOption(Option.HIT);
            }
        }

        // if player has pair of cards he can call for a split
        if (hand.hasPair()) {
            options.option(Option.SPLIT);
        } 

        // if the dealer first card is an A you can insure
        if (game.getDealer().getHand().get(0).getNumber() == CardNumber.A) {
            options.option(Option.INSURE);
        }

        OptionMenu menu = options.build();

        // get the player response for the menu
        MenuResponse response = menu.ask();

        // removing the options that are only allowed in the 'side rules', only after the card showup
        menu.removeOption(Option.DOUBLE);
        menu.removeOption(Option.INSURE);
        menu.removeOption(Option.SPLIT);
        // if no late surrender enabled, the player can only surrender at the begining
        if (!game.getConfig().isLateSurrenderEnabled()) {
            menu.removeOption(Option.SURRENDER);
        }

        boolean willTakeAnotherAction = false;
        ActionResponse actionResponse = null;

        // if i selected a side-rule option, i will run it before the round starts
        if (response.getResponseOption() != Option.HIT && response.getResponseOption() != Option.STAND && response.getResponseOption() != Option.DOUBLE) {
            willTakeAnotherAction = true;
            actionResponse = response.getResponseOption().run(game, this);
        }

        // if this side rule ended the game, stop the round
        if (game.getGameResult() != null) {
            return;
        }

        // check if we have a starting dealer blackjack before any actions
        if (game.getDealer().getPoints() == 21) {
            Out.print("The Dealer peeks his faced-down card. He shows a "+game.getDealer().getHand().get(1).toString()+". Its a Blackjack !");
            game.endGame(BlackjackTable.GameResult.LOOSE);
            return;
        }

        // if player have insured, its only worthy for the first round
        if (isInsured()) {
            setInsured(false);
            Out.print("The dealer does not have a Blackjack, and you lost your insurance pickles.");
            willTakeAnotherAction = false;
            response = menu.ask();
        }

        // if the player already choose something for the side-rules
        // show him the menu again
        if (willTakeAnotherAction) {
            response = menu.ask();
        }

        while (actionResponse != ActionResponse.CLOSE_MENU) {
            
            actionResponse = response.
                    getResponseOption().
                    run(game, this);
            if (actionResponse == ActionResponse.CLOSE_MENU) {
                break;
            }
            response = menu.ask();
        }
    }
    
    /**
     * Calculate the points this player have
     * @return the sum of points this player have
     */
    public int getPoints() {
        int points = getHand().stream().mapToInt(card -> card.getValue()).sum();
        // 'cheapest' way to to the ace thing :P
        if (points > 21 && getHand().hasAce()) {
            points -= 10;
        }
        return points;
    }

    public String getName() {
        return "You";
    }

    public boolean isDoubled() {
        return doubled;
    }

    public void setDoubled(boolean doubled) {
        this.doubled = doubled;
    }

    public int getCash() {
        return cash;
    }

    public boolean isInsured() {
        return insured;
    }

    public void setInsured(boolean insured) {
        this.insured = insured;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public List<Hand> getSpliHands() {
        return this.splitHands;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

}
