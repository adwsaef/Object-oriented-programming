package events;

import city.*;
import queues.Queue;

public class PassengerEndOfDay extends Event {
    private static final int endOfDay = 24 * 60;
    private final Passenger p;

    public PassengerEndOfDay(int time2, Queue q2, int day2, Passenger p2) {
        super(day2, time2, q2);
        p = p2;
    }

    public void process() {
        if (p.getSinceWhenOnStop() == endOfDay)
            // city.Passenger wasn't travelling today.
            return;

        System.out.println("Dzień " + (day + 1) +
                " 00:00: pasażer numer " + p.getId() + " wraca do domu");

        timeOnStopsAdd(endOfDay - p.getSinceWhenOnStop());
    }
}
