package queues;

import events.Event;

public interface Queue {
    void push(Event z);

    Event pop_first();

    boolean empty();
}