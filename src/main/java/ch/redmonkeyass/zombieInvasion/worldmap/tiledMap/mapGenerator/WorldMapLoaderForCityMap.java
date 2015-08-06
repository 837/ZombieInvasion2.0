package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class WorldMapLoaderForCityMap extends MapLoader {
  private ArrayList<MapPart> addedMapParts = new ArrayList<>();
  private ArrayList<MapPartDescription> mapPartsDescriptions = new ArrayList<>();

  public WorldMapLoaderForCityMap(String tmxBackgroundMap) throws Exception {
    super(tmxBackgroundMap);
    mapPartsDescriptions = loadMapDescription(tmxBackgroundMap);
  }

  
  

  private ArrayList<MapPartDescription> loadMapDescription(String pathToMaps) throws Exception {
    File roomFolder = new File(pathToMaps);
    File[] allMaps = roomFolder.listFiles();

    ArrayList<MapPartDescription> rooms = new ArrayList<>();

    for (int i = 0; i < allMaps.length; i++) {
      File currentRoom = allMaps[i];
      if (currentRoom.getName().endsWith(".tmx")) {
        rooms.add(new MapPartDescription(loadTiledMap("/houses/" + currentRoom.getName()),
            currentRoom.getName()));
        // logger.trace("Added room: " + currentRoom.getName());
      }
    }
    return rooms;
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
}
