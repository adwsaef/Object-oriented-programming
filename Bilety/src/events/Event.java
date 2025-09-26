package events;

import queues.Queue;

abstract public class Event implements Comparable<Event> {
    public static String timeCast(int minutes) {
        String res = "";
        int hours = minutes / 60, mins = minutes % 60;

        if (hours < 10)
            res += "0";
        res += String.valueOf(hours);

        res += ":";

        if (mins < 10)
            res += "0";
        res += String.valueOf(mins);

        return res;
    }

    // Daily statistics data.
    private static int passCounter;
    private static int timeOnStops;
    private static int activePassengers;

    // Getters and setters for each statistic variable.
    public static void passCounterReset() {
        passCounter = 0;
    }

    public static void passCounterIncrease() {
        ++passCounter;
    }

    public static int getPassCounter() {
        return passCounter;
    }

    public static void timeOnStopsReset() {
        timeOnStops = 0;
    }

    public static void timeOnStopsAdd(int val) {
        timeOnStops += val;
    }

    public static int getTimeOnStops() {
        return timeOnStops;
    }

    public static void activePassengersReset() {
        activePassengers = 0;
    }

    public static void activePassengersIncrease() {
        ++activePassengers;
    }

    public static int getActivePassengers() {
        return activePassengers;
    }

    protected final int day;
    protected final int time;
    protected final Queue q;

    public Event(int day2, int time2, Queue q2) {
        time = time2;
        q = q2;
        day = day2;
    }

    public int getTime() {
        return time;
    }

    public int compareTo(Event other) {
        return Integer.compare(time, other.time);
    }

    public abstract void process();
}
