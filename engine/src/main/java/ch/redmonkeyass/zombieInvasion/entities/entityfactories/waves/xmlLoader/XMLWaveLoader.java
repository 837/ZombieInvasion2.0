package ch.redmonkeyass.zombieInvasion.entities.entityfactories.waves.xmlLoader;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityBuilder;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityType;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.waves.Wave;
import ch.redmonkeyass.zombieInvasion.util.MathUtil;

public class XMLWaveLoader {
  private ArrayList<Wave> waves = new ArrayList<>();
  private Logger logger = LogManager.getLogger("Factories");

  public ArrayList<Wave> getWaves() {
    return waves;
  }

  public XMLWaveLoader(File waveXMLFile) {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(waveXMLFile);

      // optional, but recommended
      // read this -
      // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
      doc.getDocumentElement().normalize();
      NodeList wavesNodes = doc.getElementsByTagName("wave");
      for (int waveID = 0; waveID < wavesNodes.getLength(); waveID++) {

        Node waveNode = wavesNodes.item(waveID);

        if (waveNode.getNodeType() == Node.ELEMENT_NODE) {

          Element wave = (Element) waveNode;
          Wave currentWave = new Wave(wave.getAttribute("name"));

          NodeList entities = wave.getElementsByTagName("entity");
          for (int entityID = 0; entityID < entities.getLength(); entityID++) {
            Node entityNode = entities.item(entityID);

            if (entityNode.getNodeType() == Node.ELEMENT_NODE) {
              Element entity = (Element) entityNode;


              EntityBuilder currentBuilder = EntityBuilder.createBuilder(
                  EntityType.valueOf(entity.getElementsByTagName("type").item(0).getTextContent()));

              currentBuilder.numOfEntitiesToSpawn(Integer.valueOf(
                  entity.getElementsByTagName("numOfEntitiesToSpawn").item(0).getTextContent()));

              String startPos =
                  entity.getElementsByTagName("startpos").item(0).getTextContent().trim();
              /*
               * FIXME: Not usable yet, because the map gets randomly generated
               * 
               * currentBuilder.startPosition(new
               * Vector2(Float.valueOf(startPos.split(",")[0].trim()),
               * Float.valueOf(startPos.split(",")[1].trim())));
               */

              switch (startPos) {
                case "StartZone":
                  currentBuilder.startPosition(
                      WorldHandler.getWorldMap().getWorldMapLoader().getStartRoomPos());
                  break;
                case "Random":
                  ch.redmonkeyass.zombieInvasion.worldmap.Node n =
                      WorldHandler.getWorldMap().getAllWalkableNodes().stream()
                          .skip(MathUtil.randomInt(0,
                              WorldHandler.getWorldMap().getAllWalkableNodes().size() - 2))
                      .findAny().get();
                  currentBuilder.startPosition(new Vector2(n.x * 2, n.y * 2));
                  break;
                default:
                  currentBuilder.startPosition(
                      WorldHandler.getWorldMap().getWorldMapLoader().getStartRoomPos());
                  break;
              }


              currentWave.getEntityBuilders().add(currentBuilder);
            }
          }
          waves.add(currentWave);
        }
      }
    } catch (Exception e) {
      logger.error("Error loading wavesXML file. ", e);
    }
  }

}
