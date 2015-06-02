package ch.m837.zombieInvasion;

import ch.m837.zombieInvasion.entities.EntityHandler;
import ch.m837.zombieInvasion.entities.module.ModuleHandler;
import com.badlogic.gdx.math.Vector2;

public class World {

  private static final ModuleHandler MODULE_HANDLER = new ModuleHandler();
  private static final EntityHandler ENTITY_HANDLER = new EntityHandler();

  /*
  temporary, there has to be a more sensible way to define this
   */
  public static final float B2PIX = 1920 / 20;
  public static final float PIX2B = 1 / B2PIX;

  // create Box2d World without gravity
  private static final com.badlogic.gdx.physics.box2d.World B2DWORLD =
      new com.badlogic.gdx.physics.box2d.World(Vector2.Zero, true);

  public static ModuleHandler getModuleHandler() {
    return MODULE_HANDLER;
  }

  public static com.badlogic.gdx.physics.box2d.World getB2World() {
    return B2DWORLD;
  }

  public static EntityHandler getEntityHandler() {
    return ENTITY_HANDLER;
  }
}
