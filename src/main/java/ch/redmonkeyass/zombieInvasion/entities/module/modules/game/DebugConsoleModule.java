package ch.redmonkeyass.zombieInvasion.entities.module.modules.game;

import java.awt.Font;
import java.util.Optional;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.Entity;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.UpdatableModul;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.LightEmitter;

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
      g.setColor(new Color(0, 0, 0, 80));
      g.fillRect(World.getCamera().getPosition().x, World.getCamera().getPosition().y, Config.WIDTH,
          Config.HEIGHT);
      g.setColor(Color.white);
      g.drawString(outPut, World.getCamera().getPosition().x + 20,
          World.getCamera().getPosition().y + 30);
      consoleInputField.setLocation((int) World.getCamera().getPosition().x + 5,
          (int) World.getCamera().getPosition().y + 30);
      consoleInputField.render(gc, g);
    }
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    World.getEntityHandler().getEventsFrom(getEntityID()).ifPresent(events -> {
      events.parallelStream().forEach(e -> {
        switch (e.getEvent()) {
          case DEBUG_CONSOLE_KEY_F1_PRESSED:
            if (consoleInputField == null) {
              Font font = new Font("Verdana", Font.BOLD, 15);
              TrueTypeFont ttf = new TrueTypeFont(font, true);
              consoleInputField =
                  new TextField(gc, ttf, (int) World.getCamera().getPosition().x + 5,
                      (int) World.getCamera().getPosition().y + 30, Config.WIDTH - 10,
                      Config.HEIGHT - 35, new ComponentListener() {

                @Override
                public void componentActivated(AbstractComponent source) {
                  // TODO Auto-generated method stub
                  String[] message = consoleInputField.getText().trim().split(" ");
                  if (message.length >= 3) {
                    if (message[0].equals("add")) {
                      Entity e = World.getEntityHandler().getAllEntities().stream()
                          .filter(e1 -> e1.getID().equals(message[1])).findAny().orElse(null);
                      if (e != null) {
                        switch (message[2]) {
                          case "LightEmitter":
                            World.getModuleHandler().addModules(new LightEmitter(e.getID()));
                            break;

                          default:
                            consoleInputField.setText("WRONG INPUT");
                            break;
                        }
                      }
                    }
                  }


                  consoleInputField.setFocus(true);
                  consoleInputField.setText("");
                }
              });
              consoleInputField.setBorderColor(Color.gray);
              consoleInputField.setAcceptingInput(true);
              consoleInputField.setCursorVisible(true);
              consoleInputField.setFocus(true);
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
