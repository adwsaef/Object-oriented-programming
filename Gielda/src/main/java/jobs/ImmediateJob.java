package jobs;

import investors.*;
import market.*;

public class ImmediateJob extends DeadlineJob {
    public ImmediateJob(boolean buy2, int amount2, String id2, int limit2, Investor author2, TradingSystem s) {
        super(buy2, amount2, id2, limit2, author2, (long) s.getRound());
    }
}
