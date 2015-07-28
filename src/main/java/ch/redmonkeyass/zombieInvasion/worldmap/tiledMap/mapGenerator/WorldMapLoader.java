package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.util.Comparables;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.TiledMap;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.Door.DOORDIR;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.Door.DOORSTATE;

public class WorldMapLoader {
  private TiledMap backgroundMap = null;
  private ArrayList<RoomDescription> roomDescriptions = new ArrayList<>();
  private TiledMap finishedMap = null;

  private Logger logger = LogManager.getLogger(WorldMapLoader.class);

  public ArrayList<Room> getAddedRooms() {
    return addedRooms;
  }

  public TiledMap getFinishedMap() {
    return finishedMap;
  }

  public Vector2 getStartRoomPos() {
    Room room = addedRooms.stream()
        .filter(r -> r.getRoomDescription().getName().equals("room0_startzone_10x10.tmx")).findAny()
        .get();
    System.out.println(room.getPosOnMap().cpy().scl(WorldHandler.getWorldMap().getNodeSizeInMeter())
        .add(room.getRoomDescription().getWidth(), room.getRoomDescription().getHeight()));
    return room.getPosOnMap().cpy().scl(WorldHandler.getWorldMap().getNodeSizeInMeter())
        .add(room.getRoomDescription().getWidth(), room.getRoomDescription().getHeight());
  }

  public WorldMapLoader(String backgroundMapPath) {
    try {
      backgroundMap = loadTiledMap(backgroundMapPath);
      roomDescriptions = loadRoomDescription("res/tiledMap/rooms/");
    } catch (Exception e) {
      logger.trace(e);
    }
    finishedMap = generateTiledMap();
  }

  private ArrayList<Room> addedRooms = new ArrayList<>();

  private void createMap(ArrayList<Door> doors) {
    Door door = getRandomOpenDoor(doors);
    if (door == null || addedRooms.size() > 50) {
      return;
    }

    ArrayList<RoomDescription> tmpRoomsForThisDoor = new ArrayList<>(
        roomDescriptions.stream().filter(r -> !r.getName().equals("room0_startzone_10x10.tmx"))
            .collect(Collectors.toList()));
    boolean isOverlapping = false;
    Room room = null;
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

  private Door getRandomOpenDoor(ArrayList<Door> doors) {
    List<Door> tmpList =
        doors.stream().filter(d -> d.getDoorstate() == DOORSTATE.Open).collect(Collectors.toList());
    if (tmpList.isEmpty()) {
      return null;
    }
    return tmpList.get(new Random().nextInt(tmpList.size()));
  }

  private Room getRandomRoomWithDoorOppositeOf(DOORDIR doordir,
      ArrayList<RoomDescription> tmpRoomsForThisDoor) {

    List<RoomDescription> goodRooms = tmpRoomsForThisDoor.stream()
        .filter(rd -> rd.getDoorDescriptions().stream()
            .anyMatch(d -> d.getDoordir() == DOORDIR.getOpposite(doordir)))
        .collect(Collectors.toList());
    if (goodRooms.isEmpty()) {
      return null;
    }
    int index = new Random().nextInt(goodRooms.size());
    Room room = new Room(goodRooms.get(index));
    tmpRoomsForThisDoor.remove(room.getRoomDescription());
    return room;
  }

  private boolean doTheyOverlap(Room toTest, Room rSecond) {
    Rectangle r1 = new Rectangle(toTest.getPosOnMap().x, toTest.getPosOnMap().y,
        toTest.getRoomDescription().getWidth(), toTest.getRoomDescription().getHeight());
    Rectangle r2 = new Rectangle(rSecond.getPosOnMap().x, rSecond.getPosOnMap().y,
        rSecond.getRoomDescription().getWidth(), rSecond.getRoomDescription().getHeight());

    // System.out.println("X1: " + r1.x + "Y1: " + r1.y + "W1: " + r1.width + "H1: " + r1.height);
    // System.out.println("X2: " + r2.x + "Y2: " + r2.y + "W2: " + r2.width + "H2: " + r2.height);
    return r1.overlaps(r2);
  }

  private void calculatePosition(Room rFirst, Door dFirst, Room rSecond, Door dSecond) {
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
        rSecondX = rFirstX + rFirst.getRoomDescription().getWidth();
        rSecondY = rFirstY + dFirstY - dSecondY;
        break;
      case North:
        rSecondX = rFirstX + dFirstX - dSecondX;
        rSecondY = rFirstY - rSecond.getRoomDescription().getHeight();
        break;
      case South:
        rSecondX = rFirstX + dFirstX - dSecondX;
        rSecondY = rFirstY + rFirst.getRoomDescription().getHeight();
        break;
      case West:
        rSecondX = rFirstX - rSecond.getRoomDescription().getWidth();
        rSecondY = rFirstY + dFirstY - dSecondY;
        break;
    }
    rSecond.setPosOnMap(new Vector2(rSecondX, rSecondY));
  }

  private TiledMap generateTiledMap() {
    // FIXME throws error when not found
    RoomDescription startRoomDescription = roomDescriptions.stream()
        .filter(r -> r.getName().equals("room0_startzone_10x10.tmx")).findAny().get();


    Room startRoom = new Room(startRoomDescription);
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

    Room rTop = addedRooms.stream().filter(r -> r.getPosOnMap().equals(vTop)).findAny().get();

    Room rLeft = addedRooms.stream().filter(r -> r.getPosOnMap().equals(vLeft)).findAny().get();

    int yOffSet = (int) rTop.getPosOnMap().y * -1;
    int xOffSet = (int) rLeft.getPosOnMap().x * -1;
    addedRooms.forEach(r -> r.setPosOnMap(r.getPosOnMap().add(xOffSet, yOffSet)));

    addedRooms.forEach(r -> {
      int LAYERS = 3;
      for (int l = 0; l < LAYERS; l++) {
        for (int x = (int) r.getPosOnMap().x; x < r.getRoomDescription().getWidth()
            + (int) r.getPosOnMap().x; x++) {
          for (int y = (int) r.getPosOnMap().y; y < r.getRoomDescription().getHeight()
              + (int) r.getPosOnMap().y; y++) {
            int tileID = r.getRoomDescription().getRoomMap().getTileId(x - (int) r.getPosOnMap().x,
                y - (int) r.getPosOnMap().y, l);
            backgroundMap.setTileId(x, y, l, tileID);
          }
        }
      }
    });
    return backgroundMap;
  }



  /**
   * loads the tiledmap and fixes a line in the tmx file
   * 
   * @param tmxFileName
   * @return TiledMap
   * @throws Exception
   */
  private TiledMap loadTiledMap(String tmxFileName) throws Exception {
    // Loads the mapFile into a string, adds width/height property to objectLayer, saves the file
    // again.
    String ref = new File("res/tiledMap/" + tmxFileName).getAbsolutePath();
    String loadFileContentForFix = new String(Files.readAllBytes(Paths.get(ref)));
    loadFileContentForFix =
        loadFileContentForFix.replaceAll("<objectgroup width=\"1\" height=\"1\"", "<objectgroup");
    loadFileContentForFix =
        loadFileContentForFix.replaceAll("<objectgroup", "<objectgroup width=\"1\" height=\"1\"");
    Path path = Paths.get(ref);
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      writer.write(loadFileContentForFix);
    }
    return new TiledMap(ref);
  }

  private ArrayList<RoomDescription> loadRoomDescription(String pathToRooms) throws Exception {
    File roomFolder = new File(pathToRooms);
    File[] allRooms = roomFolder.listFiles();

    ArrayList<RoomDescription> rooms = new ArrayList<>();

    for (int i = 0; i < allRooms.length; i++) {
      File currentRoom = allRooms[i];
      if (currentRoom.getName().endsWith(".tmx")) {
        rooms.add(new RoomDescription(loadTiledMap("/rooms/" + currentRoom.getName()),
            currentRoom.getName()));
      }
    }
    return rooms;
  }
}
