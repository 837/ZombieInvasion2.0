package ch.m837.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.modul.Modul;
import ch.zombieInvasion.Eventhandling.Event;
import ch.zombieInvasion.Eventhandling.EventDispatcher;

public class Entity {
  private final String ID;

  private ArrayList<Event> events = new ArrayList<>();

  private ArrayList<Modul> moduls = new ArrayList<>();

  public Entity(String ID) {
    this.ID = ID;
  }
  public void UPDATE_ENTITY() {
    events.clear();// XXX ned sicher ob ich das so möchti.. removes events every update
    events.addAll(EventDispatcher.getEvents().parallelStream()
        .filter(event -> event.getReceiverID().equals(ID)).collect(Collectors.toList()));
  }

  public String getID() {
    return ID;
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

  public ArrayList<Event> getEvents() {
    return events;
  }

  public void receiveEvent(Event event) {
    events.add(event);
  }

  public void addModul(Modul modul) {
    moduls.add(modul);
  }
}
