package city;

import events.Event;
import queues.Queue;
import simulation.DailyStatistics;
import vehicles.Tram;
import vehicles.Vehicle;

import java.util.Scanner;

public class City {
    // Store data.
    private final Stop[] stops;
    private final Passenger[] passengers;
    private final Line[] lines;
    private final Vehicle[] vehicles;

    // Store global statistics.
    private int passengersWaitTime;
    private int passengersWaitCounter;
    private int passengersPassCounter;

    public City(Scanner sc) {

        // Load stops.
        Stop.SetCapacity(sc.nextInt());
        stops = new Stop[sc.nextInt()];
        // city.Stop's constructor loads necessary data.
        for (int i = 0; i < stops.length; ++i)
            stops[i] = new Stop(sc);

        // Load passenger.
        passengers = new Passenger[sc.nextInt()];
        // city.Passenger's constructor loads necessary data.
        for (int i = 0; i < passengers.length; ++i)
            passengers[i] = new Passenger(stops);

        Tram.setCapacity(sc.nextInt());

        // Load lines.
        lines = new Line[sc.nextInt()];
        int tramsCounter = 0;
        // city.Line's constructor loads necessary data.
        for (int i = 0; i < lines.length; ++i) {
            lines[i] = new Line(sc, stops);
            tramsCounter += lines[i].getTramCnt();
        }

        vehicles = new Tram[tramsCounter];

        // Init each tram first ride time.
        int k = 0;
        for (Line line : lines) {
            int delay = line.circleLength() / line.getTramCnt();
            int currentStart = 0;

            for (int j = 0; j < line.getTramCnt(); ++j) {
                if (j % 2 == 0) {
                    vehicles[k] = new Tram(line, true, currentStart);
                } else {
                    vehicles[k] = new Tram(line, false, currentStart);
                    currentStart += delay;
                }
                ++k;
            }

        }
    }

    public void print() {

        System.out.println("pojemność przystanków: " + Stop.getCapacity());
        System.out.println("liczba przystanków: " + stops.length);
        System.out.println();

        System.out.println("przystanki:");
        for (Stop stop : stops) stop.print();
        System.out.println();

        System.out.println("liczba pasażerów: " + passengers.length);
        System.out.println("pojemność tramwaju: " + Tram.getCapacity());
        System.out.println("liczba linii tramwajowych: " + lines.length);
        System.out.println();

        System.out.println("linie: ");
        for (Line line : lines) line.print();
        System.out.println();

    }

    public DailyStatistics processDay(int day, Queue q) {
        // Each vehicle sets its daily plan.
        for (Vehicle vehicle : vehicles) vehicle.initDay(q, day);

        // city.Passenger decides what he wants to do.
        for (Passenger passenger : passengers) passenger.initDay(q, day);

        while (!q.empty()) {
            q.pop_first().process();
        }

        DailyStatistics res = 
            new DailyStatistics(Event.getPassCounter(), Event.getTimeOnStops());

        // Update global statistics.
        passengersWaitTime += Event.getTimeOnStops();
        // Every passenger waited before his ride, but active ones waited also in the
        // end.
        passengersWaitCounter += Event.getPassCounter() + Event.getActivePassengers();
        passengersPassCounter += Event.getPassCounter();

        // Reset - days are independent.
        Event.passCounterReset();
        Event.timeOnStopsReset();
        Event.activePassengersReset();
        for (Stop stop : stops) stop.clear();

        return res;
    }

    public int getPassengersPassCounter() {
        return passengersPassCounter;
    }

    public float getAveragePassengersWait() {
        return (float) passengersWaitTime / (float) passengersWaitCounter;
    }
}
