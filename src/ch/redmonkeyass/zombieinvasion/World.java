package ch.redmonkeyass.zombieinvasion;

import ch.redmonkeyass.zombieinvasion.entities.EntityHandler;
import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieinvasion.entities.module.ModuleHandler;
import ch.redmonkeyass.zombieinvasion.legacy.camera.Camera;
import ch.redmonkeyass.zombieinvasion.legacy.eventhandling.EventDispatcher;

public class World {

  private static final ModuleHandler MODULE_HANDLER = new ModuleHandler();
  private static final EntityHandler ENTITY_HANDLER = new EntityHandler();

  // create Box2d World without gravity
  private static final com.badlogic.gdx.physics.box2d.World B2DWORLD =
      new com.badlogic.gdx.physics.box2d.World(Vector2.Zero, true);

  // camera
  private static final Camera CAMERA =
      new Camera(Config.CAM_VIEWPORT_WIDTH, Config.CAM_VIEWPORT_HEIGHT);

  // EventHandler
  private static final EventDispatcher EVENT_DISPATCHER = new EventDispatcher();

  
  public static ModuleHandler getModuleHandler() {
    return MODULE_HANDLER;
  }

  public static com.badlogic.gdx.physics.box2d.World getB2World() {
    return B2DWORLD;
  }

  public static EntityHandler getEntityHandler() {
    return ENTITY_HANDLER;
  }

  public static Camera getCamera() {
    return CAMERA;
  }

  public static EventDispatcher getEventDispatcher() {
    return EVENT_DISPATCHER;
  }
}
