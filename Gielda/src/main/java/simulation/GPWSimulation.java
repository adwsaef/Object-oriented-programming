package simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import market.*;
import investors.*;

public class GPWSimulation {
    private static final int investorTypes = 3;

    private int[] investors;
    private Company[] companies;
    private Map<String, Integer> initial;
    private int rounds;
    private int money;
    private int shortSMA = 5, longSMA = 10;

    public GPWSimulation(String[] args) throws IOException {
        investors = new int[investorTypes];
        initial = new HashMap<>();
        BufferedReader reader;
        if (args.length != 2)
            throw new RuntimeException("niepoprawna liczba argumentów: " + args.length);

        rounds = Integer.parseInt(args[1]);

        if (rounds < 0)
            throw new RuntimeException("niepoprawna liczba tur: " + rounds);

        reader = new BufferedReader(new FileReader(args[0]));
        String currentLine;
        int state = 0;
        // 0 - expects Investor types // 1 - expects companies name with last price
        // 2 - expects investor initial state // 3 - expects optional extra arguments - SMA length strategy for SMAInvestor

        while ((currentLine = reader.readLine()) != null) {
            System.out.println("wczytana linia");
            System.out.println(currentLine);

            if (!currentLine.isEmpty() && currentLine.charAt(0) == '#') // line to be ignored.
                continue;

            if (state == 0) {
                readInvestors(currentLine);
                ++state;
                continue;
            }

            if (state == 1) {
                readCompanies(currentLine);
                ++state;
                continue;
            }

            if (state == 2) {
                readInitialState(currentLine);
                ++state;
                continue;
            }

            if (state == 3) {
                readSMAStrategy(currentLine);
                ++state;
                continue;
            }
            if (state == 4)
                throw new RuntimeException("niepoprawny plik");
        }

        if (state < 3)
            throw new RuntimeException("zbyt mało danych");

        for (String key : initial.keySet()) {
            boolean confirmed = false;
            for (Company company : companies) {
                if (Objects.equals(key, company.getId())) {
                    confirmed = true;
                    break;
                }
            }
            if (!confirmed) throw new RuntimeException("nierozpoznane akcje w portfelu");
        }

        reader.close();
    }


    private void readInvestors(String currentLine) {
        if (!currentLine.isEmpty() && currentLine.length() % 2 == 0)
            throw new RuntimeException("niepoprawne wejście - typy inwestorów");

        for (int i = 1; i < currentLine.length(); i += 2) {
            if (currentLine.charAt(i) != ' ')
                throw new RuntimeException("niepoprawne typy inwestorów");
        }
        for (int i = 0; i < currentLine.length(); i += 2) {
            if (currentLine.charAt(i) == 'R') {
                ++investors[0];
                continue;
            }
            if (currentLine.charAt(i) == 'S') {
                ++investors[1];
                continue;
            }
            if (currentLine.charAt(i) == 'G') {
                ++investors[2];
                continue;
            }
            throw new RuntimeException("nierozpoznawany typ inwestora" + currentLine.charAt(i));
        }
    }

    private void readCompanies(String currentLine) {

        companies = new Company[currentLine.split(":").length - 1];

        StringBuilder currentName = new StringBuilder();
        int lastPrice = 0, position = 0;
        boolean type = true; // true if i am reading company name, false if last price.

        for (int i = 0; i <= currentLine.length(); ++i) {
            if (i == currentLine.length() || currentLine.charAt(i) == ' ') {
                // Space means company was read, fill table and prepare to read next.
                companies[position++] = new Company(currentName.toString(), lastPrice);
                currentName = new StringBuilder();
                lastPrice = 0;
                type = true;
                continue;
            }

            if (currentLine.charAt(i) == ':') {
                type = false; // next number will be read.
                continue;
            }

            if (type)
                currentName.append(currentLine.charAt(i));

            if (!type) {
                if (!Character.isDigit(currentLine.charAt(i)))
                    throw new RuntimeException("niepoprawna cena");
                lastPrice *= 10;
                lastPrice += currentLine.charAt(i) - '0';
            }
        }
    }

    private void readInitialState(String currentLine) {
        money = 0;
        int i = 0;
        while (i < currentLine.length()) {
            if (!Character.isDigit(currentLine.charAt(i))) {
                if (i == 0)
                    throw new RuntimeException("brak pieniędzy początkowych inwestora");
                if (currentLine.charAt(i) != ' ')
                    throw new RuntimeException("niepoprawna liczba pieniędzy początkowych inwestora");
                break;
            }
            money *= 10;
            money += currentLine.charAt(i) - '0';
            ++i;
        }
        if (money < 0)
            throw new RuntimeException("początkowa liczba pieniędzy nie może być ujemna");

        StringBuilder companyName = new StringBuilder();
        int actionsCnt = 0;
        boolean type = true;
        while (++i <= currentLine.length()) {
            if (i == currentLine.length() || currentLine.charAt(i) == ' ') {
                // Space means company was read, fill table and prepare to read next.
                initial.put(companyName.toString(), actionsCnt);
                companyName = new StringBuilder();
                actionsCnt = 0;
                type = true;
                continue;
            }

            if (currentLine.charAt(i) == ':') {
                type = false; // next number will be read.
                continue;
            }

            if (type)
                companyName.append(currentLine.charAt(i));

            if (!type) {
                if (!Character.isDigit(currentLine.charAt(i)))
                    throw new RuntimeException("niepoprawna liczba akcji");
                actionsCnt *= 10;
                actionsCnt += currentLine.charAt(i) - '0';
            }
        }
    }

    private void readSMAStrategy(String currentLine) {
        String[] parts = currentLine.split(" ");
        if (parts.length != 2) throw new RuntimeException("niepoprawna strategia inwestorów");
        shortSMA = Integer.parseInt(parts[0]);
        longSMA = Integer.parseInt(parts[1]);
        if (shortSMA <= 0 || longSMA <= 0 || shortSMA >= longSMA)
            throw new RuntimeException("niepoprawna strategia inwestorów");
    }

    public void printInput() {
        System.out.println();
        System.out.println("wcztane dane");
        System.out.println("liczba tur:" + rounds);

        System.out.println();
        System.out.println("inwestorzy typu random: " + investors[0]);
        System.out.println("inwestorzy typu SMA:" + investors[1]);
        System.out.println("inwestorzy typu Zachłanni: " + investors[2]);

        System.out.println();
        System.out.println("Identyfikatory akcji wraz z poprzednimi cenami:");
        for (Company company : companies) System.out.print(company.getId() + ":  " + company.getStartPrice() + "  ");
        System.out.println();

        System.out.println();
        System.out.println("początkowy stan portfela inwestorów: " + money);
        System.out.println("początkowa liczba akcji inwestorów: ");
        for (Map.Entry<String, Integer> val : initial.entrySet())
            System.out.print(val.getKey() + ": " + val.getValue() + "  ");
        System.out.println();

        System.out.println();
        System.out.println("Obrana strategia inwestora SMA: " + shortSMA + " " + longSMA);
        System.out.println();
        System.out.println();
    }

    public Investor[] performSimulation() {
        int peopleCnt = 0;
        for (int investor : investors) peopleCnt += investor;
        Investor[] users = new Investor[peopleCnt];
        int pos = 0;
        for (int i = 0; i < investors.length; ++i) {
            for (int j = 0; j < investors[i]; ++j) {
                if (i == 0)
                    users[pos] = new RandomInvestor(money, initial);
                if (i == 1)
                    users[pos] = new SMAinvestor(money, initial, shortSMA, longSMA);
                if (i == 2)
                    users[pos] = new GreedyInvestor(money, initial);
                ++pos;
            }
        }

        StockMarket market = new StockMarket(users, companies);

        for (int i = 0; i < rounds; ++i) {
            market.nextRound();
        }

        System.out.println();
        for (Investor user : users) {
            user.print();
        }
        return users;
    }

    public static void main(String[] args) {
        GPWSimulation simulation;
        try {
            simulation = new GPWSimulation(args);
        } catch (Exception e) {
            System.out.println("błąd w danych wejściowych");
            System.out.println(e.getMessage());
            return;
        }

        simulation.printInput();

        simulation.performSimulation();
    }
}
