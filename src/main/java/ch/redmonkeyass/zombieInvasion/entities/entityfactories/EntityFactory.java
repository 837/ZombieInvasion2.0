package ch.redmonkeyass.zombieInvasion.entities.entityfactories;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.Entity;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.AStarMovementModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.EntityStatusModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.EventListenerModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.LightEmitter;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.PhysicsModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.SelectionModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.SimpleImageRenderModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.ThetaStarMovementModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.debugmodules.DebugRendererModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.game.DebugConsoleModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse.MouseSelectionModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse.MouseTileSelectionModule;
import ch.redmonkeyass.zombieInvasion.util.Images;

public class EntityFactory {
  static public void createEntity(EntityType entityType) {
    switch (entityType) {
      case ADOLF:
        createAdolf(entityType);
        break;
      case HANS:
        createHans(entityType);
        break;
      case GERHART:
        createGerhart(entityType);
        break;
      case ZOMBIE:
        createZombie(entityType);
        break;
      case MOUSE:
        createMouseEntity(entityType);
        break;
      case GAME:
        createGameEntity(entityType);
        break;
    }
  }

  private static void addFriction(Body b) {
    // FrictionJointDef fj = new FrictionJointDef();
    // fj.
  }

  private static void createMouseEntity(EntityType entityType) {
    String id = "MOUSE";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));
    WorldHandler.getModuleHandler().addModules(new MouseSelectionModule(id),
        new MouseTileSelectionModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createGameEntity(EntityType entityType) {
    String id = "GAME";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));

    WorldHandler.getModuleHandler().addModules(new DebugConsoleModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createAdolf(EntityType entityType) {
    String id = "ADOLF";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));
    WorldHandler.getModuleHandler().addModules(new SelectionModule(id));

    // WorldHandler.getModuleHandler().addModules(new EntityStatusModule(id));

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
      bodyDef.position.set(11.5f, 11.5f);

      // Create our body in the WorldHandler using our body definition
      Body body = WorldHandler.getB2World().createBody(bodyDef);

      PolygonShape shape = new PolygonShape();
      shape.setAsBox(entityType.getWidth() / 2, entityType.getHeight() / 2);

      FixtureDef fixtureDef = new FixtureDef();
      // Create a fixture definition to apply our shape to
      fixtureDef.shape = shape;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.0f;
      fixtureDef.restitution = 0.0f;


      // Create our fixture and attach it to the body
      body.createFixture(fixtureDef);

      // Remember to dispose of any shapes after you're done with them!
      // BodyDef and FixtureDef don't need disposing, but shapes do.
      // shape.dispose();

      WorldHandler.getModuleHandler()
          .addModules(new PhysicsModule(id, body, EntityType.ADOLF.getWidth()));
    }

    WorldHandler.getModuleHandler().addModules(new SimpleImageRenderModule(id, Images.ADOLF));

    WorldHandler.getModuleHandler().addModules(new AStarMovementModule(id),  new LightEmitter(id),
        new DebugRendererModule(id), new EventListenerModule(id));
  }

  private static void createHans(EntityType entityType) {
    String id = "HANS";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));
    WorldHandler.getModuleHandler().addModules(new SelectionModule(id));

    // WorldHandler.getModuleHandler().addModules(new EntityStatusModule(id));
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
      bodyDef.position.set(12.5f, 12.5f);

      // Create our body in the WorldHandler using our body definition
      Body body = WorldHandler.getB2World().createBody(bodyDef);


      PolygonShape shape = new PolygonShape();
      shape.setAsBox(entityType.getWidth() / 2, entityType.getHeight() / 2);

      FixtureDef fixtureDef = new FixtureDef();
      // Create a fixture definition to apply our shape to
      fixtureDef.shape = shape;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.0f;
      fixtureDef.restitution = 0.0f;


      // Create our fixture and attach it to the body
      body.createFixture(fixtureDef);

      // Remember to dispose of any shapes after you're done with them!
      // BodyDef and FixtureDef don't need disposing, but shapes do.
      // shape.dispose();

      WorldHandler.getModuleHandler().addModules(new PhysicsModule(id, body, EntityType.HANS.getWidth()));
    }

    WorldHandler.getModuleHandler().addModules(new SimpleImageRenderModule(id, Images.HANS));

    WorldHandler.getModuleHandler().addModules(new ThetaStarMovementModule(id));

    // DebugModules
    WorldHandler.getModuleHandler().addModules(new DebugRendererModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
    // WorldHandler.getModuleHandler().addModules(new LightEmitter(id));
  }

  private static void createGerhart(EntityType entityType) {
    String id = "GERHART";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));
    WorldHandler.getModuleHandler().addModules(new SelectionModule(id));

    // WorldHandler.getModuleHandler().addModules(new EntityStatusModule(id));
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
      bodyDef.position.set(13.5f, 13.5f);

      // Create our body in the WorldHandler using our body definition
      Body body = WorldHandler.getB2World().createBody(bodyDef);

      PolygonShape shape = new PolygonShape();
      shape.setAsBox(entityType.getWidth() / 2, entityType.getHeight() / 2);

      FixtureDef fixtureDef = new FixtureDef();
      // Create a fixture definition to apply our shape to
      fixtureDef.shape = shape;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.0f;
      fixtureDef.restitution = 0.0f;


      // Create our fixture and attach it to the body
      body.createFixture(fixtureDef);

      // Remember to dispose of any shapes after you're done with them!
      // BodyDef and FixtureDef don't need disposing, but shapes do.
      // shape.dispose();

      WorldHandler.getModuleHandler().addModules(new PhysicsModule(id, body, EntityType.GERHART.getWidth()));
    }

    WorldHandler.getModuleHandler().addModules(new SimpleImageRenderModule(id, Images.GERHART));

    WorldHandler.getModuleHandler().addModules(new ThetaStarMovementModule(id));

    // DebugModules
    WorldHandler.getModuleHandler().addModules(new DebugRendererModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createZombie(EntityType entityType) {
    String id = "ZOMBIE" + WorldHandler.getEntityHandler().getAllEntities().size() + 1;
    WorldHandler.getEntityHandler().addEntity(new Entity(id));
    WorldHandler.getModuleHandler().addModules(new SelectionModule(id));
    WorldHandler.getModuleHandler().addModules(new EntityStatusModule(id));

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
      bodyDef.position.set(12.5f, 12.5f);

      // Create our body in the WorldHandler using our body definition
      Body body = WorldHandler.getB2World().createBody(bodyDef);

      PolygonShape shape = new PolygonShape();
      shape.setAsBox(entityType.getWidth() / 2, entityType.getHeight() / 2);

      FixtureDef fixtureDef = new FixtureDef();
      // Create a fixture definition to apply our shape to
      fixtureDef.shape = shape;
      fixtureDef.density = 1.0f;
      fixtureDef.friction = 0.0f;
      fixtureDef.restitution = 0.0f;


      // Create our fixture and attach it to the body
      body.createFixture(fixtureDef);

      // Remember to dispose of any shapes after you're done with them!
      // BodyDef and FixtureDef don't need disposing, but shapes do.
      // shape.dispose();

      WorldHandler.getModuleHandler().addModules(new PhysicsModule(id, body, EntityType.ZOMBIE.getWidth()));
    }

    WorldHandler.getModuleHandler().addModules(new SimpleImageRenderModule(id, Images.ZOMBIE));

    // WorldHandler.getModuleHandler().addModules(new MovementModule(id));

    // DebugModules
    WorldHandler.getModuleHandler().addModules(new DebugRendererModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }
}
