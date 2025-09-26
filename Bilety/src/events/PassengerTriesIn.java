package events;

import city.*;
import vehicles.*;
import queues.Queue;

public class PassengerTriesIn extends Event {
    private final Stop s;
    private final Passenger p;

    private final Tram t;
    private final int linePos;
    private final boolean dir;

    public PassengerTriesIn(int time2, Queue q2, int day2, Tram t2, Stop s2,
                            int linePos2, boolean dir2, Passenger p2) {

        super(day2, time2, q2);
        t = t2;
        s = s2;
        p = p2;
        linePos = linePos2;
        dir = dir2;
    }

    public void process() {
        if (p.getInVehicle()) // city.Passenger already in vehicle.
            return;

        System.out.print("Dzień " + day + " "
                + Event.timeCast(time) + ": pasażer numer " + p.getId()
                + " próbuje wsiąść do tramwaju linii " + t.getLine().getId()
                + " numer " + t.getSideNum() + " na przystanku " + s.getName()
                + " w kierunku " + t.getLine().getFinalStop(dir).getName());

        if (t.tramFull()) {
            System.out.println(" ale nie udaje mu się to, tramwaj jest pełen");
            return;
        }
        // Update passenger, stop and tram.
        p.genDestination(t.getLine(), linePos, dir);
        s.removePassenger(p);
        t.newPassenger(p);
        
        System.out.println(" z sukcesem i zamiarem wyjścia na "
                + p.getDestination().getName());

        timeOnStopsAdd(time - p.getSinceWhenOnStop());
    }
}
