package ch.redmonkeyass.game.entityfactories.waves;

import java.util.ArrayList;

import ch.redmonkeyass.game.entityfactories.EntityBuilder;

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
