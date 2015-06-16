package ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;

public class MouseSelectionModule extends Module implements RenderableModul, UpdatableModul {
  public MouseSelectionModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  private Rectangle area = new Rectangle(0, 0, 0, 0);
  private final Color areaColor = new Color(96, 96, 96, 80);

  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID()).parallelStream()
        .filter(event -> event.getReceiverID().equals("GLOBAL")).forEach(e -> {
          switch (e.getEvent()) {
            case LEFT_DOWN:
              e.getAdditionalInfo(Vector2.class).ifPresent(position -> {
                position.add(World.getCamera().getPosition());
                area = new Rectangle(position.x, position.y, 0, 0);
                LogManager.getLogger("zombie").trace("area start pos: " + position.toString());
              });
              break;

            case LEFT_DRAGGED:
              e.getAdditionalInfo(Vector2[].class).ifPresent(positions -> {
                positions[1].add(World.getCamera().getPosition());
                calculateArea(positions[1].cpy());
                // EventDispatcher.createEvent(0, EventType.AREA_SELECTION_EVENT, area,
                // "AreaSelectionHelper", "GLOBAL");
              });

              break;
            case LEFT_RELEASED:
              e.getAdditionalInfo(Vector2.class).ifPresent(position -> {
                position.add(World.getCamera().getPosition());
                calculateArea(position.cpy());
                LogManager.getLogger("zombie").trace("area end pos: " + position);
                if (area.getWidth() > 10 || area.getHeight() > 10 || area.getWidth() < -10
                    || area.getHeight() < -10) {

                  Rectangle areaToCheck = new Rectangle(Math.min(area.getMinX(), area.getMaxX()),
                      Math.min(area.getMinY(), area.getMaxY()), Math.abs(area.getWidth()),
                      Math.abs(area.getHeight()));

                  World.getEventDispatcher().createEvent(0, EventType.AREA_SELECTION, areaToCheck,
                      "AreaSelectionHelper", "GLOBAL");
                } else {
                  World.getEventDispatcher().createEvent(0, EventType.LEFT_CLICK_SELECTION, position.cpy(),
                      "AreaSelectionHelper", "GLOBAL");
                }
                area = new Rectangle(0, 0, 0, 0);
              });
              break;
          }
        });
  }

  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    g.setColor(areaColor);
    g.fill(area);
  }

  private void calculateArea(Vector2 newPos) {
    area.setWidth(newPos.cpy().sub(area.getX(), area.getY()).x);
    area.setHeight(newPos.cpy().sub(area.getX(), area.getY()).y);
  }

  @Override
  public Object getData(DataType dataType) {
    // TODO Auto-generated method stub
    return null;
  }
}
