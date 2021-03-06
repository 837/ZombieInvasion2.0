package ch.redmonkeyass.game.entityfactories;

import ch.redmonkeyass.game.module.modules.PhysicsModule;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;

public class EntityFactoryHelper {
  /**
   * Creates a physicsModule for the entity attaches the enitie's id as userdata of the box2d body
   * 
   * @param entityBuilder
   * @param id
   */
  public static PhysicsModule createPhysicsModule(EntityBuilder entityBuilder, String id) {
    // First we create a body definition
    BodyDef bodyDef = new BodyDef();
    // We set our body to dynamic, for something like ground which doesn't move we would set it to
    // StaticBody
    bodyDef.type = BodyType.DynamicBody;
    // Set our body's starting position in object space (meters)
    bodyDef.position.set(new Vector2(entityBuilder.getStartPosition())
        .add((float) Math.random() * 0.1f, (float) Math.random() * 0.1f));

    // Create our body in the WorldHandler using our body definition
    Body body = WorldHandler.getB2World().createBody(bodyDef);

    // PolygonShape shape = new PolygonShape();
    // shape.setAsBox(entityBuilder.getEntityType().getWidth() / 2,
    // entityBuilder.getEntityType().getHeight() / 2);

    CircleShape shape = new CircleShape();
    shape.setRadius(Math.max(entityBuilder.getEntityType().getWidth() / 2,
        entityBuilder.getEntityType().getHeight() / 2));

    // Create our fixture and attach it to the body
    body.createFixture(shape, 1f);


    // Remember to dispose of any shapes after you're done with them!
    // BodyDef and FixtureDef don't need disposing, but shapes do.
    shape.dispose();
    body.setUserData(id);
    return new PhysicsModule(id, body, entityBuilder.getEntityType().getWidth());
  }
}
