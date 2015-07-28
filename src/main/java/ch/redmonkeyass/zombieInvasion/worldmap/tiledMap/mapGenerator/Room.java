package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Room {
  private final RoomDescription roomDescription;
  public final static int WALL_LAYER = 2;
  private Vector2 posOnMap;

  private ArrayList<Door> doors = new ArrayList<>();

  public Room(RoomDescription roomDescription) {
    this.roomDescription = roomDescription;
    this.roomDescription.createDoors(doors);
    getDoors().forEach(d -> d.setParent(this));
  }

  public void setPosOnMap(Vector2 posOnMap) {
    this.posOnMap = posOnMap;
  }

  public Vector2 getPosOnMap() {
    return posOnMap;
  }

  public RoomDescription getRoomDescription() {
    return roomDescription;
  }

  public ArrayList<Door> getDoors() {
    return new ArrayList<>(doors);
  }
}
