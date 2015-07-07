package ch.redmonkeyass.zombieInvasion.entities.module.modules.game;

import java.awt.Font;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;

public class DebugConsoleModule extends Module implements UpdatableModul, RenderableModul {
  private TextField consoleInputField = null;
  private String outPut = "";

  public DebugConsoleModule(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    if (consoleInputField != null) {
      g.setColor(new Color(0, 0, 0, 50));
      g.drawRect(World.getCamera().getPosition().x + 5, World.getCamera().getPosition().y + 5, 200,
          2000);
      g.setColor(Color.white);
      g.drawString(outPut, World.getCamera().getPosition().x + 20,
          World.getCamera().getPosition().y + 30);
      consoleInputField.render(gc, g);
      System.out.println("asdfdsd");
    }
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID()).ifPresent(events -> {
      events.parallelStream().filter(event -> event.getReceiverID().equals("GLOBAL")
          || event.getReceiverID().equals("DEBUG_CONSOLE")).forEach(e -> {
        switch (e.getEvent()) {
          case DEBUG_CONSOLE_KEY_F1_PRESSED:
            if (consoleInputField == null) {
              Font font = new Font("Verdana", Font.BOLD, 32);
              TrueTypeFont ttf = new TrueTypeFont(font, true);
              consoleInputField =
                  new TextField(gc, ttf, (int) World.getCamera().getPosition().x + 5,
                      (int) World.getCamera().getPosition().y + 5, 200, 20);
            } else {
              consoleInputField = null;
            }
            break;
        }
      });
    });
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

}
