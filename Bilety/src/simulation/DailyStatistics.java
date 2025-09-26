package simulation;

public class DailyStatistics {
    private final int passCounter;
    private final int timeOnStops;

    public DailyStatistics(int passCounter2, int timeOnStops2) {
        passCounter = passCounter2;
        timeOnStops = timeOnStops2;
    }

    public void print(int day) {
        System.out.println("Dzień " + day +
                ": codzienna statystyka - liczba przejazdów " + passCounter);

        System.out.println("Dzień " + day +
                ": codzienna statystyka - sumaryczny czas czekania na przystankach "
                + timeOnStops + "min");

        System.out.println();
    }
}
