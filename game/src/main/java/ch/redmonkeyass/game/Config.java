package ch.redmonkeyass.game;

public class Config {
  // Game state identifiers
  public static final int SPLASHSCREEN = 0;
  public static final int MAINMENU = 1;
  public static final int GAME = 2;

  // Application Properties
  public static final int WIDTH = 1920;
  public static final int HEIGHT = 1080;
  public static final int FPS = 60;
  public static final double VERSION = 0.9;
  public static final float TICKS_PER_SECOND = 30;
  public static final double TIME_PER_TICK = 1000 / TICKS_PER_SECOND;
  public static final int MAX_FRAMESKIP = 5;

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
  public static final float MOUSE_DRAG_SMOOTHNESS = 2.5f;


  // WorldMap
  public static final String WORLDMAP_NAME = "200x200Background.tmx";
  public static final int MAX_ROOMS = 30;

  //Waves
  public static final String WAVES_FILE_NAME = "wavesXML.xml";
  
  //res
  public static final String RESSOURCE_FOLDER = System.getProperty("user.dir")+"/res";
}
