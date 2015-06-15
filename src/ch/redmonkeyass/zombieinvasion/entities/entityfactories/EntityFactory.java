package ch.redmonkeyass.zombieinvasion.entities.entityfactories;

import ch.redmonkeyass.zombieinvasion.World;
import ch.redmonkeyass.zombieinvasion.entities.Entity;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.MovementModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.PhysicsModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.SelectionModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.SimpleImageRenderModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.debugmodules.DebugRendererModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.mouse.MouseSelectionModule;
import ch.redmonkeyass.zombieinvasion.legacy.util.Images;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.UUID;

public class EntityFactory {

  /**create an entity with all its modules.
   *
   * @param entityType is being used for width and height
   */
  public static void createEntity(EntityType entityType) {
    switch (entityType) {
      case PLAYER_TEST:
        createTestEntity1(entityType,new Vector2(10,10));
        break;
//      case HANS:
//        createTestEntity1(entityType);
//        break;
//      case HANS:
//        createTestEntity1(entityType);
//        break;
      case MOUSE:
        createMouseEntity(entityType);
        break;
      case BOX:
      default: //ignore

    }
  }

  private static void createMouseEntity(EntityType entityType) {
    String id = "MOUSE: " + UUID.randomUUID();
    World.getEntityHandler().addEntity(new Entity(id, Entity.EntityStatus.INDESTRUCTIBLE));
    World.getModuleHandler().addModules(new MouseSelectionModule(id));
  }


  private static void createTestEntity1(EntityType entityType, Vector2 startPosition) {
    String id = "PLAYER_TEST: " + UUID.randomUUID();
    World.getEntityHandler().addEntity(new Entity(id));
    World.getModuleHandler().addModules(new SelectionModule(id));

    /*
     * Add Physics for testentity
     */
    {
      // First we create a body definition
      BodyDef bodyDef = new BodyDef();
      // We set our body to dynamic, for something like ground which doesn't move we would set it to
      // StaticBody
      bodyDef.type = BodyType.DynamicBody;
      // Set our body's starting position in object space (meters)
      bodyDef.position.set(startPosition);


      // Create a circle shape and set its radius to 6
      // PolygonShape shape = new PolygonShape();
      // shape.setAsBox(32, 32, new Vector2(16, 16), 0);
      PolygonShape shape = new PolygonShape();
      shape.setAsBox(entityType.getWidth() / 2, entityType.getHeight() / 2);

      FixtureDef fixtureDef = new FixtureDef();
      // Create a fixture definition to apply our shape to
      fixtureDef.shape = shape;
      fixtureDef.density = 0.01f;
      fixtureDef.friction = 0.01f;

      // Create our body in the world using our body definition
      Body body = World.getB2World().createBody(bodyDef);

      // Create our fixture and attach it to the body
      body.createFixture(fixtureDef);

      // Remember to dispose of any shapes after you're done with them!
      // BodyDef and FixtureDef don't need disposing, but shapes do.
      // shape.dispose();

      World.getModuleHandler().addModules(new PhysicsModule(id, body));
    }

    World.getModuleHandler()
        .addModules(new SimpleImageRenderModule(id, Images.TEST_ENTITY_IMG.getB2DScaled()));

    World.getModuleHandler().addModules(new MovementModule(id));

    // DebugModules
    World.getModuleHandler().addModules(new DebugRendererModule(id));
  }

  private static void createBox(EntityType type,Vector2 startPosition) {
    String id = "PLAYER_TEST: " + UUID.randomUUID();
    World.getEntityHandler().addEntity(new Entity(id));

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.StaticBody;
    bodyDef.position.set(startPosition);

    Body b2Body = World.getB2World().createBody(bodyDef);
    FixtureDef fixtureDef = new FixtureDef();
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(type.getHeight(), type.getWidth());
    fixtureDef.shape = shape;
    b2Body.createFixture(fixtureDef);

    World.getModuleHandler().addModules(new PhysicsModule(id,b2Body));
  }

}
