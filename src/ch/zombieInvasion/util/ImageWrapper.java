package ch.zombieInvasion.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import ch.m837.zombieInvasion.Config;
import ch.m837.zombieInvasion.entities.entityFactories.EntityType;

public class ImageWrapper {
  Image img;
  Image b2dScaledImg;
  private Logger logger = LogManager.getLogger(ImageWrapper.class);

  public ImageWrapper(String data, EntityType entity) {
    try {
      img = new Image(data);
      b2dScaledImg = img.getScaledCopy((int) (Config.B2PIX * entity.getWidth()),
          (int) (Config.B2PIX * entity.getHeight()));
    } catch (SlickException e) {
      logger.error("Error while creating an ImageWrapper: " + data);
    }
  }

  /**
   * 
   * @return this img data;
   */
  public Image get() {
    return img;
  }

  /**
   * 
   * @return getB2D Scaled Img;
   */
  public Image getB2DScaled() {
    return b2dScaledImg;
  }

  /**
   * 
   * @return img.getWidth() / 2;
   */
  public float getRadiusW() {
    return img.getWidth() / 2;
  }

  /**
   * 
   * @return img.getHeight() / 2;
   */
  public float getRadiusH() {
    return img.getHeight() / 2;
  }
}
