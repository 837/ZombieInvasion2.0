package ch.redmonkeyass.zombieInvasion.entities.module;

import java.util.Optional;

import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;

/**
 * Every Module needs to extend this class.
 * 
 * @author Matthias
 *
 */
public abstract class Module {
  private final String entityID;

  public Module(String entityID) {
    this.entityID = entityID;
  }

  /**
   * GetData is the data provider of each Module, add all DataTypes a Module <b>can/should
   * share </b>with other Modules.
   * 
   * @param dataType
   * @return
   */
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
