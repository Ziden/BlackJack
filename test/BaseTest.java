

import org.slomka.blackjack.BlackjackTable;
import org.junit.Before;
import org.slomka.blackjack.Out;

/**
 *
 * @author Slomka
 */
public class BaseTest {

    protected BlackjackTable table;

    @Before
    public void setUp() {
        table = new BlackjackTable(3);
        table.getPlayer().getHand().clear();
        table.getDealer().getHand().clear();
        table.setAutoloop(false);
        table.getPlayer().setCash(500);
    }
    
}
