package market;

import java.util.*;
import investors.*;
import jobs.*;

public abstract class TradingSystem {
    protected List<Investor> users;
    protected TreeSet<Job> sellJobs;
    protected TreeSet<Job> buyJobs;
    protected Hashtable<String, Integer> companies;
    protected Long round = 0L;

    public TradingSystem(Investor[] users2, Company[] companies2) {
        users = new ArrayList<>();
        sellJobs = new TreeSet<>(new JobComparator(false));
        buyJobs = new TreeSet<>(new JobComparator(true));
        companies = new Hashtable<>();

        users.addAll(Arrays.asList(users2));

        for (Company company : companies2) {
            companies.put(company.getId(), company.getStartPrice());
        }
    }

    public Long getRound() {
        return round;
    }

    public int getCompaniesCount() {
        return companies.size();
    }

    public String[] getCompanies() {
        String[] tmp = new String[companies.size()];
        int i = 0;
        for (HashMap.Entry<String, Integer> entry : companies.entrySet()) {
            tmp[i] = entry.getKey();
            ++i;
        }
        return tmp;
    }

    public int getPreviousPrice(String s) {
        return companies.get(s);
    }
}
