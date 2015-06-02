package ch.m837.zombieInvasion;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.gameStates.Game;
import ch.m837.zombieInvasion.gameStates.MainMenu;
import ch.zombieInvasion.util.LOGGER;

public class ZombieInvasion extends StateBasedGame {
  // Class Constructor
  public ZombieInvasion(String appName) {
    super(appName);
  }

  // Initialize your game states (calls init method of each gamestate, and set's the state ID)
  public void initStatesList(GameContainer gc) throws SlickException {
    // The first state added will be the one that is loaded first, when the application is launched
    // this.addState(new SplashScreen(SPLASHSCREEN));
    this.addState(new MainMenu(Config.MAINMENU));
    this.addState(new Game(Config.GAME));
  }

  public static void main(String[] args) {
    try {
      AppGameContainer app = new AppGameContainer(new ZombieInvasion(
          ZombieInvasion.class.getSimpleName() + "2.0 [Version : " + Config.VERSION + "]"));
      app.setDisplayMode(Config.WIDTH, Config.HEIGHT, false);
      app.setTargetFrameRate(Config.FPS);
      app.setShowFPS(true);
      app.setClearEachFrame(true);
      app.start();
    } catch (SlickException ex) {
      LOGGER.LOG(ZombieInvasion.class.getSimpleName() + " : " + ex);
    }
  }
}
