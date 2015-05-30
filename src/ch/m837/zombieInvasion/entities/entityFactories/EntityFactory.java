package ch.m837.zombieInvasion.entities.entityFactories;

import java.util.UUID;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

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
      Vector2 position = Vector2.Zero;
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyDef.BodyType.DynamicBody; // others: static body, kinetic body (means it
                                                   // can
                                                   // move but will not react to forces)
      bodyDef.allowSleep = true; // redundant - i think default is already true...
      bodyDef.bullet = false;// redundant - set to true if object is supposed to move very very fast
      bodyDef.fixedRotation = false;// redundant,
      bodyDef.position.set(position);

      // add body to World
      Body body = World.getB2World().createBody(bodyDef);

      FixtureDef fixture = new FixtureDef();
      fixture.isSensor = false; // sensors register collissions but they're not affected by forces
      PolygonShape shape = new PolygonShape();
      shape.setAsBox(32f, 32f, new Vector2(position.x + Images.TEST_ENTITY_IMG.getRadiusW(),
          position.y + Images.TEST_ENTITY_IMG.getRadiusH()), 0f); // a 32x32
      // rectangle
      fixture.shape = shape;
      fixture.density = 1;
      fixture.friction = 0.1f;

      body.createFixture(fixture); // bodies can have multiple shapes
      World.getModuleHandler().addModules(new PhysicsModule(id, body));
    }

    World.getModuleHandler()
        .addModules(new SimpleImageRenderModule(id, Images.TEST_ENTITY_IMG.get()));

    World.getModuleHandler().addModules(new MovementModule(id));
  }

}
