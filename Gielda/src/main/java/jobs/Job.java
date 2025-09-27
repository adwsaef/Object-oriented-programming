package jobs;

import java.util.Objects;
import investors.*;
import market.*;

abstract public class Job {
    protected final boolean buy;
    protected final String id;
    protected final int limit;
    protected int amount;
    protected Investor author;
    protected Long time;

    public Job(boolean buy2, int amount2, String id2, int limit2, Investor author2) {
        buy = buy2;
        amount = amount2;
        limit = limit2;
        id = id2;
        author = author2;
        time = 0L;
    }

    public boolean isBuy() {
        return buy;
    }

    public String getId() {
        return id;
    }

    public int getLimit() {
        return limit;
    }

    public int getAmount() {
        return amount;
    }

    public Investor getInvestor() {
        return author;
    }

    public void setTime(Long val) {
        time = val;
    }

    public Long getTime() {
        return time;
    }

    public void print() {
        if (buy)
            System.out.println("zlecenie kupienia");
        if (!buy)
            System.out.println("zlecenie sprzedaży");
        System.out.println("id firmy " + id + " limit ceny " + limit);
        System.out.println("liczba akcji do sprzedaży/kupienia: " + amount);
    }

    public abstract boolean correctState();

    public abstract boolean deleteState(StockMarket s);

    public static Trade performTrade(Job a, Job b) {
        // Check if one users sells, other buys. It is about same product.
        if (a.isBuy() == b.isBuy())
            return null;
        if (!Objects.equals(a.getId(), b.getId()))
            return null;
        if (a.isBuy()) {
            Job tmp = b;
            b = a;
            a = tmp;
        }
        // a sells, b buys - check if there is price agreement.
        int price = a.getLimit();
        if (b.getTime() < a.getTime())
            price = b.getLimit();

        if (a.getLimit() > price)
            return null;
        if (b.getLimit() < price)
            return null;

        int amount = Math.min(a.getAmount(), b.getAmount());

        // Decide about amount, check if investors have resource.
        if (a.getInvestor().getActions(a.getId()) < amount)
            return null;

        if (b.getInvestor().getMoney() < amount * price)
            return null;

        Trade res = new Trade(a, b, a.getId(), amount, price);

        a.getInvestor().performTrade(res, true);
        b.getInvestor().performTrade(res, false);

        return res;
    }

    public static void rollBack(Trade t) {
        if (t == null)
            return;
        t.getSeller().getInvestor().performTrade(t, false);
        t.getBuyer().getInvestor().performTrade(t, true);
    }
}
