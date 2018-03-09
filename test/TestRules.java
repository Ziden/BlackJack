
import org.slomka.blackjack.BlackjackTable.GameResult;
import org.slomka.blackjack.Out;
import static org.slomka.blackjack.deck.Card.CardNumber.*;
import static org.slomka.blackjack.deck.Card.CardSuit.CLUBS;
import static org.slomka.blackjack.deck.Card.CardSuit.DIAMONDS;
import static org.slomka.blackjack.deck.Card.CardSuit.SPADES;
import org.slomka.blackjack.exceptions.CardNotFoundException;
import org.slomka.blackjack.menus.Option;
import org.slomka.blackjack.menus.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.slomka.blackjack.deck.Card;
import static org.slomka.blackjack.deck.Card.CardSuit.CLUBS;
import static org.slomka.blackjack.deck.Card.CardSuit.SPADES;
import org.slomka.blackjack.deck.Deck;
import org.slomka.blackjack.exceptions.InvalidOptionException;

/**
 *
 * @author Slomka
 */
public class TestRules extends BaseTest {

    /*
     * Tests basic points calculation
     */
    @Test
    public void basePoints() {
        table.getDeck().shuffle();
        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(Q, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(K, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N4, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N5, CLUBS));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }
        assertEquals("cards worth correct", table.getPlayer().getPoints(), 9);
        assertEquals("cards worth correct", table.getDealer().getPoints(), 20);
    }

    /*
     * Tests for the volatice ace value
     */
    @Test
    public void testAceValues() {
        table.getDeck().shuffle();
        try {
            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, CLUBS));

            assertEquals("cards worth correct", table.getPlayer().getPoints(), 11);

            table.giveCard(table.getPlayer(), table.getDeck().getCard(N5, CLUBS));

            assertEquals("cards worth correct", table.getPlayer().getPoints(), 16);

            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, SPADES));

            assertEquals("cards worth correct", table.getPlayer().getPoints(), 17);

            table.getPlayer().getHand().clear();

            table.setDeck(table.prepareDeck(3));

            try {
                table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
                table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
                table.giveCard(table.getPlayer(), table.getDeck().getCard(A, CLUBS));
                table.giveCard(table.getPlayer(), table.getDeck().getCard(A, DIAMONDS));
            } catch (CardNotFoundException e) {
                fail("Card suposed to be found " + e.getCard().toString());
            }
            assertEquals("ace value is right", table.getPlayer().getPoints(), 12);

        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }
    }

    /*
     * Tests for card type limit in the deck
     */
    @Test
    public void deckCardsTest() {

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            fail("4 cards of the same were found, however we have 3 decks only.");
        } catch (CardNotFoundException e) {
            assertEquals("there could not be found a 4th card with 3 decks only", true, true);
        }

    }

    /*
     * Check for a DRAW game
     */
    @Test
    public void checkDraw() {

        table.getDeck().shuffle();

        int initialCash = table.getPlayer().getCash();
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(Q, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(Q, SPADES));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }
        OptionBuilder.setAutorunOptions(Option.STAND);

        table.playersTurns();

        assertEquals("Player lost his bet", table.getPlayer().getCash(), initialCash);
        assertEquals("Lost the game", table.getGameResult(), GameResult.PUSH);

    }

    /*
     * Check for a WIN game
     */
    @Test
    public void checkWin() {

        table.getDealer().setIddle(true);

        int initialCash = table.getPlayer().getCash();
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(N5, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N3, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N9, SPADES));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }
        OptionBuilder.setAutorunOptions(Option.STAND);

        table.playersTurns();

        assertEquals("Player won his bet", table.getPlayer().getCash(), initialCash + bet);
        assertEquals("Won the game", table.getGameResult(), GameResult.WIN);

    }

    /*
     * Checks for a LOSS game
     */
    @Test
    public void checkLoss() {

        int initialCash = table.getPlayer().getCash();
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(N5, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N5, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N2, SPADES));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }
        OptionBuilder.setAutorunOptions(Option.STAND);

        table.playersTurns();

        assertEquals("Player lost his bet", table.getPlayer().getCash(), initialCash - bet);
        assertEquals("Lost the game", table.getGameResult(), GameResult.LOOSE);

    }

    /*
     * Tests for the Five Charlie Rule
     */
    @Test
    public void testFiveCharlie() {

        table.getDealer().setIddle(true);

        table.getConfig().setFiveCharlieEnabled(true);

        int initialCash = table.getPlayer().getCash();
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        try {

            table.giveCard(table.getDealer(), table.getDeck().getCard(N3, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N3, CLUBS));

            table.giveCard(table.getPlayer(), table.getDeck().getCard(N2, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N2, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N2, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N2, CLUBS));

        } catch (CardNotFoundException e) {

            fail("Card suposed to be found " + e.getCard().toString());

        }

        OptionBuilder.setAutorunOptions(Option.HIT, Option.STAND);

        table.playersTurns();

        assertEquals("Player win his bet", table.getPlayer().getCash(), initialCash + bet + bet);
        assertEquals("Lost the game", table.getGameResult(), GameResult.WIN);

    }

    /*
     * Tests for the Swedish Pub rule
     */
    @Test
    public void testSwedishPub() {

        table.getDealer().setIddle(true);
        table.getConfig().setSwedishPub(true);

        try {
            
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N8, CLUBS));

            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N8, CLUBS));

            OptionBuilder.setAutorunOptions(Option.STAND);

            table.playersTurns();

            Out.print("TABLE RESULT " + table.getGameResult());

            assertEquals("Swedish pub looses if score is same between 17 - 19", GameResult.LOOSE, table.getGameResult());

        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

        this.setUp();
        table.getDealer().setIddle(true);

        table.getConfig().setSwedishPub(false);

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N8, CLUBS));

            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N8, CLUBS));

            OptionBuilder.setAutorunOptions(Option.STAND);

            table.playersTurns();

            assertEquals("Swedish pub looses if score is same between 17 - 19", table.getGameResult(), GameResult.PUSH);

        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

    }

    /*
     * Tests if insurance options shows up when a Ace is revealed from the dealer.
     */
    @Test
    public void testInsuranceShouldShow() {

        table.getDeck().shuffle();

        int initialCash = table.getPlayer().getCash();
        table.setAutoloop(false);
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N3, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N6, SPADES));

        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

        Out.print("DEALER POINTS : " + table.getDealer().getPoints());

        OptionBuilder.setAutorunOptions(Option.INSURE, Option.STAND);

        try {
            table.playersTurns();
            assert (true);
        } catch (InvalidOptionException e) {
            e.printStackTrace();
            fail("Player could not choose INSURE when dealer open hand did have an ACE");
        }

    }

    /*
     * Tests if insurance is not showing if dealer has no ace
     */
    @Test
    public void testInsuranceShouldNotShow() {

        int initialCash = table.getPlayer().getCash();

        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(N3, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N3, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N6, SPADES));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

        Out.print("DEALER POINTS : " + table.getDealer().getPoints());

        OptionBuilder.setAutorunOptions(Option.INSURE, Option.STAND);

        try {
            table.playersTurns();
            fail("Player could choose INSURE when dealer open hand did not have an ACE");
        } catch (InvalidOptionException e) {
            assert (true);
        }
    }

    /*
     * Tests if surrender rule make you able to select surrender after a hit
     */
    @Test
    public void checkSurrenderRule() {

        table.getConfig().setLateSurrenderEnabled(true);

        table.getDealer().getHand().clear();
        
        table.getDealer().setIddle(true);
        
        Deck deck = new Deck();
        deck.addCards(new Card[]{new Card(N2, CLUBS), new Card(N2, CLUBS), new Card(N2, CLUBS), new Card(N2, CLUBS)});
        table.setDeck(deck);

        table.giveCard(table.getDealer(), new Card(N8, CLUBS));
        table.giveCard(table.getDealer(), new Card(N8, CLUBS));

        table.giveCard(table.getPlayer(), new Card(N3, SPADES));
        table.giveCard(table.getPlayer(), new Card(N3, SPADES));

        OptionBuilder.setAutorunOptions(Option.HIT, Option.SURRENDER);

        try {
            table.playersTurns();
            assert (true);
        } catch (InvalidOptionException e) {
            fail("Player should be able to surrender after he hits if late surrender rule is on");
        }

    }
    
    
    /*
     * Tests if surrender rule make you are not able to select surrender after a hit
     */
    @Test
    public void checkNotSurrenderRule() {

        table.getDealer().setIddle(true);
        
        table.getConfig().setLateSurrenderEnabled(false);

        table.getDealer().getHand().clear();
        
        Deck deck = new Deck();
        deck.addCards(new Card[]{new Card(N2, CLUBS), new Card(N2, CLUBS), new Card(N2, CLUBS), new Card(N2, CLUBS)});
        table.setDeck(deck);

        table.giveCard(table.getDealer(), new Card(N8, CLUBS));
        table.giveCard(table.getDealer(), new Card(N8, CLUBS));

        table.giveCard(table.getPlayer(), new Card(N3, SPADES));
        table.giveCard(table.getPlayer(), new Card(N3, SPADES));

        OptionBuilder.setAutorunOptions(Option.HIT, Option.SURRENDER);

        try {
            table.playersTurns();
            fail("Player should not be able to surrender after he hits if late surrender rule is on");
        } catch (InvalidOptionException e) {
            assert (true);
            
        }

    }

}
