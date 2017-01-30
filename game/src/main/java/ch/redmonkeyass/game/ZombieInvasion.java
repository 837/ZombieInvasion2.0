package ch.redmonkeyass.game;


import ch.redmonkeyass.game.gamestates.Game;
import ch.redmonkeyass.game.gamestates.MainMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ZombieInvasion extends StateBasedGame {
  private static Logger logger = LogManager.getLogger(ZombieInvasion.class);

  // Class Constructor
  public ZombieInvasion(String appName) {
    super(appName);
  }

  public static void main(String[] args) {
    try {
      AppGameContainer app = new AppGameContainer(new ZombieInvasion(
          ZombieInvasion.class.getSimpleName() + "2.0 [Version : " + Config.VERSION + "]"));
      app.setDisplayMode(Config.WIDTH, Config.HEIGHT, false);
      // app.setTargetFrameRate(Config.FPS);
      app.setShowFPS(true);
      app.setClearEachFrame(true);
      app.start();
    } catch (Exception ex) {
      logger.error(ex);
      ex.printStackTrace();
    }
  }

  // Initialize your game states (calls init method of each gamestate, and set's the state ID)
  public void initStatesList(GameContainer gc) throws SlickException {
    // The first state added will be the one that is loaded first, when the application is launched
    // this.addState(new SplashScreen(SPLASHSCREEN));
    this.addState(new MainMenu(Config.MAINMENU));
    this.addState(new Game(Config.GAME));
  }
}
