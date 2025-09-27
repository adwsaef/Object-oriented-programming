package jobs;

import investors.*;
import market.*;

public class ExecOrCancelJob extends ImmediateJob {
    private final int startAmount;

    public ExecOrCancelJob(boolean buy2, int amount2, String id2, int limit2, Investor author2, TradingSystem s) {
        super(buy2, amount2, id2, limit2, author2, s);
        startAmount = amount2;
    }

    public boolean correctState() {

        return amount != 0 && amount != startAmount;
    }
}
