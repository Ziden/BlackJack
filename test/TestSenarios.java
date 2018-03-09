
import java.util.Arrays;
import java.util.stream.Stream;
import org.slomka.blackjack.BlackjackTable.GameResult;
import org.slomka.blackjack.Out;
import static org.slomka.blackjack.deck.Card.CardNumber.*;
import static org.slomka.blackjack.deck.Card.CardSuit.CLUBS;
import static org.slomka.blackjack.deck.Card.CardSuit.SPADES;
import org.slomka.blackjack.exceptions.CardNotFoundException;
import org.slomka.blackjack.menus.Option;
import org.slomka.blackjack.menus.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.slomka.blackjack.deck.Card;
import static org.slomka.blackjack.deck.Card.CardSuit.DIAMONDS;
import org.slomka.blackjack.deck.Deck;
import org.slomka.blackjack.exceptions.InvalidOptionException;

/**
 *
 * @author Slomka
 */
public class TestSenarios extends BaseTest {

    /**
     * Tests the use of insurance against a opening BJ hand
     */
    @Test
    public void checkUseInsurance() {

        table.getDeck().shuffle();

        int initialCash = table.getPlayer().getCash();
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        try {
            table.getDealer().getHand().add(table.getDeck().getCard(A, CLUBS));
            table.getDealer().getHand().add(table.getDeck().getCard(Q, CLUBS));
            table.getPlayer().getHand().add(table.getDeck().getCard(N2, CLUBS));
            table.getPlayer().getHand().add(table.getDeck().getCard(N3, CLUBS));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

        OptionBuilder.setAutorunOptions(Option.INSURE, Option.STAND);

        table.playersTurns();

        assertEquals("Player saved 50% of his bet", table.getPlayer().getCash(), initialCash - bet / 2);

    }

    /*
     * Tests the same loss against a BJ with no insurance
     */
    @Test
    public void checkLooseNotInsured() {

        table.getDeck().shuffle();

        int initialCash = table.getPlayer().getCash();
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();
        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(Q, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N2, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N3, CLUBS));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }
        OptionBuilder.setAutorunOptions(Option.STAND);

        table.playersTurns();

        assertEquals("Player lost his bet", table.getPlayer().getCash(), initialCash - bet);
        assertEquals("Lost the game", table.getGameResult(), GameResult.LOOSE);

    }

    /*
     * Tests for a double win
     */
    @Test
    public void checkDouble() {

        table.getDealer().setIddle(true);

        int initialCash = table.getPlayer().getCash();

        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(N2, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N2, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N3, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N3, SPADES));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

        OptionBuilder.setAutorunOptions(Option.DOUBLE, Option.STAND);

        table.playersTurns();

        int doubleBJ = (int) ((bet * 2));
        int shouldHave = initialCash + doubleBJ;

        Out.print("BET = " + initialCash + " " + doubleBJ);

        assertEquals("Player lost his bet", table.getPlayer().getCash(), shouldHave);
    }

    /*
     * Tests for a double bust
     */
    @Test
    public void checkDoubleBust() {

        table.getDealer().setIddle(true);

        int initialCash = table.getPlayer().getCash();

        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        table.setDeck(table.prepareDeck(3));

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(N2, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N2, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N10, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N10, SPADES));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

        OptionBuilder.setAutorunOptions(Option.DOUBLE, Option.STAND);

        table.playersTurns();

        int doubleBJ = (int) ((bet * 2));
        int shouldHave = initialCash - doubleBJ;

        assertEquals("Player lost to a bust doubled", table.getPlayer().getCash(), shouldHave);
    }

    /*
     * Tests for a surrender retuning 50% of the bet
     */
    @Test
    public void testSurrender() {

        table.getDeck().shuffle();

        int initialCash = table.getPlayer().getCash();
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();
        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(A, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(Q, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N2, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(N3, CLUBS));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }
        OptionBuilder.setAutorunOptions(Option.SURRENDER);

        table.playersTurns();

        assertEquals("Player lost his bet", table.getPlayer().getCash(), initialCash - (bet / 2));
        assertEquals("Lost the game", table.getGameResult(), GameResult.LOOSE);

    }

    /*
     * Tests for a BJ win 
     */
    @Test
    public void checkBJWin() {

        int initialCash = table.getPlayer().getCash();

        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        try {
            table.giveCard(table.getDealer(), table.getDeck().getCard(N2, CLUBS));
            table.giveCard(table.getDealer(), table.getDeck().getCard(N3, CLUBS));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(A, SPADES));
            table.giveCard(table.getPlayer(), table.getDeck().getCard(Q, SPADES));
        } catch (CardNotFoundException e) {
            fail("Card suposed to be found " + e.getCard().toString());
        }

        OptionBuilder.setAutorunOptions(Option.STAND);

        table.playersTurns();

        int doubleBJ = (int) (bet * 1.5d);
        int shouldHave = initialCash + doubleBJ;

        Out.print("BET = " + initialCash + " " + doubleBJ);

        assertEquals("Player lost his bet", table.getPlayer().getCash(), shouldHave);

    }

    /*
     * Tests for Splits wins
     */
    @Test
    public void checkSplit() {

        int initialCash = table.getPlayer().getCash();

        table.getDealer().setIddle(true);

        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        // fill deck with 5 DIAMOND
        Arrays.asList(table.getDeck().getCards()).replaceAll(x -> new Card(N7, DIAMONDS));

        table.giveCard(table.getDealer(), new Card(N2, CLUBS));
        table.giveCard(table.getDealer(), new Card(N3, CLUBS));

        table.giveCard(table.getPlayer(), new Card(N7, SPADES));
        table.giveCard(table.getPlayer(), new Card(N7, SPADES));

        OptionBuilder.setAutorunOptions(Option.SPLIT, Option.STAND, Option.SPLIT, Option.STAND, Option.HIT, Option.STAND);

        table.playersTurns();

        assertEquals("split 3 times , win 3 times", 500 + 300, table.getPlayer().getCash());
    }

    /*
     * Tests for Splits mix, split 3 times, win 1 loose 2
     */
    @Test
    public void checkSplitMix() {

        int initialCash = table.getPlayer().getCash();

        table.getDealer().setIddle(true);

        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        // fill deck with 5 DIAMOND
        Arrays.asList(table.getDeck().getCards()).replaceAll(x -> new Card(N7, DIAMONDS));

        table.giveCard(table.getDealer(), new Card(N8, CLUBS));
        table.giveCard(table.getDealer(), new Card(N8, CLUBS));

        table.giveCard(table.getPlayer(), new Card(N7, SPADES));
        table.giveCard(table.getPlayer(), new Card(N7, SPADES));

        OptionBuilder.setAutorunOptions(Option.SPLIT, Option.STAND, Option.SPLIT, Option.STAND, Option.HIT, Option.STAND);

        table.playersTurns();

        assertEquals("split 3 times , win 3 times", 400, table.getPlayer().getCash());
    }

    /*
     * Tests for splitting aces
     */
    @Test
    public void checkAcesSplit() {

        int initialCash = table.getPlayer().getCash();

        table.getDealer().setIddle(true);

        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        // fill deck with 5 DIAMOND
        Arrays.asList(table.getDeck().getCards()).replaceAll(x -> new Card(N2, DIAMONDS));

        table.giveCard(table.getDealer(), new Card(N8, CLUBS));
        table.giveCard(table.getDealer(), new Card(N8, CLUBS));

        table.giveCard(table.getPlayer(), new Card(A, SPADES));
        table.giveCard(table.getPlayer(), new Card(A, SPADES));

        OptionBuilder.setAutorunOptions(Option.SPLIT, Option.STAND, Option.HIT, Option.STAND);

        try {
            table.playersTurns();
            fail("Player should not suposed to hit after he splitted a pair of aces");
        } catch (InvalidOptionException e) {
            if (e.getOption() == Option.HIT) {
                assert (true);
            } else {
                fail("Player should not suposed to hit after he splitted a pair of aces");
            }
        }
    }

    /*
     * Tests split dont recieves BJ bonus
     */
    @Test
    public void checkSplitRecievesBJBonus() {
        int initialCash = table.getPlayer().getCash();
        table.getDealer().setIddle(true);
        table.doBet(table.getPlayer(), 100);
        int bet = table.getPlayer().getHand().getBet();

        table.getDealer().getHand().clear();

        // fill deck with 5 DIAMOND
        Deck deck = new Deck();
        deck.addCards(new Card[]{new Card(A, CLUBS),new Card(A, CLUBS),new Card(A, CLUBS),new Card(A, CLUBS)});
        table.setDeck(deck);
        
        table.giveCard(table.getDealer(), new Card(N8, CLUBS));
        table.giveCard(table.getDealer(), new Card(N8, CLUBS));

        table.giveCard(table.getPlayer(), new Card(K, SPADES));
        table.giveCard(table.getPlayer(), new Card(K, SPADES));

        OptionBuilder.setAutorunOptions(Option.SPLIT, Option.STAND, Option.STAND);

        table.playersTurns();

        assertEquals("split 3 times , win 3 times", 500 + (bet * 2), table.getPlayer().getCash());
    }

}
