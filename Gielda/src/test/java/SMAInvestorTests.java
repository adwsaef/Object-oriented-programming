import investors.Investor;
import investors.SMAinvestor;
import market.Company;
import market.StockMarket;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class SMAInvestorTests {

    void performTest (int startMoney, int shortSMA, int longSMA){
        Map<String, Integer> startActions = new HashMap<>();
        startActions.put("AAA", 1000);

        Investor[] SMA = new Investor[1];
        SMA[0] = new SMAinvestor(startMoney, startActions, shortSMA, longSMA);

        Company[] comp = new Company[1];
        comp[0]= new Company("AAA", 10);

        StockMarket s = new StockMarket(SMA,comp);

        for (int i=0; i<longSMA; ++i){
            // SMA assumes one performAction per round.
            assertNull(SMA[0].performAction(s));
        }
    }

    @Test
    public void testActions() {
        performTest(1,1, 5);
        performTest(1, 3, 10);
        performTest(Integer.MAX_VALUE,1, 2);
        performTest(Integer.MAX_VALUE, 10000, 10000 + 1);

    }
}
