package jobs;

import investors.*;
import market.*;

public class DeadlineJob extends Job {
    private final long deadline;

    public DeadlineJob(boolean buy2, int amount2, String id2, int limit2, Investor author2, Long deadline2) {
        super(buy2, amount2, id2, limit2, author2);
        deadline = deadline2;
    }

    public boolean correctState() {
        return false;
    }

    public boolean deleteState(StockMarket s) {
        // Check if deadline is fullfilled, cannot be fulfilled or is to be deleted.
        if (s.getRound() == deadline || amount == 0) return true;
        if (buy) {
            return author.getMoney() < limit * amount;
        }else{
            return author.getActions(id) < amount;
        }
    }
}
