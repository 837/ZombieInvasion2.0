package ch.m837.zombieInvasion.entities.module.modules;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import ch.m837.zombieInvasion.Config;
import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.module.Module;
import ch.m837.zombieInvasion.entities.module.UpdatableModul;

public class SelectionModule extends Module implements UpdatableModul {
  private boolean isSelected = false;

  public SelectionModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getData(DataType dataType) {
    if (dataType == DataType.IS_SELECTED) {
      return isSelected;
    }
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID()).parallelStream().forEach(event -> {
      switch (event.getEvent()) {
        case LEFT_CLICK_SELECTION:
          World.getEntityHandler()
              .getDataFrom(getEntityID(), DataType.COLLISION_FIXTURE, Fixture.class)
              .ifPresent(fixture -> {
            event.getAdditionalInfo(Vector2.class).ifPresent(position -> {
              position.scl(Config.PIX2B);
              isSelected = fixture.testPoint(position);

              System.out.println("SINGLE: Entity: " + getEntityID() + " isSelected: " + isSelected);
            });
          });
          break;

        case AREA_SELECTION:
          World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
              .ifPresent(position -> {
            event.getAdditionalInfo(Rectangle.class).ifPresent(rectangle -> {
              position.add(World.getCamera().getPosition());
              position.scl(Config.B2PIX);


              isSelected = rectangle.contains(position.x, position.y);

              System.out.println("AREA: Entity: " + getEntityID() + " isSelected: " + isSelected);
            });;

          });
          break;
      }

    });

  }
}
