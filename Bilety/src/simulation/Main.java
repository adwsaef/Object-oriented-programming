package simulation;

import city.City;
import queues.Queue;
import queues.SortedArray;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int days = input.nextInt();
        Queue q = new SortedArray();
        City c = new City(input);

        // Print data.
        System.out.println("Wczytane dane:");
        System.out.println();
        System.out.println("liczba dni: " + days);
        c.print();

        // Perform simulation.
        DailyStatistics[] res = new DailyStatistics[days];
        for (int i = 0; i < days; ++i) {
            res[i] = c.processDay(i, q);
        }

        // Print statistics.
        System.out.println();
        for (int i = 0; i < days; ++i) {
            res[i].print(i);
        }
        
        System.out.println();
        System.out.println();
        System.out.println("Globalna statystyka - łączna liczba przejazdów pasażerów "
                + c.getPassengersPassCounter());

        System.out.println("Globalna statystyka - średni czas czekania na przystanku "
                + c.getAveragePassengersWait() + " min");
    }
}
