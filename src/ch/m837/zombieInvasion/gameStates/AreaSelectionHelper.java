package ch.m837.zombieInvasion.gameStates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.m837.zombieInvasion.World;
import ch.zombieInvasion.Eventhandling.EventDispatcher;
import ch.zombieInvasion.Eventhandling.EventType;

public class AreaSelectionHelper {
  private Rectangle area = new Rectangle(0, 0, 0, 0);

  private Vector2 widthHeightOfArea = Vector2.Zero;

  private Color areaColor = new Color(96, 96, 96, 80);


  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    EventDispatcher.getEvents().stream().filter(event -> event.getReceiverID().equals("GLOBAL"))
        .forEach(e -> {
          switch (e.getEvent()) {
            case LEFT_DOWN:
              Vector2 positionDown = ((Vector2) e.getAdditionalInfo());
              positionDown.add(World.getCamera().getPosition());
              area.setLocation(positionDown.x, positionDown.y);
              System.out.println("area start pos: " + positionDown.toString());
              break;

            case LEFT_DRAGGED:
              Vector2 positionDrag = ((Vector2[]) e.getAdditionalInfo())[0];
              positionDrag.add(World.getCamera().getPosition());
              calculateArea(positionDrag.cpy());
              // EventDispatcher.createEvent(0, EventType.AREA_SELECTION_EVENT, area,
              // "AreaSelectionHelper", "GLOBAL");

              break;
            case LEFT_RELEASED:
              Vector2 positionReleased = ((Vector2) e.getAdditionalInfo());
              positionReleased.add(World.getCamera().getPosition());
              calculateArea(positionReleased.cpy());
              System.out.println("area end pos: " + positionReleased);
              if (area.getWidth() > 10 || area.getHeight() > 10) {
                EventDispatcher.createEvent(0, EventType.AREA_SELECTION, area,
                    "AreaSelectionHelper", "GLOBAL");
              } else {
                EventDispatcher.createEvent(0, EventType.LEFT_CLICK_SELECTION, positionReleased.cpy(),
                    "AreaSelectionHelper", "GLOBAL");

              }
              area = new Rectangle(0, 0, 0, 0);
              break;
          }
        });
  }

  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    g.setColor(areaColor);
    g.fill(area);
  }

  private void calculateArea(Vector2 newPos) {
    widthHeightOfArea = widthHeightOfArea.set(newPos.sub(area.getX(), area.getY()));
    area.setWidth(widthHeightOfArea.x);
    area.setHeight(widthHeightOfArea.y);
  }
}
