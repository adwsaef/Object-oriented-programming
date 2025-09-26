package queues;

import events.Event;

public class SortedArray implements Queue {
    // Array is sorted descending up to size-1.
    Event[] t = new Event[0];
    int size = 0;

    public SortedArray() {
    }

    private void increaseSize() {
        // Increase size two times.
        int newLength = Math.max(1, t.length * 2);
        Event[] t2 = new Event[newLength];

        System.arraycopy(t, 0, t2, 0, t.length);

        t = t2;
    }

    private void insert(Event other, int i) {
        if (t[i] == null) {
            t[i] = other;
        } else {

            if (t[i].compareTo(other) <= 0) {
                // If other should be here, swap other with t[i].
                Event tmp = other;
                other = t[i];
                t[i] = tmp;
            }

            insert(other, i + 1);
        }
    }

    @Override
    public void push(Event other) {
        if (size == t.length)
            increaseSize();

        insert(other, 0);
        ++size;
    }

    @Override
    public Event pop_first() {
        assert (size != 0);
        Event res = t[size - 1];
        t[size - 1] = null;
        --size;
        return res;
    }

    @Override
    public boolean empty() {
        return size == 0;
    }
}
