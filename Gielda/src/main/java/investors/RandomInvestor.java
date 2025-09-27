package investors;

import java.util.*;
import jobs.*;
import market.*;

public class RandomInvestor extends Investor {
    // Patience means how big deadline for jobs can be.
    private static final Long patience = 10L;
    private static final int possibleDecisions = 4;

    public RandomInvestor(int startMoney, Map<String, Integer> startActions) {
        super(startMoney, startActions);
    }

    @Override
    public void print(){
        System.out.println("Losowy");
        super.print();
    }

    @Override
    public Job performAction(TradingSystem system) {
        Random rand = new Random();
        // Choose which action to sell/buy (add extra one which means none).
        int option = rand.nextInt(actions.size()+1);
        if (option == actions.size())
            return null;

        // Get company's name.
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(actions.entrySet());
        String company = entryList.get(option).getKey();

        // Decide price.
        int askedPrice = system.getPreviousPrice(company) - StockMarket.getBalance()
                + rand.nextInt(2 * StockMarket.getBalance() + 1);
        if (askedPrice <= 0)
            askedPrice = 1;

        // Decide buy or sell and amount.
        int actionsCnt;
        boolean buy;
        if (rand.nextInt(2) == 0) {
            buy = true;
            actionsCnt = rand.nextInt(money / askedPrice + 1);
        } else {
            buy = false;
            actionsCnt = rand.nextInt(entryList.get(option).getValue()+1);
        }

        // Decide queryType - one of them is no Job.
        int queryType = rand.nextInt(possibleDecisions + 1);

        if (actionsCnt == 0)
            return null;

        if (queryType == 1)
            return new ImmediateJob(buy, actionsCnt, company, askedPrice, this, system);

        if (queryType == 2)
            return new DeadlineJob(buy, actionsCnt, company, askedPrice, this,
                    system.getRound() + (rand.nextLong() % patience));

        if (queryType == 3)
            return new ExecOrCancelJob(buy, actionsCnt, company, askedPrice, this, system);

        return new NoDeadlineJob(buy, actionsCnt, company, askedPrice, this);
    }
}
