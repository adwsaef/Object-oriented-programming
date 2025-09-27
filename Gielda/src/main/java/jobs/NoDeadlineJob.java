package jobs;

import investors.*;

public class NoDeadlineJob extends DeadlineJob {
    // I can assume that each time limits (and round count) fits in int, so I can make bigger limit and it will work as no limit.
    public NoDeadlineJob(boolean buy2, int amount2, String id2, int limit2, Investor author2) {
        super(buy2, amount2, id2, limit2, author2, Long.MAX_VALUE);
    }
}
