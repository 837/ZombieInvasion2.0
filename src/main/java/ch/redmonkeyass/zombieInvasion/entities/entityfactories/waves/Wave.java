package ch.redmonkeyass.zombieInvasion.entities.entityfactories.waves;

import java.util.ArrayList;

import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityBuilder;

public class Wave {
  private String name;
  private ArrayList<EntityBuilder> entityBuilders = new ArrayList<>();

  public Wave(String name) {
    this.name = name;
  }

  public ArrayList<EntityBuilder> getEntityBuilders() {
    return entityBuilders;
  }

  public String getName() {
    return name;
  }
}
