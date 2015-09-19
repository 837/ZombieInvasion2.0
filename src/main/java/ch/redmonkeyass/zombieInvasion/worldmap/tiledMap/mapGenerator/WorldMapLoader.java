package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.TiledMap;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator.UNFINISHED.WorldMapLoaderForCityMap;

public class WorldMapLoader {
  private Logger logger = LogManager.getLogger(WorldMapLoader.class);

  public enum MAP_LOADER_TYPE {
    RANDOM_ROOM_MAP, CITY_BUILDING_MAP;
  }

  private MapLoader mapLoader;

  public WorldMapLoader(String tmxBackGroundMapFile, MAP_LOADER_TYPE mapLoaderType) throws Exception {
    switch (mapLoaderType) {
      case CITY_BUILDING_MAP:
        logger.trace("Selected MapLoader: CITY_BUILDING_MAP");
         mapLoader = new WorldMapLoaderForCityMap(tmxBackGroundMapFile);
        break;
      case RANDOM_ROOM_MAP:
        logger.trace("Selected MapLoader: RANDOM_ROOM_MAP");
        mapLoader = new WorldMapLoaderForRandomGeneratedRoomsMap(tmxBackGroundMapFile);
        break;
      default:
        logger.trace("Not handled map loader type! Can't create a map!");
        break;

    }
  }

  public TiledMap getFinishedMap() {
    if (mapLoader.getFinishedMap() == null) {
      System.out.println("map is null!!!!!!!!");
    }
    return mapLoader.getFinishedMap();
  }

  public Vector2 getStartRoomPos() {
    return mapLoader.getStartPos();
  }
}
