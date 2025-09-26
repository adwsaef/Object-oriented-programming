package events;

import city.Stop;
import vehicles.*;
import queues.Queue;

public class TramFinishReleasing extends Event {
    private final Tram t;
    private final int linePos;
    private final boolean dir;

    public TramFinishReleasing(int time2, Queue q2, int day2, Tram t2,
                               int linePos2, boolean dir2) {
        
        super(day2, time2, q2);
        t = t2;
        linePos = linePos2;
        dir = dir2;
    }

    public void process() {
        Stop s = t.getLine().getStop(linePos);
        System.out.print("Dzień " + day + " "
                + Event.timeCast(time) + ": tramwaj linii " + t.getLine().getId()
                + " numer " + t.getSideNum() + " kończy wypuszczać pasażerów");

        // Passengers are let in, only if it isnt tram last stop in tour.
        if ((dir && linePos != t.getLine().getRouteLength() - 1)
                || (!dir && linePos != 0)) {

            System.out.println();
            s.initGettingIn(time, q, day, t, linePos, dir);

        } else {
            System.out.println(" jest to ostatni przystanek na trasie, pasażerowie nie są wpuszczani");
        }
    }
}
