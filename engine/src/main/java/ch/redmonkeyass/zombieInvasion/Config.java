package ch.redmonkeyass.zombieInvasion;

import com.typesafe.config.ConfigFactory;

public class Config {
	public static final com.typesafe.config.Config conf = ConfigFactory.load("ZombieInvasion");


	public Config() {
		// Game state identifiers
		//final int SPLASHSCREEN = conf.getInt("states.SPLASHSCREEN");
		//final int MAINMENU = conf.getInt("states.MAINMENU");
		//final int GAME = conf.getInt("states.GAME");
	}

	static {
	}

	// Game state identifiers
	public static final int SPLASHSCREEN = conf.getInt("states.SPLASHSCREEN");
	public static final int MAINMENU = conf.getInt("states.MAINMENU");
	public static final int GAME = conf.getInt("states.GAME");

	// Application Properties
	public static final int WIDTH = conf.getInt("application.WIDTH");
	public static final int HEIGHT = conf.getInt("application.WIDTH");
	public static final int FPS = conf.getInt("application.FPS");
	public static final float VERSION = (float) conf.getDouble("application.VERSION");
	public static final float TICKS_PER_SECOND = (float) conf.getDouble("application.TICKS_PER_SECOND");
	public static final float TIME_PER_TICK = 1000f / (float) TICKS_PER_SECOND;
	public static final int MAX_FRAMESKIP = conf.getInt("application.MAX_FRAMESKIP");

	// Box2D Config(??)
  /*
   * temporary, there has to be a more sensible way to define this
   */
	public static final float B2PIX = 32; // Should be constant over all development
	public static final float PIX2B = 1 / B2PIX;

	// Camera
	public static final int CAM_VIEWPORT_WIDTH = WIDTH;
	public static final int CAM_VIEWPORT_HEIGHT = HEIGHT;

	// Input
	public static final float MOUSE_DRAG_SMOOTHNESS = (float) conf.getDouble("input.MOUSE_DRAG_SMOOTHNESS");


	// WorldMap
	public static final String WORLDMAP_NAME = "200x200Background.tmx";
	public static final int MAX_ROOMS = 30;

	//Waves
	public static final String WAVES_FILE_NAME = "wavesXML.xml";

	//res
	public static final String RESSOURCE_FOLDER = System.getProperty("user.dir") + "\\..\\engine\\src\\main\\resources\\";


}
