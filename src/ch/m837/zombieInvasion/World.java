package ch.m837.zombieInvasion;

import ch.m837.zombieInvasion.entities.EntityHandler;
import ch.m837.zombieInvasion.entities.modul.ModulHandler;

public class World {

  private static final ModulHandler modulHandler = new ModulHandler();
  private static final EntityHandler entityHandler = new EntityHandler();

  public static ModulHandler getModulHandler() {
    return modulHandler;
  }

  public static EntityHandler getEntityHandler() {
    return entityHandler;
  }
}
