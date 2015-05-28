package ch.m837.zombieInvasion.entities.modul.moduls;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.modul.Modul;
import ch.m837.zombieInvasion.entities.modul.UpdatableModul;

public class SelectionModul extends Modul implements UpdatableModul {
  private boolean isSelected = false;

  public SelectionModul(String entityID) {
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
        case POINT_SELECTION_EVENT:
          // Shape s = (Shape) World.getEntityHandler().getDataFrom(getEntityID(),
          // DataType.COLLISION_SHAPE);
          // if(s.collide((Vector2D)event.getAdditionalInfo())){
          // isSelected=true;
          // }
          break;
        case AREA_SELECTION_EVENT:
          // Shape s = (Shape) World.getEntityHandler().getDataFrom(getEntityID(),
          // DataType.COLLISION_SHAPE);
          // Vector2D[] a = (Vector2D[])event.getAdditionalInfo();
          // if(s.collide(a)){
          // isSelected=true;
          // }
          break;
        default:
          break;
      }
    });

  }
}
