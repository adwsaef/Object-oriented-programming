package vehicles;

import city.*;
import queues.Queue;

public abstract class Vehicle {
    private static int globalSideNum = 0;

    protected final int sideNum;
    protected final Line l;

    Vehicle(Line l2) {
        l = l2;
        sideNum = globalSideNum++;
    }

    public int getSideNum() {
        return sideNum;
    }

    public Line getLine() {
        return l;
    }

    public abstract void initDay(Queue q, int day);
}