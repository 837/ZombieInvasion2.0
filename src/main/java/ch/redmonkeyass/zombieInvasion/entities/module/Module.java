package ch.redmonkeyass.zombieInvasion.entities.module;

import java.util.Optional;

import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;

public abstract class Module {
  private final String entityID;

  public Module(String entityID) {
    this.entityID = entityID;
  }

  abstract public Optional<Object> getData(DataType dataType);

  public String getEntityID() {
    return entityID;
  }

  /**
   * Clean up Module, dispose of shapes or bodies
   * <p>
   * Called when you remove the Module
   */
  public void prepareModuleForRemoval() {
    // TODO Override if needed!
  }
}
