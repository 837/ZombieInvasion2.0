package ch.redmonkeyass.zombieInvasion.entities.module.modules;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;

public class SelectionModule extends Module implements UpdatableModul {
  private boolean isSelected = false;

  public SelectionModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    if (dataType == DataType.IS_SELECTED) {
      return Optional.ofNullable(isSelected);
    }
    return Optional.empty();
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID()).ifPresent(events -> {
      events.parallelStream().forEach(event -> {
        switch (event.getEvent()) {
          case LEFT_CLICK_SELECTION:
            World.getEntityHandler()
                .getDataFrom(getEntityID(), DataType.COLLISION_FIXTURE, Fixture.class)
                .ifPresent(fixture -> event.getAdditionalInfo(Vector2.class).ifPresent(position -> {
              position.scl(Config.PIX2B);
              isSelected = fixture.testPoint(position);

              LogManager.getLogger("zombie")
                  .trace("SINGLE: Entity: " + getEntityID() + " isSelected: " + isSelected);
            }));
            break;

          case AREA_SELECTION:
            World.getEntityHandler().getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
                .ifPresent(
                    position -> event.getAdditionalInfo(Rectangle.class).ifPresent(rectangle -> {
              position.scl(Config.B2PIX);
              isSelected = rectangle.contains(position.x, position.y);

              LogManager.getLogger("zombie")
                  .trace("AREA: Entity: " + getEntityID() + " isSelected: " + isSelected);
            }));
            break;
        }

      });
    });
  }
}