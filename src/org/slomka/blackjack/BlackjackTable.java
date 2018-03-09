package org.slomka.blackjack;

import org.slomka.blackjack.menus.OptionMenu;
import org.slomka.blackjack.deck.Card;
import org.slomka.blackjack.deck.Deck;
import org.slomka.blackjack.menus.MenuResponse;
import org.slomka.blackjack.menus.Option;
import org.slomka.blackjack.menus.OptionBuilder;

/**
 * *
 *
 * @author Slomka
 */
public class BlackjackTable {

    private Player player;
    private Dealer dealer;
    private Deck deck;
    private Card faceDownCard;
    private Config config;
    private boolean autoloop;
    private GameResult gameResult = null;

    public enum GameResult {

        WIN, LOOSE, PUSH, BJWIN;
    }

    /**
     * *
     * Creates the table , the deck, player and dealer
     *
     * @param numberOfDecks the number of decks that will be used in this table
     */
    public BlackjackTable(int numberOfDecks) {
        config = new Config();
        this.deck = prepareDeck(numberOfDecks);
        player = new Player();
        player.setCash(config.getStartCash());
        dealer = new Dealer();
        Out.print("--------- new game ----------");
        Out.print("The game table has been set up, with " + numberOfDecks + " decks and a total of " + this.deck.getCards().length + " cards.");
        this.deck.shuffle();
    }

    /**
     * *
     * Create 1 deck from a group of N decks
     *
     * @param numberOfDecks the number of inner decks that will have this deck
     * @return a deck with all inner decks inside
     */
    public Deck prepareDeck(int numberOfDecks) {
        Deck mainDeck = new Deck();
        for (int n = 0; n < numberOfDecks - 1; n++) {
            Deck deck = new Deck();
            mainDeck.addCards(deck.getCards());
        }
        return mainDeck;
    }

    /**
     * Setup the table basics for a new game
     */
    public void setupTable() {
        player.getHand().setBet(0);
        player.setDoubled(false);
        player.setInsured(false);
        player.getHand().clear();
        dealer.getHand().clear();
        player.getSpliHands().clear();
        gameResult = null;

        if (player.getCash() < config.getMinBet()) {
            Out.print("Oh well, looks like you are out of pickles...");
            Out.print("I only play against people that have pickles. Be gone, you... pickleless.");
            Out.print("* you were kicked out of the table *");
            System.exit(1);
        }

        if (deck.getCards().length < 30) {
            Out.print("As the cards on the deck were finishing, the Dealer got all the cards back");
            this.deck = prepareDeck(3);
            this.deck.shuffle();
        }

    }

    /**
     * Deal the starting cards
     */
    public void dealCards() {
        dealAndConsumeCard(dealer);
        this.faceDownCard = deck.popCard();
        dealer.getHand().add(faceDownCard);
        Out.print("The Dealer has placed a face-down card");
        dealAndConsumeCard(player);
        dealAndConsumeCard(player);
        Out.print("You have " + player.getPoints() + " points");
    }

    /**
     * Base game order
     */
    public void doRound() {

        setupTable();

        takeBet(player);

        dealCards();

        playersTurns();

    }

    /**
     * Do the players turns. Since this is a 2 player hardcoded, just take theyr
     * turns and check who has more points.
     */
    public void playersTurns() {

        player.takeTurn(this);

        dealer.takeTurn(this);

        checkWinner();
    }

    /**
     * Check who have more points and calls it a game.
     * Busting and Blackjacking are done before this method.
     */
    public void checkWinner() {
        
        int dealerPoints = dealer.getPoints();
        int playerPoints = player.getPoints();

        if (playerPoints > dealerPoints) {
            endGame(BlackjackTable.GameResult.WIN);
        } else if (dealerPoints > playerPoints) {
            endGame(BlackjackTable.GameResult.LOOSE);
        } else {
            endGame(BlackjackTable.GameResult.PUSH);
        }
    }

    /**
     * *
     * When game is over.
     *
     * @param result , the game's result of course.
     */
    public void endGame(GameResult result) {

        // if the game is over, the game cannot get over again
        if (this.gameResult != null) {
            return;
        }

        // print the final points
        printPoints();

        Out.print("Game Over ! " + result.name());
        this.gameResult = result;

        /////////
        // WIN //
        /////////
        if (result == GameResult.WIN || result == GameResult.BJWIN) {
            // first we cash back the bet 
            player.setCash(player.getCash() + player.getHand().getBet());
            // then we pay the winning money
            if (result == GameResult.BJWIN) {
                // on a BJ win i get 3:2 win
                player.getHand().setBet((int) (player.getHand().getBet() * 1.5d));
            }
            int win = player.getHand().getBet();
            player.setCash(player.getCash() + win);
            Out.print("You won " + win + " pickles, yay ! Now you have " + player.getCash() + " pickles");

            ///////////
            // LOOSE //
            ///////////
        } else if (result == GameResult.LOOSE) {

            if (!player.isInsured()) {
                Out.print("You lost your " + player.getHand().getBet() + " bet, keeping " + player.getCash() + " pickles!");
            } else {
                // when i loose, if i'm insured, ill get my bet back 
                player.setCash(player.getCash() + player.getHand().getBet());
                Out.print("Since you were insured against the blackjack, you only lost the insurance cost, having still " + player.getCash() + " pickles.");

            }

            //////////
            // DRAW //
            //////////
        } else if (result == GameResult.PUSH) {

            if (config.isSwedishPub()) {
                if (player.getPoints() >= 17 && player.getPoints() <= 19) {
                    Out.print("A 'Push' (Draw) with points of 17, 18 or 19 is a swedish loss.");
                    Out.print("You lost your " + player.getHand().getBet() + " bet, keeping " + player.getCash() + " pickles!");
                    result = GameResult.LOOSE;
                    this.gameResult = result;
                } else {
                    player.setCash(player.getCash() + player.getHand().getBet());
                    Out.print("A 'Push' (Draw), you get your " + player.getHand().getBet() + " back, keeping " + player.getCash() + " pickles.");
                }
            } else {
                player.setCash(player.getCash() + player.getHand().getBet());
                Out.print("A 'Push' (Draw), you get your " + player.getHand().getBet() + " back, keeping " + player.getCash() + " pickles.");
            }

        }

        // player can still play for its split hands
        if (player.getSpliHands().size() > 0) {

            Hand hand = player.getSpliHands().remove(0);
            player.setHand(hand);
            Out.print("Now playng the split hand of " + hand.toString());
            this.gameResult = null;
            this.playersTurns();

        }

        if (autoloop) {
            askForMore();
        }
    }

    /**
     * *
     * Opens up a menu for the player to place his bet
     *
     * @param player
     */
    private void takeBet(Player player) {
        Out.print("You have " + player.getCash() + " pickles to bet.");
        OptionMenu betMenu = new OptionBuilder().message("Enter the amount of pickles you want to bet.").build();
        MenuResponse response = betMenu.ask();
        int bet = response.getResponseNumber();
        boolean read = true;
        while (read) {
            bet = response.getResponseNumber();
            if (bet > player.getCash()) {
                Out.print("You dont have this much pickles. You wish you had.");
                response = betMenu.ask();
            } else if (bet % 2 == 1) {
                Out.print("You need to bet with even numbers.");
                response = betMenu.ask();
            } else if (bet == 0) {
                Out.print("You can't bet 0 pickles. The Dealer wants pickles.");
                response = betMenu.ask();
            } else if (bet > config.getMaxBet()) {
                Out.print("You can't bet more then " + config.getMaxBet() + " pickles.");
                response = betMenu.ask();
            } else if (bet < config.getMinBet()) {
                Out.print("You can't bet less then " + config.getMinBet() + " pickles.");
                response = betMenu.ask();
            } else {
                read = false;
            }
        }
        doBet(player, bet);
    }

    /**
     * *
     * Sets the player bet
     *
     * @param player the player that is betting
     * @param howMuch the amount of the bet
     */
    public void doBet(Player player, int howMuch) {
        player.setCash(player.getCash() - howMuch);
        player.getHand().setBet(howMuch);
        Out.print(player.getName() + " bet " + howMuch + " pickles, now " + player.getName() + " only have " + player.getCash() + " pickles left.");
    }

    /**
     * *
     * Deals a random card for that player and consumes that card
     *
     * @param who that will recieve the card
     */
    public void dealAndConsumeCard(Player who) {
        Card picked = deck.popCard();
        giveCard(who, picked, who.getHand());
    }

    /**
     * *
     * Deals a random card for that player and consumes that card
     *
     * @param who that will recieve the card
     */
    public void dealAndConsumeCard(Player who, Hand targetHand) {
        Card picked = deck.popCard();
        giveCard(who, picked, targetHand);
    }

    /**
     * *
     * Gives a player a specific card. Will not consume the card.
     *
     * @param who who will recieve the card
     * @param c the specific card
     */
    public void giveCard(Player who, Card c, Hand hand) {
        hand.add(c);
        String h = "";
        if (who.getHand() != hand) {
            h = "r splited hand";
        }
        Out.print("The Dealer gave " + who.getName() + "" + h + " a " + c.toString());
    }

    /**
     * *
     * Gives a player a specific card. Will not consume the card.
     *
     * @param who who will recieve the card
     * @param c the specific card
     */
    public void giveCard(Player who, Card c) {
        giveCard(who, c, who.getHand());
    }

    /**
     * *
     * Asks for one more round.
     */
    private void askForMore() {
        MenuResponse response = new OptionBuilder().message("Want to play more ? You have " + player.getCash() + " pickles!").option(Option.PLAY).option(Option.QUIT).build().ask();
        if (response.getResponseOption() == Option.PLAY) {
            doRound();
        } else {
            System.exit(1);
        }
    }

    /**
     * *
     * Prints the current points.
     */
    public void printPoints() {
        if (player.getPoints() < 21) {
            Out.print("You have " + player.getPoints() + " points");
        } else if (player.getPoints() == 21) {
            Out.print("You have a BLACKJACK!");
        }
        if (dealer.getPoints() < 21) {
            Out.print("The dealer have " + dealer.getPoints() + " points");
        } else if (dealer.getPoints() == 21) {
            Out.print("The Dealer have a BLACKJACK!");
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Card getFaceDownCard() {
        return this.faceDownCard;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setAutoloop(boolean a) {
        this.autoloop = a;
    }

    public Config getConfig() {
        return config;
    }

}
