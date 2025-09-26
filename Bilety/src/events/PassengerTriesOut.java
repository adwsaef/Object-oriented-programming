package events;

import city.*;
import vehicles.*;
import queues.Queue;

public class PassengerTriesOut extends Event {
    private final Passenger p;
    private final Tram t;
    private final Stop s;
    private final boolean dir;

    public PassengerTriesOut(int time2, Queue q2, int day2, Tram t2,
                             Passenger p2, Stop s2, boolean dir2) {

        super(day2, time2, q2);
        t = t2;
        p = p2;
        s = s2;
        dir = dir2;
    }

    public void process() {
        System.out.print("Dzień " + day + " "
                + Event.timeCast(time) + ": pasażer numer " + p.getId()
                + " próbuje wysiąść z tramwaju linii " + t.getLine().getId()
                + " numer " + t.getSideNum() + " w kierunku "
                + t.getLine().getFinalStop(dir).getName() + " na przystanek "
                + s.getName());

        if (s.stopFull()) {
            System.out.println(" ale nie udaje mu się to, przystanek jest pełen");
            return;
        }

        t.removePassenger(p);
        s.newPassenger(p);
        System.out.println(" z sukcesem");

        passCounterIncrease();
        p.setSinceWhenOnStop(time);
    }
}
