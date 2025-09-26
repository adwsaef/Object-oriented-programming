package vehicles;

import events.*;
import city.*;
import queues.Queue;

public class Tram extends Vehicle {
    private static int capacity;
    private static final int firstTour = 6 * 60;
    private static final int lastTour = 23 * 60;

    public static void setCapacity(int val) {
        capacity = val;
    }

    public static int getCapacity() {
        return capacity;
    }

    private final boolean side;
    private final int start;
    private final Passenger[] p;
    private int size = 0;

    public Tram(Line l2, boolean side2, int start2) {
        super(l2);
        side = side2;
        start = start2;
        p = new Passenger[capacity];
    }

    @Override
    public void initDay(Queue q, int day) {
        int currentTime = firstTour + start;
        int len = l.getRouteLength();

        while (currentTime <= lastTour) {
            // Circle can start only before lastTour time.
            // Set helper variables, depending on side vehicles.Tram starts.
            int step = 1, pos = 0;
            boolean dir = true;
            if (!side) {
                step = -1;
                pos = len - 1;
                dir = false;
            }

            // vehicles.Tram's circle has two parts.
            for (int x = 0; x < 2; ++x) {

                for (int i = 0; i < len - 1; ++i) {
                    q.push(new TramOnStop(currentTime, q, day, this, pos, dir));
                    if (step == 1) {
                        currentTime += l.getSingleDistance(pos);
                    } else {
                        currentTime += l.getSingleDistance(pos - 1);
                    }
                    pos += step;
                }

                // Last stop before loop, and extra wait on loop.
                q.push(new TramOnStop(currentTime, q, day, this, pos, dir));
                currentTime += l.getLoopTime();

                // Change vehicles.Tram's direction.
                step *= -1;
                dir = !dir;
            }
        }

        // Last loop wait doesn't happen, vehicles.Tram just ends work one minute after it
        // reaches loop.
        currentTime -= l.getLoopTime();
        ++currentTime;
        q.push(new TramEndOfWork(currentTime, q, day, this));
    }

    public void initGettingOff(int time, Queue q, int day, Stop s, boolean dir) {
        // Add event for every passenger who wants to leave, 
        // they may or may not succeed.
        for (int i = 0; i < size; ++i) {
            if (p[i].getDestination().getId() == s.getId())
                q.push(new PassengerTriesOut(time, q, day, this, p[i], s, dir));
        }
    }

    public boolean tramFull() {
        return size == p.length;
    }

    public void newPassenger(Passenger p2) {
        // vehicles.Tram can't be full.
        assert (!tramFull());

        p[size++] = p2;
        p2.setInVehicle(true);
        p2.setPosition(size - 1);
    }

    public void removePassenger(Passenger p2) {
        // city.Passenger must be in tram.
        assert (0 <= p2.getPosition() && p2.getPosition() < p.length);
        assert (p[p2.getPosition()].getId() == p2.getId());

        // Move last passenger into p2 position, and decrease size.
        p[p2.getPosition()] = p[size - 1];
        p[size - 1].setPosition(p2.getPosition());
        p[size - 1] = null;
        --size;
    }

    public Passenger getPassenger(int passengerPos) {
        return p[passengerPos];
    }

    public int getSize() {
        return size;
    }
}
