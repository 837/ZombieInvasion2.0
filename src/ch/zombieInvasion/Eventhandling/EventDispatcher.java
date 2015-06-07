package ch.zombieInvasion.Eventhandling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class EventDispatcher {
   private ArrayList<Event> currentEvents = new ArrayList<>();
   private ArrayList<Event> persistentEvents = new ArrayList<>();

   private Comparator<Event> timeComparator = new Comparator<Event>() {
    @Override
    public int compare(Event m1, Event m2) {
      return (int) (m1.getDelayMillis() - m2.getDelayMillis());
    }
  };

   private PriorityQueue<Event> messagesQueue = new PriorityQueue<>(timeComparator);

   public void createEvent(long delayMillis, EventType msg, Object additonalInfo,
      String senderID, String receiverID) {
    Event event = new Event(delayMillis, msg, additonalInfo, senderID, receiverID);
    if (delayMillis == 0.0) {
      messagesQueue.add(event);
    } else if (delayMillis < 0.0) {
      event.setPersistent(true);
      messagesQueue.add(event);
    } else {
      long currentTime = System.currentTimeMillis();
      event.setDelayMillis(currentTime + delayMillis);
      messagesQueue.add(event);
    }
  }

   public void dispatchEvents() {
    long currentTime = System.currentTimeMillis();
    currentEvents.clear();
    while (!messagesQueue.isEmpty() && messagesQueue.peek().getDelayMillis() < currentTime) {
      if (messagesQueue.peek().isPersistent()) {
        persistentEvents.add(messagesQueue.poll());
      } else {
        currentEvents.add(messagesQueue.poll());
      }
    }
  }

   public ArrayList<Event> getEvents() {
    ArrayList<Event> allEvents = new ArrayList<>();
    allEvents.addAll(currentEvents);
    allEvents.addAll(persistentEvents);
    return allEvents;
  }

   public void removePersistentEvent(Event event) {
    persistentEvents.remove(event);
  }
}
