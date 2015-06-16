package ch.redmonkeyass.zombieInvasion.entities.module;

import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;

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
