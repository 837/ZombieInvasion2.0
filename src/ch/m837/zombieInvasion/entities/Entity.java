package ch.m837.zombieInvasion.entities;

import java.util.ArrayList;

import ch.zombieInvasion.Eventhandling.Event;

public class Entity {
  private final String ID;

  private ArrayList<Event> events = new ArrayList<>();

  public Entity(String ID) {
    this.ID = ID;
  }

  public String getID() {
    return ID;
  }

  public ArrayList<Event> getEvents() {
    return events;
  }
}
