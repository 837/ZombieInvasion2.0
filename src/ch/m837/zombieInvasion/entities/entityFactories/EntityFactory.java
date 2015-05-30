package ch.m837.zombieInvasion.entities.entityFactories;

import java.util.UUID;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import ch.m837.zombieInvasion.Config;
import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.Entity;
import ch.m837.zombieInvasion.entities.module.modules.MovementModule;
import ch.m837.zombieInvasion.entities.module.modules.PhysicsModule;
import ch.m837.zombieInvasion.entities.module.modules.SelectionModule;
import ch.m837.zombieInvasion.entities.module.modules.SimpleImageRenderModule;
import ch.zombieInvasion.util.Images;

public class EntityFactory {
  static public void createEntity(EntityType type) {
    switch (type) {
      case TEST_ENTITY_1:
        createTestEntity1();
        break;

    }
  }

  static private void createTestEntity1() {
    String id = "TEST_ENTITY_1: " + UUID.randomUUID();
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
      // Set our body's starting position in the world
      bodyDef.position.set(100, 300);

      // Create our body in the world using our body definition
      Body body = World.getB2World().createBody(bodyDef);

      // Create a circle shape and set its radius to 6
      // PolygonShape shape = new PolygonShape();
      // shape.setAsBox(32, 32, new Vector2(16, 16), 0);
      CircleShape shape = new CircleShape();
      shape.setRadius(16);
      
      FixtureDef fixtureDef = new FixtureDef();
      // Create a fixture definition to apply our shape to
      fixtureDef.shape = shape;
      fixtureDef.density = 0.5f;
      fixtureDef.friction = 0.4f;


      // Create our fixture and attach it to the body
      Fixture fixture = body.createFixture(fixtureDef);

      // Remember to dispose of any shapes after you're done with them!
      // BodyDef and FixtureDef don't need disposing, but shapes do.
      shape.dispose();

      World.getModuleHandler().addModules(new PhysicsModule(id, body));
    }

    World.getModuleHandler()
        .addModules(new SimpleImageRenderModule(id, Images.TEST_ENTITY_IMG.get()));

    World.getModuleHandler().addModules(new MovementModule(id));
  }

}
