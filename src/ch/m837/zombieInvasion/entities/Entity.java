package ch.m837.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ch.zombieInvasion.Eventhandling.Event;
import ch.zombieInvasion.Eventhandling.EventDispatcher;

public class Entity {
  private final String ID;

  private ArrayList<Event> events = new ArrayList<>();

  private ArrayList<Modul> moduls = new ArrayList<>();

  public void UPDATE() {
    events.addAll(EventDispatcher.getEvents().parallelStream()
        .filter(event -> event.getReceiverID().equals(ID)).collect(Collectors.toList()));

    moduls.forEach(modul -> modul.UPDATE());
  }

  public void RENDER() {
    moduls.forEach(modul -> modul.RENDER());
  }

  public Entity(String ID) {
    this.ID = ID;
  }

  public String getID() {
    return ID;
  }

  public ArrayList<Event> getEvents() {
    return events;
  }

  public Object getData(DataType dataType) {
    for (Modul currentModul : moduls) {
      Object data = currentModul.getData(dataType);
      if (data != null) {
        return data;
      }
    }
    return DataType.DATA_NOT_FOUND;
  }

  public void receiveEvent(Event event) {
    events.add(event);
  }
}
