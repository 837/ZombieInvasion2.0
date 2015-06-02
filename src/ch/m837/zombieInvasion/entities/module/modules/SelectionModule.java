package ch.m837.zombieInvasion.entities.module.modules;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

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
        case LEFT_CLICK:
          World.getEntityHandler().getDataFrom(getEntityID(), DataType.COLLISION_FIXTURE)
              .ifPresent(fixtureData -> {
            Fixture fixture = (Fixture) fixtureData;
            isSelected = fixture.testPoint((Vector2) event.getAdditionalInfo());
            System.out.println("Entity: " + getEntityID() + " isSelected: " + isSelected);

          });
          break;
        case AREA_SELECTION_EVENT:
          World.getEntityHandler().getDataFrom(getEntityID(), DataType.COLLISION_FIXTURE)
              .ifPresent(fixtureData -> {
            Fixture fixture = (Fixture) fixtureData;

            // if (fixture2.testPoint(a)) {
            // isSelected = true;
            // }
          });;


          break;
        default:
          break;
      }
    });

  }
}
