package ch.redmonkeyass.zombieInvasion.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {
  public static Sound backGroundMusic1;

  private static Logger logger = LogManager.getLogger(Sounds.class);

  static {
    try {
      backGroundMusic1 = new Sound("res/sounds/bgm1.ogg");

    } catch (final SlickException ex) {
      logger.error("Failed to create Sounds instance in static block.");
      throw new RuntimeException("Failed to create Sounds instance in static block.", ex);
    }
  }
}
