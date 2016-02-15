package ch.redmonkeyass.zombieInvasion;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.camera.Camera;
import ch.redmonkeyass.zombieInvasion.entities.EntityHandler;
import ch.redmonkeyass.zombieInvasion.module.ModuleHandler;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventDispatcher;
import ch.redmonkeyass.zombieInvasion.worldmap.WorldMap;

public class WorldHandler {

  private static final ModuleHandler MODULE_HANDLER = new ModuleHandler();
  private static final EntityHandler ENTITY_HANDLER = new EntityHandler();

  // create Box2d WorldHandler without gravity
  private static final com.badlogic.gdx.physics.box2d.World B2DWORLD =
      new com.badlogic.gdx.physics.box2d.World(Vector2.Zero, true);

  // Camera
  private static final Camera CAMERA =
      new Camera(Config.CAM_VIEWPORT_WIDTH, Config.CAM_VIEWPORT_HEIGHT);

  // EventHandler
  private static final EventDispatcher EVENT_DISPATCHER = new EventDispatcher();

  // WorldMap
  private static final WorldMap WORLD_MAP = new WorldMap(Config.WORLDMAP_NAME);

  // WorldMap
  private static final ch.redmonkeyass.zombieInvasion.util.TextureLoader textures =
      new ch.redmonkeyass.zombieInvasion.util.TextureLoader(Config.RESSOURCE_FOLDER);

  public static ch.redmonkeyass.zombieInvasion.util.TextureLoader getTextures() {
    return textures;
  }

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

  public static WorldMap getWorldMap() {
    return WORLD_MAP;
  }
}
