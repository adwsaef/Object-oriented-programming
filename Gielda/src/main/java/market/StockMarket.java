package market;

import java.util.*;
import investors.*;
import jobs.*;

public class StockMarket extends TradingSystem {
    private Long time = 0L;
    private static final int balance = 10;
    private final Vector <Trade> tradesPerformed;

    public static int getBalance() {
        return balance;
    }

    private void investorActions() {
        Collections.shuffle(users);
        for (Investor user : users) {
            Job tmp = user.performAction(this);
            ++time;
            if (tmp == null)
                continue;
            tmp.setTime(time);

            if (tmp.isBuy())
                buyJobs.add(tmp);
            if (!tmp.isBuy())
                sellJobs.add(tmp);
        }
    }

    public StockMarket(Investor[] users2, Company[] companies2) {
        super(users2, companies2);
        tradesPerformed=new Vector<>();
    }

    public void nextRound() {
        ++round;
        investorActions();
        performTrades();
        clearJobs();
    }

    private boolean exhaustJob(Vector<Job> jobs, int index) {
        Vector<Trade> history = new Vector<>();

        for (Job job : jobs) {
            history.add(Job.performTrade(job, jobs.get(index)));
            tradesPerformed.add(history.lastElement());
        }

        boolean correct = true;
        if (jobs.get(index).correctState()) {
            correct = false;
        } else {
            for (int i = 0; i < jobs.size(); ++i) {
                if (i != index) {
                    if (jobs.get(i).correctState()) {
                        if (exhaustJob(jobs, i)) {
                            correct = false;
                            break;
                        }
                    }
                }
            }
        }
        if (!correct) {
            for (Trade trade : history)
                Job.rollBack(trade);
        }
        return correct;
    }

    private void confirmTrades(){
        for (Trade t :  tradesPerformed){
            if (t!= null){
                if (t.getAmount() !=0) {
                    // Here it is possible to print information about trades.
                    companies.replace(t.getId(), t.getPrice());
                }
            }
        }
        tradesPerformed.clear();
    }

    private void performTrades() {
        TreeSet<Job> resSellJobs = new TreeSet<>(new JobComparator(false));
        TreeSet<Job> resBuyJobs = new TreeSet<>(new JobComparator(true));
        // for each Jobs.Job to do, try to process it in correct order. If it succeeded
        // update. Otherwise, remove Jobs.Job and move it straight to res (it will not be
        // processed again).

        while (!sellJobs.isEmpty() || !buyJobs.isEmpty()) {

            Job primaryJob = null;
            if (!sellJobs.isEmpty())
                primaryJob = sellJobs.first();
            if (primaryJob == null || (!buyJobs.isEmpty() && buyJobs.first().getTime() <= primaryJob.getTime())) {
                primaryJob = buyJobs.first();
            }

            assert (primaryJob != null);

            Vector<Job> jobs = new Vector<>();
            // primaryJob is the one I want to process in current iteration.
            int primaryJobPosition = -1;
            for (Job job : buyJobs) {
                if (job == primaryJob)
                    primaryJobPosition = jobs.size();
                jobs.add(job);
            }
            for (Job job : sellJobs) {
                if (job == primaryJob)
                    primaryJobPosition = jobs.size();
                jobs.add(job);
            }

            assert (primaryJobPosition != -1);

            if (!exhaustJob(jobs, primaryJobPosition))
                tradesPerformed.clear();

            buyJobs.clear();
            sellJobs.clear();
            confirmTrades();

            if (primaryJob.isBuy()) {
                resBuyJobs.add(primaryJob);
            } else {
                resSellJobs.add(primaryJob);
            }
            for (int i = 0; i < jobs.size(); ++i) {
                if (i != primaryJobPosition) {
                    if (jobs.get(i).isBuy()) {
                        buyJobs.add(jobs.get(i));
                    } else {
                        sellJobs.add(jobs.get(i));
                    }
                }
            }
        }
        sellJobs = resSellJobs;
        buyJobs = resBuyJobs;

    }

    public void clearJobs() {
        TreeSet<Job> resSellJobs = new TreeSet<>(new JobComparator(false));
        TreeSet<Job> resBuyJobs = new TreeSet<>(new JobComparator(true));
        for (Job job : sellJobs) {
            if (!job.deleteState(this))
                resSellJobs.add(job);
        }
        for (Job job : resBuyJobs) {
            if (!job.deleteState(this))
                resBuyJobs.add(job);
        }
        sellJobs = resSellJobs;
        buyJobs = resBuyJobs;

    }
}
