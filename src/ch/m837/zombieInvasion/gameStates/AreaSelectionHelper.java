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
              e.getAdditionalInfo(Vector2.class).ifPresent(position -> {
                position.add(World.getCamera().getPosition());
                area.setLocation(position.x, position.y);
                System.out.println("area start pos: " + position.toString());
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
                System.out.println("area end pos: " + position);
                if (area.getWidth() > 10 || area.getHeight() > 10) {
                  EventDispatcher.createEvent(0, EventType.AREA_SELECTION, area,
                      "AreaSelectionHelper", "GLOBAL");
                } else {
                  EventDispatcher.createEvent(0, EventType.LEFT_CLICK_SELECTION, position.cpy(),
                      "AreaSelectionHelper", "GLOBAL");
                }
              });
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
