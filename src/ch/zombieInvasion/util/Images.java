package ch.zombieInvasion.util;

import ch.m837.zombieInvasion.entities.entityFactories.EntityType;

public class Images {
  /*
   * Images for Menu
   */
  public static final ImageWrapper MENU_BACKGROUND =
      new ImageWrapper("res/sprites/menu/background/wallbg.jpg", EntityType.ZERO);

  /*
   * Entities
   */
  public static final ImageWrapper ADOLF =
      new ImageWrapper("res/sprites/entities/testEntity1.png", EntityType.ADOLF);

  public static final ImageWrapper HANS =
      new ImageWrapper("res/sprites/entities/testEntity2.png", EntityType.HANS);

  public static final ImageWrapper GERHART =
      new ImageWrapper("res/sprites/entities/testEntity3.png", EntityType.GERHART);

  public static final ImageWrapper ZOMBIE =
      new ImageWrapper("res/sprites/entities/testEntity4.png", EntityType.ZOMBIE);
}
