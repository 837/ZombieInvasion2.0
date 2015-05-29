package ch.m837.zombieInvasion.entities.module;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;

public abstract class Module {
  private final String entityID;

  public Module(String entityID) {
    this.entityID = entityID;
  }

  abstract public Object getData(DataType dataType);

  public String getEntityID() {
    return entityID;
  }
}
