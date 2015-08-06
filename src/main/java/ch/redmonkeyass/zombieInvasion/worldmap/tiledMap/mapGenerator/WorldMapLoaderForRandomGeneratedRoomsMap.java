package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.util.Comparables;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.TiledMap;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.Door.DOORDIR;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.Door.DOORSTATE;

public class WorldMapLoaderForRandomGeneratedRoomsMap extends MapLoader {
  private ArrayList<MapPart> addedRooms = new ArrayList<>();
  private ArrayList<MapPartDescription> roomDescriptions = new ArrayList<>();

  public WorldMapLoaderForRandomGeneratedRoomsMap(String backgroundMapPath) throws Exception {
    super(backgroundMapPath);
    roomDescriptions = loadRoomDescription("res/tiledMap/rooms/");
    setFinishedMap(generateTiledMapOnlyConsistingOfRooms());
  }



  /**
   * Recursive function to generate random map
   * 
   * @param doors
   * @param roomDescriptions
   */
  private void createMap(ArrayList<Door> doors) {
    Door door = getRandomOpenDoor(doors);
    if (door == null || addedRooms.size() > Config.MAX_ROOMS) {
      return;
    }

    ArrayList<MapPartDescription> tmpRoomsForThisDoor = new ArrayList<>(
        roomDescriptions.stream().filter(r -> !r.getName().equals("room0_startzone_10x10.tmx"))
            .collect(Collectors.toList()));
    boolean isOverlapping = false;
    MapPart room = null;
    do {
      isOverlapping = false;
      room = getRandomRoomWithDoorOppositeOf(door.getDoordir(), tmpRoomsForThisDoor);
      if (room == null) {
        door.setDoorstate(DOORSTATE.Closed);
        break;
      }
      calculatePosition(door.getParent(), door, room, room.getDoors().stream()
          .filter(d -> d.getDoordir() == DOORDIR.getOpposite(door.getDoordir())).findAny().get());
      for (int i = 0; i < addedRooms.size(); i++) {
        if (doTheyOverlap(room, addedRooms.get(i))) {
          isOverlapping = true;
          // System.out.println("Overlapped");
        }
      }
    } while (isOverlapping);
    if (room != null) {
      addedRooms.add(room);
      door.setDoorstate(DOORSTATE.Closed);
      doors.addAll(room.getDoors());
      // System.out.println("Doors num: " + doors.size());
      // System.out.println("CurrentRoomName: " + room.getRoomDescription().getName());
    }
    createMap(doors);
  }

  /**
   * Returns a random open door
   * 
   * @param doors
   * @return random door
   */
  private Door getRandomOpenDoor(ArrayList<Door> doors) {
    List<Door> tmpList =
        doors.stream().filter(d -> d.getDoorstate() == DOORSTATE.Open).collect(Collectors.toList());
    if (tmpList.isEmpty()) {
      return null;
    }
    return tmpList.get(new Random().nextInt(tmpList.size()));
  }

  /**
   * Returns a random door with opposite dir of doordir
   * 
   * @param doordir
   * @param tmpRoomsForThisDoor
   * @return random door with opposite dir of doordir
   */
  private MapPart getRandomRoomWithDoorOppositeOf(DOORDIR doordir,
      ArrayList<MapPartDescription> tmpRoomsForThisDoor) {

    List<MapPartDescription> goodRooms = tmpRoomsForThisDoor.stream()
        .filter(rd -> rd.getDoorDescriptions().stream()
            .anyMatch(d -> d.getDoordir() == DOORDIR.getOpposite(doordir)))
        .collect(Collectors.toList());
    if (goodRooms.isEmpty()) {
      return null;
    }
    int index = new Random().nextInt(goodRooms.size());
    MapPart room = new MapPart(goodRooms.get(index));
    tmpRoomsForThisDoor.remove(room.getMappartDescription());
    return room;
  }

  /**
   * Tests if the room toTest overlaps with the second room rSecond
   * 
   * @param toTest
   * @param rSecond
   * @return if it overlaps
   */
  private boolean doTheyOverlap(MapPart toTest, MapPart rSecond) {
    Rectangle r1 = new Rectangle(toTest.getPosOnMap().x, toTest.getPosOnMap().y,
        toTest.getMappartDescription().getWidth(), toTest.getMappartDescription().getHeight());
    Rectangle r2 = new Rectangle(rSecond.getPosOnMap().x, rSecond.getPosOnMap().y,
        rSecond.getMappartDescription().getWidth(), rSecond.getMappartDescription().getHeight());

    // System.out.println("X1: " + r1.x + "Y1: " + r1.y + "W1: " + r1.width + "H1: " + r1.height);
    // System.out.println("X2: " + r2.x + "Y2: " + r2.y + "W2: " + r2.width + "H2: " + r2.height);
    return r1.overlaps(r2);
  }

  /**
   * calculates the position the new room should be place in the world.
   * 
   * @param rFirst
   * @param dFirst
   * @param rSecond
   * @param dSecond
   */
  private void calculatePosition(MapPart rFirst, Door dFirst, MapPart rSecond, Door dSecond) {
    // System.out.println("RoomFirst: " + rFirst.getRoomDescription().getName());
    // System.out.println("RoomSecond: " + rSecond.getRoomDescription().getName());
    int rSecondX = 0;
    int rSecondY = 0;
    int rFirstX = (int) rFirst.getPosOnMap().x;
    int rFirstY = (int) rFirst.getPosOnMap().y;

    int dFirstX = (int) dFirst.getStartPosDoorTiles().x;
    int dFirstY = (int) dFirst.getStartPosDoorTiles().y;

    int dSecondX = (int) dSecond.getStartPosDoorTiles().x;
    int dSecondY = (int) dSecond.getStartPosDoorTiles().y;

    switch (dFirst.getDoordir()) {
      case East:
        rSecondX = rFirstX + rFirst.getMappartDescription().getWidth();
        rSecondY = rFirstY + dFirstY - dSecondY;
        break;
      case North:
        rSecondX = rFirstX + dFirstX - dSecondX;
        rSecondY = rFirstY - rSecond.getMappartDescription().getHeight();
        break;
      case South:
        rSecondX = rFirstX + dFirstX - dSecondX;
        rSecondY = rFirstY + rFirst.getMappartDescription().getHeight();
        break;
      case West:
        rSecondX = rFirstX - rSecond.getMappartDescription().getWidth();
        rSecondY = rFirstY + dFirstY - dSecondY;
        break;
    }
    rSecond.setPosOnMap(new Vector2(rSecondX, rSecondY));
  }

  /**
   * Creates a random generated tiledmap, based on the room in the res folder
   * 
   * @return generated tiledmap
   */
  private TiledMap generateTiledMapOnlyConsistingOfRooms() {
    // FIXME throws error when not found
    MapPartDescription startRoomDescription = roomDescriptions.stream()
        .filter(r -> r.getName().equals("room0_startzone_10x10.tmx")).findAny().get();



    MapPart startRoom = new MapPart(startRoomDescription);
    startRoom.setPosOnMap(new Vector2(0, 0));
    addedRooms.add(startRoom);
    createMap(startRoom.getDoors());
    ArrayList<Vector2> pos = new ArrayList<>();

    addedRooms.forEach(r -> pos.add(r.getPosOnMap()));

    List<Vector2> top =
        pos.stream().sorted(new Comparables.SortSmallestY()).collect(Collectors.toList());
    Vector2 vTop = top.get(0);

    List<Vector2> left =
        pos.stream().sorted(new Comparables.SortSmallestX()).collect(Collectors.toList());
    Vector2 vLeft = left.get(0);

    MapPart rTop = addedRooms.stream().filter(r -> r.getPosOnMap().equals(vTop)).findAny().get();

    MapPart rLeft = addedRooms.stream().filter(r -> r.getPosOnMap().equals(vLeft)).findAny().get();

    int yOffSet = (int) rTop.getPosOnMap().y * -1;
    int xOffSet = (int) rLeft.getPosOnMap().x * -1;
    addedRooms.forEach(r -> r.setPosOnMap(r.getPosOnMap().add(xOffSet, yOffSet)));

    addedRooms.forEach(r -> {
      int LAYERS = 3;
      for (int l = 0; l < LAYERS; l++) {
        for (int x = (int) r.getPosOnMap().x; x < r.getMappartDescription().getWidth()
            + (int) r.getPosOnMap().x; x++) {
          for (int y = (int) r.getPosOnMap().y; y < r.getMappartDescription().getHeight()
              + (int) r.getPosOnMap().y; y++) {
            int tileID = r.getMappartDescription().getRoomMap().getTileId(x - (int) r.getPosOnMap().x,
                y - (int) r.getPosOnMap().y, l);
            backgroundMap.setTileId(x, y, l, tileID);
          }
        }
      }
    });

    MapPart room = addedRooms.stream()
        .filter(r -> r.getMappartDescription().getName().equals("room0_startzone_10x10.tmx")).findAny()
        .get();

    setStartRoomPos(room.getPosOnMap().cpy().scl(((64 / Config.B2PIX) / 2) * 2)
        .add(room.getMappartDescription().getWidth(), room.getMappartDescription().getHeight()));
    return backgroundMap;
  }


  /**
   * loads the rooms from the res folder into roomdescriptions
   * 
   * @param pathToRooms
   * @return
   * @throws Exception
   */
  private ArrayList<MapPartDescription> loadRoomDescription(String pathToRooms) throws Exception {
    File roomFolder = new File(pathToRooms);
    File[] allRooms = roomFolder.listFiles();

    ArrayList<MapPartDescription> rooms = new ArrayList<>();

    for (int i = 0; i < allRooms.length; i++) {
      File currentRoom = allRooms[i];
      if (currentRoom.getName().endsWith(".tmx")) {
        rooms.add(new MapPartDescription(loadTiledMap("/rooms/" + currentRoom.getName()),
            currentRoom.getName()));
        // logger.trace("Added room: " + currentRoom.getName());
      }
    }
    return rooms;
  }
}
