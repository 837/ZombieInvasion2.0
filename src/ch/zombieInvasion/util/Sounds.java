package ch.zombieInvasion.util;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {
	public static Sound backGroundMusic1;

	static {
		try {
			backGroundMusic1 = new Sound("res/sounds/bgm1.ogg");

		} catch (final SlickException ex) {
			LOGGER.LOG("Failed to create Sounds instance in static block.");
			throw new RuntimeException("Failed to create Sounds instance in static block.", ex);
		}
	}
}
