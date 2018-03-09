import org.slomka.blackjack.Out;
import org.slomka.blackjack.deck.Card;
import static org.slomka.blackjack.deck.Card.CardNumber.*;
import static org.slomka.blackjack.deck.Card.CardSuit.CLUBS;
import org.slomka.blackjack.exceptions.CardNotFoundException;
import org.slomka.blackjack.menus.Option;
import org.slomka.blackjack.menus.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author Slomka
 */
public class TestObjects extends BaseTest {

    /*
    * Tests if cards are being remove from the deck when using them
    */
    @Test
    public void testDeckConsuming() {
        table.getDeck().shuffle();
        assertEquals("full deck", table.getDeck().getCards().length, 52 * 3);

        Out.print("DECK SIZE: " + table.getDeck().getCards().length);

        try {
            Card takeOffACard = table.getDeck().getCard(A, CLUBS);

            Out.print("DECK SIZE: " + table.getDeck().getCards().length);

            assertEquals("full deck", table.getDeck().getCards().length, (52 * 3) - 1);

            table.dealAndConsumeCard(table.getDealer());

            assertEquals("took one card out", table.getDeck().getCards().length, 52 * 3 - 2);

            table.dealAndConsumeCard(table.getDealer());

            assertEquals("took another card out", table.getDeck().getCards().length, 52 * 3 - 3);

        } catch (CardNotFoundException ex) {
            fail("The deck should have the card.");
        }
    }

    /*
    * Tests if decks are getting empty, and if so, if the table object is 
    * re-constructing the deck
    */
    @Test
    public void testDeckOverrun() {
        
        table.getDeck().shuffle();
        
        assertEquals("full deck", table.getDeck().getCards().length, 52 * 3);

        for (int x = 0; x < 52 * 3 - 20; x++) {
            table.dealAndConsumeCard(table.getDealer());
        }

        table.dealAndConsumeCard(table.getPlayer());
        
        table.dealAndConsumeCard(table.getPlayer());

        OptionBuilder.setAutorunOptions(Option.STAND);

        Out.print("Cards remaining: " + table.getDeck().getCards().length);

        assertEquals("took another card out", table.getDeck().getCards().length, 18);

        table.setupTable();

        // dealer got the cards back ? cause cards were almost over...
        assertEquals("took another card out", table.getDeck().getCards().length, 52 * 3);

    }

}
