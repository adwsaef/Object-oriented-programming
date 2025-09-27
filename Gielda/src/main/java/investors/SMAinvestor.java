package investors;

import java.util.*;

import jobs.*;
import market.*;

public class SMAinvestor extends Investor {
    private final int shortSMALength;
    private final int longSMALength;
    private final Map<String, Queue<Integer>> shortLastPrice;
    private final Map<String, Queue<Integer>> longLastPrice;
    private final Map<String, Integer> ShortSMA;
    private final Map<String, Integer> LongSMA;

    public SMAinvestor(int startMoney, Map<String, Integer> startActions, int shortSMALength2, int longSMALength2) {
        super(startMoney, startActions);
        shortSMALength = shortSMALength2;
        longSMALength = longSMALength2;
        shortLastPrice = new HashMap<>();
        longLastPrice = new HashMap<>();
        ShortSMA = new HashMap<>();
        LongSMA = new HashMap<>();
    }

    public void updateQueues(TradingSystem system) {
        String[] companies = system.getCompanies();
        for (String company : companies) {
            if (!shortLastPrice.containsKey(company)) shortLastPrice.put(company, new LinkedList<Integer>());

            Queue<Integer> currentShort = shortLastPrice.get(company);

            if (currentShort.size() >= shortSMALength) currentShort.remove();

            currentShort.add(system.getPreviousPrice(company));

            if (!longLastPrice.containsKey(company)) longLastPrice.put(company, new LinkedList<Integer>());

            Queue<Integer> currentLong = longLastPrice.get(company);

            if (currentLong.size() >= longSMALength) currentLong.remove();

            currentLong.add(system.getPreviousPrice(company));
        }
    }

    private static int sumQueue(Queue<Integer> queue) {
        int sum = 0;
        for (Integer value : queue) {
            sum += value;
        }
        return sum;
    }

    @Override
    public void print() {
        System.out.println("SMA");
        super.print();
    }

    @Override
    public Job performAction(TradingSystem system) {
        Random rand = new Random();
        updateQueues(system);
        Job result = null;

        for (String key : shortLastPrice.keySet()) {
            boolean no_data = !ShortSMA.containsKey(key) || !LongSMA.containsKey(key);
            int prevShort = ShortSMA.getOrDefault(key, -1);
            int prevLong = LongSMA.getOrDefault(key, -1);
            int currShort = sumQueue(shortLastPrice.get(key)) / shortSMALength;
            int currLong = sumQueue(longLastPrice.get(key)) / longSMALength;

            if (longLastPrice.get(key).size() >= longSMALength) {
                ShortSMA.put(key, currShort);
                LongSMA.put(key, currShort);
            }

            if (!no_data) {
                int limit = Integer.max(1, system.getPreviousPrice(key) -
                        StockMarket.getBalance() + rand.nextInt(StockMarket.getBalance() * 2 + 1));
                if (prevLong >= prevShort && currLong <= currShort)
                    result = new NoDeadlineJob(true, actions.get(key), key, limit, this);

                if (prevLong <= prevShort && currLong >= currShort)
                    result = new NoDeadlineJob(false, actions.get(key), key, limit, this);
            }

        }

        return result;
    }
}
