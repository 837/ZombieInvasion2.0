package ch.redmonkeyass.zombieInvasion.util;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entityfactories.EntityType;

public class Images {
  /*
   * Images for Menu
   */
  public static final ImageWrapper MENU_BACKGROUND =
      new ImageWrapper(WorldHandler.getTextures().getTextureByName("wallbg"), EntityType.ZERO);
  
  /*
   * Entities
   */
  public static final ImageWrapper ADOLF =
      new ImageWrapper(WorldHandler.getTextures().getTextureByName("adolf"), EntityType.ADOLF);

  public static final ImageWrapper HANS =
      new ImageWrapper(WorldHandler.getTextures().getTextureByName("hans"), EntityType.HANS);

  public static final ImageWrapper GERHART =
      new ImageWrapper(WorldHandler.getTextures().getTextureByName("gerhart"), EntityType.GERHART);

  public static final ImageWrapper ZOMBIE =
      new ImageWrapper(WorldHandler.getTextures().getTextureByName("zombie"), EntityType.ZOMBIE);

  public static final ImageWrapper CIRCULAR_LIGHT =
      new ImageWrapper(WorldHandler.getTextures().getTextureByName("CircleLight"), EntityType.ZERO);

  public static final ImageWrapper TRANSPARENT =
      new ImageWrapper(WorldHandler.getTextures().getTextureByName("transparent"), EntityType.ZERO);



}
