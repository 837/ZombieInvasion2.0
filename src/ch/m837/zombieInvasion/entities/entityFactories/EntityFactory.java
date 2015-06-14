package ch.m837.zombieInvasion.entities.entityFactories;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.Entity;
import ch.m837.zombieInvasion.entities.Entity.EntityStatus;
import ch.m837.zombieInvasion.entities.module.modules.MovementModule;
import ch.m837.zombieInvasion.entities.module.modules.PhysicsModule;
import ch.m837.zombieInvasion.entities.module.modules.SelectionModule;
import ch.m837.zombieInvasion.entities.module.modules.SimpleImageRenderModule;
import ch.m837.zombieInvasion.entities.module.modules.debugModules.DebugRendererModule;
import ch.m837.zombieInvasion.entities.module.modules.mouse.MouseSelectionModule;
import ch.zombieInvasion.util.Images;

public class EntityFactory {
  static public void createEntity(EntityType entityType) {
    switch (entityType) {
      case PLAYER_TEST:
        createTestEntity1(entityType);
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
    }
  }

  private static void createMouseEntity(EntityType entityType) {
    String id = "MOUSE: " + UUID.randomUUID();
    World.getEntityHandler().addEntity(new Entity(id, EntityStatus.INDESTRUCTIBLE));
    World.getModuleHandler().addModules(new MouseSelectionModule(id));
  }

  private static void createTestEntity1(EntityType entityType) {
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
      bodyDef.position.set(10, 10);

      // Create our body in the world using our body definition
      Body body = World.getB2World().createBody(bodyDef);

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

}
