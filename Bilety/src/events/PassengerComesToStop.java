package events;

import city.*;
import queues.Queue;

public class PassengerComesToStop extends Event {
    private final Passenger p;

    public PassengerComesToStop(int time2, Queue q2, int day2, Passenger p2) {
        super(day2, time2, q2);
        p = p2;
    }

    public void process() {
        if (!p.getHomeStop().stopFull()) {
            p.getHomeStop().newPassenger(p);
            // There is enough place at stop.
            System.out.println("Dzień " + day + " "
                    + Event.timeCast(time) + ": pasażer numer " + p.getId()
                    + " przychodzi na przystanek " + p.getHomeStop().getName());

            p.setSinceWhenOnStop(time);
            activePassengersIncrease();
        } else {
            // city.Stop is full.
            System.out.println("Dzień " + day + " "
                    + Event.timeCast(time) + " pasażer numer " + p.getId()
                    + " przychodzi na przystanek " + p.getHomeStop().getName()
                    + " ale odchodzi z powodu braku miejsca");
        }
    }
}
