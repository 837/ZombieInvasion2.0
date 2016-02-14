package ch.redmonkeyass.zombieInvasion.entities.module.modules.game;

import java.awt.Font;
import java.lang.reflect.Constructor;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
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
      g.setColor(new Color(0, 0, 255, 80));
      g.fillRect(WorldHandler.getCamera().getPosition().x, WorldHandler.getCamera().getPosition().y,
          Config.WIDTH, Config.HEIGHT);
      g.setColor(Color.white);
      g.drawString(outPut, WorldHandler.getCamera().getPosition().x + 20,
          WorldHandler.getCamera().getPosition().y + 90);
      consoleInputField.setLocation((int) WorldHandler.getCamera().getPosition().x + 5,
          (int) WorldHandler.getCamera().getPosition().y + 30);
      consoleInputField.render(gc, g);
    }
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    WorldHandler.getEntityHandler().getEventsFrom(getEntityID())
        .ifPresent(events -> events.parallelStream().forEach(e -> {
          switch (e.getEvent()) {
            case DEBUG_CONSOLE_KEY_F1_PRESSED:
              if (consoleInputField == null) {
                Font font = new Font("Verdana", Font.BOLD, 15);
                TrueTypeFont ttf = new TrueTypeFont(font, true);
                consoleInputField =
                    new TextField(gc, ttf, (int) WorldHandler.getCamera().getPosition().x + 5,
                        (int) WorldHandler.getCamera().getPosition().y + 30, Config.WIDTH - 10, 60,
                        componentListener -> {


                  String[] message = consoleInputField.getText().trim().split(" ");
                  switch (message.length) {
                    case 0:
                      outPut += "try help\n";
                      break;
                    case 1:
                      switch (message[0]) {
                        case "clear":
                          outPut = "";
                          break;
                        case "help":
                          outPut = "";
                          outPut += "***GENERAL***\n" + "help -- displays help\n"
                              + "clear -- clears the output\n" + "***ENTITIES***\n"
                              + "add -- add ENTITY_ID MODULENAME\n"
                              + "remove -- remove ENTITY_ID MODULENAME\n" + "***WAVES***\n"
                              + "setWave -- setWave WAVE_NUMBER\n";
                          break;
                        default:
                          outPut += "[" + message[0] + "] not found, try help\n";
                          break;
                      }
                      consoleInputField.setText("");
                      break;
                    case 2:
                      switch (message[0]) {
                        case "setWave":
                          try {
                            WorldHandler.getEventDispatcher().createEvent(0, EventType.SET_WAVE,
                                Integer.valueOf(message[1]), "GAME", "GLOBAL");
                            outPut += "Set Wave to: [" + message[1] + "] if it exists.\n";
                          } catch (Exception e2) {
                            outPut += "Input couldn't be handled. [" + message[1]
                                + " is not an Integer]\n";
                          }
                          break;

                        default:
                          outPut += "[" + message[0] + "] not found, try help\n";
                          break;
                      }
                    case 3:
                      /*
                       * preCond: message format <action> <entity name> <additional info> <action>
                       * available: add, remove if <additional info> is a class it must be in
                       * ch.redmonkeyass.zombieInvasion.entities.module.modules and Constructor must
                       * have exactly one Parameter of tpye String example: add HANS LightEmitter
                       */
                      WorldHandler.getEntityHandler().getAllEntities().stream()
                          .filter(e1 -> e1.getID().equals(message[1])).findAny()
                          .ifPresent(entity -> {
                        try {
                          if (message[0].equals("add") || message[0].equals("remove")) {
                            Class<Module> clazz;
                            Constructor<Module> constructtoni;

                            clazz = (Class<Module>) Class
                                .forName("ch.redmonkeyass.zombieInvasion.entities.module.modules."
                                    + message[2]);
                            switch (message[0]) {
                              case "add":
                                constructtoni = clazz.getConstructor(String.class);

                                WorldHandler.getModuleHandler()
                                    .addModules(constructtoni.newInstance(entity.getID()));
                                outPut +=
                                    "Added to: " + entity.getID() + " the module: " + message[2];
                                break;
                              case "remove":
                                WorldHandler.getModuleHandler().removeModulesFrom(entity.getID(),
                                    clazz);
                                outPut += "Removed from: " + entity.getID() + " the module: "
                                    + message[2];
                                break;
                              default:
                                break;
                            }
                          } else {
                            outPut += "wrong input";
                          }
                        } catch (ClassNotFoundException notFound) {
                          LogManager.getLogger("zombie").debug("class couldn't be found", notFound);
                          outPut += "Class couldn't be found";
                        } catch (ClassCastException badClass) {
                          LogManager.getLogger("zombie").debug("tried to load bad class", badClass);
                          outPut += "Tried to load bad class";
                        } catch (Exception ex) {
                          LogManager.getLogger("zombie").debug("input couldn't be handled", ex);
                          outPut += "Input couldn't be handled";
                        }
                        outPut += "\n";
                      });
                      consoleInputField.setText("");
                      break;
                  }
                  consoleInputField.setFocus(true);
                });
                consoleInputField.setBorderColor(Color.gray);
                consoleInputField.setAcceptingInput(true);
                consoleInputField.setCursorVisible(true);
                consoleInputField.setFocus(true);
              } else {
                consoleInputField.deactivate();
                consoleInputField.inputEnded();
                consoleInputField = null;
              }
              break;
          }
        }));
  }

  @Override
  public Optional<Object> getData(DataType dataType) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

}
