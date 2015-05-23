package ch.m837.zombieInvasion.entities;

public abstract class Modul {
  private final String modulID;
  private final String entityID;

  public Modul(String modulID, String entityID) {
    this.modulID = modulID;
    this.entityID = entityID;
  }

  abstract public Object getData(DataType dataType);

  abstract public void UPDATE();

  abstract public void RENDER();

  public String getModulID() {
    return modulID;
  }

  public String getEntityID() {
    return entityID;
  }
}
