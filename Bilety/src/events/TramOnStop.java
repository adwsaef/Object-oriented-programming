package events;

import city.*;
import vehicles.*;
import queues.Queue;

public class TramOnStop extends Event {
    private final Tram t;
    private final int linePos;
    private final boolean dir;

    public TramOnStop(int time2, Queue q2, int day2, Tram t2,
                      int linePos2, boolean dir2) {

        super(day2, time2, q2);
        t = t2;
        linePos = linePos2;
        dir = dir2;
    }

    public void process() {
        Stop s = t.getLine().getStop(linePos);
        System.out.println("Dzień " + day + " "
                + Event.timeCast(time) + ": przyjeżdża tramwaj linii "
                + t.getLine().getId() + " numer " + t.getSideNum()
                + " na przystanek " + s.getName()
                + " i rozpoczyna wypuszczanie pasażerów");

        // vehicles.Tram always starts with letting passenger out.
        t.initGettingOff(time, q, day, s, dir);

        // After events associated with letting out passengers from all trams 
        // in same time, are processed, trams will start letting passenger in.
        // If more than one tram is on same stop, every passenger who went out,
        // has chance to enter any tram on stop.
        q.push(new TramFinishReleasing(time, q, day, t, linePos, dir));
    }
}
