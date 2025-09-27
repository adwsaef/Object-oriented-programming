package investors;

import java.util.*;
import jobs.*;
import market.*;

public class GreedyInvestor extends Investor {
    // GreedyInvestor is my new type of investor. Every round he makes ImmediateJob with the action, which previous priced
    // changed the most. If it increased he sells, otherwise buys.
    private Map<String, Integer> previousPrices;

    public GreedyInvestor(int startMoney, Map<String, Integer> startActions) {
        super(startMoney, startActions);
        previousPrices = new HashMap<>();
    }

    @Override
    public void print() {
        System.out.println("Zachłanny");
        super.print();
    }

    @Override
    public Job performAction(TradingSystem system) {
        Random rand = new Random();
        Map<String, Integer> currentPrices = new HashMap<>();
        int difference = 0;

        String[] companies = system.getCompanies();
        for (String id : companies) {
            currentPrices.put(id, system.getPreviousPrice(id));
            difference = Integer.max(difference, Math.abs(currentPrices.get(id) - previousPrices.getOrDefault(id, 0)));
        }
        Job res = null;
        for (String id : companies) {
            int currentDifference = currentPrices.get(id) - previousPrices.getOrDefault(id, 0);
            if (Math.abs(currentDifference) == difference) {
                // Decide new price - it must differ by at most getBalance.
                int limit = Integer.max(1, currentPrices.get(id) -
                        StockMarket.getBalance() + rand.nextInt(StockMarket.getBalance() * 2) + 1);
                if (currentDifference > 0) {
                    res = new NoDeadlineJob(false, actions.getOrDefault(id, 0), id, limit, this);
                } else {
                    int canBuy = Integer.max(1, money / limit);
                    res = new NoDeadlineJob(true, canBuy, id, limit, this);
                }
            }
        }

        previousPrices = currentPrices;
        return res;
    }
}
