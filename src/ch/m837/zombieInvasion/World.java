package ch.m837.zombieInvasion;

import ch.m837.zombieInvasion.entities.EntityHandler;
import ch.m837.zombieInvasion.entities.module.ModuleHandler;
import com.badlogic.gdx.math.Vector2;

public class World {

  private static final ModuleHandler MODULE_HANDLER = new ModuleHandler();
  private static final EntityHandler entityHandler = new EntityHandler();

  //create Box2d World without gravity
  private static final com.badlogic.gdx.physics.box2d.World b2World =
          new com.badlogic.gdx.physics.box2d.World(Vector2.Zero,true);

  public static ModuleHandler getModuleHandler() {
    return MODULE_HANDLER;
  }

  public static com.badlogic.gdx.physics.box2d.World getB2World() {
    return b2World;
  }

  public static EntityHandler getEntityHandler() {
    return entityHandler;
  }
}
