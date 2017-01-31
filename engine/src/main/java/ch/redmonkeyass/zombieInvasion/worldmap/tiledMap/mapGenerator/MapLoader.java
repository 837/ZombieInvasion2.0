package ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.mapGenerator;


import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.worldmap.tiledMap.TiledMap;
import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapLoader {
  protected TiledMap backgroundMap;
  protected TiledMap finishedMap;

  protected Logger logger = LogManager.getLogger(WorldMapLoader.class);

  protected Vector2 startRoomPos = Vector2.Zero;

  public MapLoader(String tmxBackgroundMap) throws Exception {
    backgroundMap = loadTiledMap(tmxBackgroundMap);
  }

  /**
   * Returns the middle of the startzone
   * 
   * @return startzonepos
   */
  public Vector2 getStartPos() {
    return startRoomPos;
  }

  public void setStartRoomPos(Vector2 startRoomPos) {
    this.startRoomPos = startRoomPos;
  }

  /**
   * The random generated tiledmap
   * 
   * @return finished tiledmap
   */
  public TiledMap getFinishedMap() {
    return finishedMap;
  }

  public void setFinishedMap(TiledMap finishedMap) {
    this.finishedMap = finishedMap;
  }


  /**
   * loads the tiledmap and fixes a line in the tmx file
   * 
   * @param tmxFileName
   * @return TiledMap
   * @throws Exception
   */
  protected TiledMap loadTiledMap(String tmxFileName) throws Exception {
    // Loads the mapFile into a string, adds width/height property to objectLayer, saves the file
    // again.
    String ref = new File(Config.RESSOURCE_FOLDER + "tiledmap\\"+tmxFileName).getAbsolutePath();
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

}
