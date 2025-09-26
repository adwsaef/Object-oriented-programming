package city;

import events.*;
import queues.Queue;
import randomGenerator.Losowanie;


public class Passenger {
    private static int globalId = 0;
    private static final int goOutBegin = 6 * 60;
    private static final int goOutEnd = 12 * 60;
    private static final int endOfDay = 24 * 60;

    private final int id;
    private final Stop homeStop;

    private Stop destination;
    private boolean inVehicle = false;
    private int position; // In vehicle or in stop.
    private int sinceWhenOnStop;

    public Passenger(Stop[] s) {
        id = globalId++;
        homeStop = s[Losowanie.losuj(0, s.length - 1)];
    }

    public void initDay(Queue q, int day) {
        // Decide when to come on stop, and when go back home 
        // (unless straight from tram, if all stops are full)
        q.push(new PassengerComesToStop(Losowanie.losuj(goOutBegin, goOutEnd),
                q, day, this));

        q.push(new PassengerEndOfDay(endOfDay, q, day, this));

        sinceWhenOnStop = endOfDay;
        destination = null;
        inVehicle = false;
        position = 0;
    }

    public int getId() {
        return id;
    }

    public Stop getHomeStop() {
        return homeStop;
    }

    public void genDestination(Line l, int linePos, boolean dir) {
        // Can't gen destination if on last stop.
        if (!dir) {
            assert (linePos != 0);
            
            destination = 
                l.getStop(Losowanie.losuj(0, linePos - 1));
        } else {
            assert (linePos != l.getRouteLength() - 1);

            destination = 
                l.getStop(Losowanie.losuj(linePos + 1, l.getRouteLength() - 1));
        }
    }

    public Stop getDestination() {
        return destination;
    }

    public void setInVehicle(boolean val) {
        inVehicle = val;
    }

    public boolean getInVehicle() {
        return inVehicle;
    }

    public void setPosition(int val) {
        position = val;
    }

    public int getPosition() {
        return position;
    }

    public int getSinceWhenOnStop() {
        return sinceWhenOnStop;
    }

    public void setSinceWhenOnStop(int val) {
        sinceWhenOnStop = val;
    }
}
