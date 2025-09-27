package investors;

import java.util.*;
import market.*;
import jobs.*;

public abstract class Investor {
    protected int money;
    protected Map<String, Integer> actions;

    public Investor(int startMoney, Map<String, Integer> startActions) {
        money = startMoney;
        actions = new HashMap<>();
        actions.putAll(startActions);
    }

    public int getMoney() {
        return money;
    }

    public int getActions(String c) {
        return actions.getOrDefault(c, 0);
    }

    public Map<String, Integer> getAllActions(){
        return actions;
    }

    public void performTrade(Trade t, boolean seller) {
        if (seller) {
            money += t.getAmount() * t.getPrice();
            actions.put(t.getId(), actions.get(t.getId()) - t.getAmount());
        } else {
            money -= t.getAmount() * t.getPrice();
            actions.put(t.getId(), actions.get(t.getId()) + t.getAmount());
        }
    }

    public void print() {
        System.out.println("stan pieniędzy inwestora: " + money);
        System.out.println("posiadane akcje:");
        for (Map.Entry<String, Integer> entry : actions.entrySet()) {
            System.out.print(entry.getKey() + ":" + entry.getValue() + " ");
        }
        System.out.println();
        System.out.println();
    }

    public abstract Job performAction(TradingSystem system);
}
