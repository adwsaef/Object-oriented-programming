package city;

import events.PassengerTriesIn;
import queues.Queue;
import vehicles.Tram;

import java.util.Arrays;
import java.util.Scanner;

public class Stop {
    private static int globalId;
    private static int capacity;

    public static void SetCapacity(int val) {
        capacity = val;
    }

    public static int getCapacity() {
        return capacity;
    }

    private final String name;
    private final Passenger[] p;
    private final int id;
    private int size;

    public Stop(Scanner sc) {
        name = sc.next();
        id = globalId++;
        p = new Passenger[capacity];
    }

    public void print() {
        System.out.println("przystanek o nazwie " + name
                + " i identyfikatorze " + id);
    }

    public String getName() {
        return name;
    }

    public Passenger getPassenger(int k) {
        return p[k];
    }

    public int getId() {
        return id;
    }

    public void newPassenger(Passenger p2) {
        assert (!stopFull());

        p[size++] = p2;
        p2.setInVehicle(false);
        p2.setPosition(size - 1);
    }

    public void removePassenger(Passenger p2) {
        // city.Passenger must be on stop.
        assert (0 <= p2.getPosition() && p2.getPosition() < p.length);
        assert (p[p2.getPosition()].getId() == p2.getId());

        // Move last passenger into p2 position, and decrease size.
        p[p2.getPosition()] = p[size - 1];
        p[size - 1].setPosition(p2.getPosition());
        p[size - 1] = null;
        --size;
    }

    public boolean stopFull() {
        return size == capacity;
    }

    public void initGettingIn(int time, Queue q, int day, Tram t,
                              int linePos, boolean dir) {

        for (int i = 0; i < size; ++i) { // Every passenger tries to get in.
            q.push(new PassengerTriesIn(time, q, day, t, this, linePos, dir, p[i]));
        }
    }

    public void clear() {
        size = 0;
        Arrays.fill(p, null);
    }

    public int getSize() {
        return size;
    }
}
