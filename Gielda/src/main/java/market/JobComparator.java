package market;

import java.util.Comparator;
import jobs.*;

public class JobComparator implements Comparator<Job> {
    private final boolean isBuy;

    public JobComparator(boolean isBuy2) {
        isBuy = isBuy2;
    }

    public int compare(Job A, Job B) {
        if (A.getLimit() != B.getLimit()) {
            if (isBuy) {
                return Integer.compare(B.getLimit(), A.getLimit());
            }
            return Integer.compare(A.getLimit(), B.getLimit());
        }
        return Long.compare(A.getTime(), B.getTime());
    }
}
