package city;

import java.util.Scanner;

public class Line {
    private static int globalId;

    private final int id;
    private final int tramCnt;
    private final int loopStop; // Waiting time on loop.
    private final Stop[] route;
    private final int[] time; // Time of ride between stop.

    public Line(Scanner sc, Stop[] stops) {
        id = globalId++;

        tramCnt = sc.nextInt();
        int routeLength = sc.nextInt();

        time = new int[routeLength - 1];
        route = new Stop[routeLength];

        int tmp = 0;
        for (int i = 0; i < routeLength; ++i) {

            // Find stop with read name and fill route.
            String s = sc.next();
            for (Stop stop : stops) {
                if (s.equals(stop.getName())) {
                    route[i] = stop;
                    break;
                }
            }

            // Read time.
            if (i != routeLength - 1) {
                time[i] = sc.nextInt();
            } else {
                tmp = sc.nextInt();
            }
        }
        loopStop = tmp;
    }

    public void print() {
        System.out.println("identyfikator linii: " + id);
        System.out.println("liczba tramwajów kursujących na linii: " + tramCnt);
        System.out.println("liczba przystanków na linii: " + route.length);
        System.out.println("przystanki na linii: ");
        for (Stop stop : route) System.out.print(stop.getName() + " ");

        System.out.println();
        System.out.println("czasy przejazdu między kolejnymi przystankami: ");
        for (int j : time) System.out.print(j + " ");
        System.out.println();
        System.out.println("czas postoju na pętli: " + loopStop);
        System.out.println();
    }

    public int getId() {
        return id;
    }

    public int getTramCnt() {
        return tramCnt;
    }

    public int getLoopTime() {
        return loopStop;
    }

    public Stop getStop(int k) {
        return route[k];
    }

    public int getRouteLength() {
        return route.length;
    }

    public int getSingleDistance(int k) {
        return time[k];
    }

    public Stop getFinalStop(boolean dir) {
        if (dir)
            return route[route.length - 1];
        return route[0];
    }

    public int circleLength() {
        int res = loopStop;
        for (int j : time) res += j;
        return res * 2;
    }
}
