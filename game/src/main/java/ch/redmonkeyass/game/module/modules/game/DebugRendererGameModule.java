package ch.redmonkeyass.game.module.modules.game;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Optional;

/**
 * DebugRendererGameModule is exactly what it sounds like. Renders all the debug stuff. put in it
 * whatever you want to draw.
 * 
 * @author Matthias
 *
 */
public class DebugRendererGameModule extends Module implements RenderableModul {

  public DebugRendererGameModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    WorldHandler.getEntityHandler().getDataFrom("MOUSE", DataType.MOUSE_SELECTED_NODE, Node.class)
        .ifPresent(selectedNode -> {
          g.setColor(Color.white);
          g.drawRect(
              selectedNode.getX()
                  * (Config.B2PIX * WorldHandler.getWorldMap().getNodeSizeInMeter()),
              selectedNode.getY()
                  * (Config.B2PIX * WorldHandler.getWorldMap().getNodeSizeInMeter()),
              selectedNode.getTileSize(), selectedNode.getTileSize());
          g.drawString("SelectedNodeType: " + selectedNode.getType(),
              10 + WorldHandler.getCamera().getPosition().x,
              30 + WorldHandler.getCamera().getPosition().y);
          g.drawString(
              "SelectedNodePos: [" + selectedNode.getX() + ", " + selectedNode.getY() + "]",
              10 + WorldHandler.getCamera().getPosition().x,
              45 + WorldHandler.getCamera().getPosition().y);
          g.drawString("SelectedNode has body: [" + (selectedNode.getBody() != null) + "]",
              10 + WorldHandler.getCamera().getPosition().x,
              60 + WorldHandler.getCamera().getPosition().y);
          g.drawString(
              "Camera position: [" + WorldHandler.getCamera().getPosition().toString() + "]",
              10 + WorldHandler.getCamera().getPosition().x,
              80 + WorldHandler.getCamera().getPosition().y);
        });
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

}
