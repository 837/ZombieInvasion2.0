package ch.m837.zombieInvasion.entities.modul;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;

public abstract class Modul {
  private final String entityID;

  public Modul(String entityID) {
    this.entityID = entityID;
  }

  abstract public Object getData(DataType dataType);

  public String getEntityID() {
    return entityID;
  }
}
