package ch.m837.zombieInvasion;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import ch.zombieInvasion.util.LOGGER;

public class ZombieInvasion extends StateBasedGame {

  // Game state identifiers
  public static final int SPLASHSCREEN = 0;
  public static final int MAINMENU = 1;
  public static final int GAME = 2;

  // Application Properties
  public static final int WIDTH = 1366;
  public static final int HEIGHT = 768;
  public static final int FPS = 60;
  public static final double VERSION = 1.0;

  // Class Constructor
  public ZombieInvasion(String appName) {
    super(appName);
  }

  // Initialize your game states (calls init method of each gamestate, and set's the state ID)
  public void initStatesList(GameContainer gc) throws SlickException {
    // The first state added will be the one that is loaded first, when the application is launched
    // this.addState(new SplashScreen(SPLASHSCREEN));
    this.addState(new MainMenu(MAINMENU));
    this.addState(new Game(GAME));
  }

  public static void main(String[] args) {
    try {
      AppGameContainer app = new AppGameContainer(new ZombieInvasion(
          ZombieInvasion.class.getSimpleName() + " [Version : " + VERSION + "]"));
      app.setDisplayMode(WIDTH, HEIGHT, false);
      app.setTargetFrameRate(FPS);
      app.setShowFPS(true);
      app.setClearEachFrame(true);
      app.start();
    } catch (SlickException ex) {
      LOGGER.LOG(ZombieInvasion.class.getSimpleName() + " : " + ex);
    }
  }
}
