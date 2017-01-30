package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MapPart {
  private final MapPartDescription mappartDescription;
  public final static int WALL_LAYER = 2;
  private Vector2 posOnMap;

  private ArrayList<Door> doors = new ArrayList<>();

  public MapPart(MapPartDescription mappartDescription) {
    this.mappartDescription = mappartDescription;
    this.mappartDescription.createDoors(doors);
    getDoors().forEach(d -> d.setParent(this));
  }

  public void setPosOnMap(Vector2 posOnMap) {
    this.posOnMap = posOnMap;
  }

  public Vector2 getPosOnMap() {
    return posOnMap;
  }

  public MapPartDescription getMappartDescription() {
    return mappartDescription;
  }

  public ArrayList<Door> getDoors() {
    return new ArrayList<>(doors);
  }
}
