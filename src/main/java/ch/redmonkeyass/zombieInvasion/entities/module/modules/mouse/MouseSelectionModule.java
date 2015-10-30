package ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;

/**
 * MouseSelectionModule gives a mouse the ability to send selection Events to entities.
 * <p>
 * <b>Needs:</b> <br>
 * EventType.LEFT_DOWN,<br>
 * EventType.LEFT_DRAGGED,<br>
 * EventType.LEFT_RELEASED<br>
 * 
 * @author Matthias
 *
 */
public class MouseSelectionModule extends Module implements RenderableModul, UpdatableModul {
  private final Color areaColor = new Color(96, 96, 96, 80);
  private Rectangle area = new Rectangle(0, 0, 0, 0);

  public MouseSelectionModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getEventsFrom(getEntityID()).ifPresent(events -> {
      events.forEach(e -> {
        switch (e.getEvent()) {
          case LEFT_DOWN:
            e.getAdditionalInfo(Vector2.class).ifPresent(position -> {
              position.add(WorldHandler.getCamera().getPosition());
              area = new Rectangle(position.x, position.y, 0, 0);
              LogManager.getLogger("zombie").trace("area start pos: " + position.toString());
            });
            break;

          case LEFT_DRAGGED:
            e.getAdditionalInfo(Vector2[].class).ifPresent(positions -> {
              positions[1].add(WorldHandler.getCamera().getPosition());
              calculateArea(positions[1].cpy());
            });

            break;
          case LEFT_RELEASED:
            e.getAdditionalInfo(Vector2.class).ifPresent(position -> {
              position.add(WorldHandler.getCamera().getPosition());
              calculateArea(position.cpy());
              LogManager.getLogger("zombie").trace("area end pos: " + position);
              if (area.getWidth() > 10 || area.getHeight() > 10 || area.getWidth() < -10
                  || area.getHeight() < -10) {

                Rectangle areaToCheck = new Rectangle(Math.min(area.getMinX(), area.getMaxX()),
                    Math.min(area.getMinY(), area.getMaxY()), Math.abs(area.getWidth()),
                    Math.abs(area.getHeight()));

                WorldHandler.getEventDispatcher().createEvent(0, EventType.AREA_SELECTION,
                    areaToCheck, "AreaSelectionHelper", "GLOBAL");
              } else {
                WorldHandler.getEventDispatcher().createEvent(0, EventType.LEFT_CLICK_SELECTION,
                    position.cpy(), "AreaSelectionHelper", "GLOBAL");
              }
              area = new Rectangle(0, 0, 0, 0);
            });
            break;
        }
      });
    });
  }

  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    g.setColor(areaColor);
    if(area.getMinX()!=0 && area.getMinY()!=0) {
      g.fill(area);
    }
  }

  private void calculateArea(Vector2 newPos) {
    area.setWidth(newPos.cpy().sub(area.getX(), area.getY()).x);
    area.setHeight(newPos.cpy().sub(area.getX(), area.getY()).y);
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }
}
