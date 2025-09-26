package events;

import queues.Queue;
import vehicles.*;
import city.*;

public class TramEndOfWork extends Event {
    private static final int endOfDay = 24 * 60;
    private final Tram t;

    public TramEndOfWork(int time2, Queue q2, int day2, Tram t2) {
        super(day2, time2, q2);
        t = t2;
    }

    public void process() {
        System.out.println("Dzień " + day + " "
                + Event.timeCast(time) + ": tramwaj linii " + t.getLine().getId()
                + " numer " + t.getSideNum() + " kończy pracę ");

        // Remove each passenger and update statistics.
        while (t.getPassenger(0) != null) {
            Passenger p = t.getPassenger(0);
            
            System.out.println("Dzień " + day + " "
                    + Event.timeCast(time) + ": pasażer numer " + p.getId()
                    + " w nieokreślony sposób wraca do domu ");

            t.removePassenger(p);
            p.setSinceWhenOnStop(endOfDay);
            passCounterIncrease();
        }
    }
}
