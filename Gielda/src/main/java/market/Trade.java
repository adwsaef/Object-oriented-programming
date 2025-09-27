package market;

import jobs.*;

public class Trade {
    private final Job seller;
    private final Job buyer;
    private final String id;
    private final int amount;
    private final int price;

    public Trade(Job seller2, Job buyer2, String id2, int amount2, int price2) {
        seller = seller2;
        buyer = buyer2;
        id = id2;
        amount = amount2;
        price = price2;
    }

    public Job getSeller() {
        return seller;
    }

    public Job getBuyer() {
        return buyer;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public void print(){
        System.out.println("doszło do wymiany " + id);
    }
}
