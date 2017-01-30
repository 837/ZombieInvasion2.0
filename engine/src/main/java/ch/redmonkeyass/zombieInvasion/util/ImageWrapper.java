package ch.redmonkeyass.zombieInvasion.util;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.entityfactories.EntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;

public class ImageWrapper {
  Image img;
  Image b2dScaledImg;
  private Logger logger = LogManager.getLogger(ImageWrapper.class);


  public ImageWrapper(String data, EntityType entity) {
    try {
      img = new Image(data);
      float scaleX = 1 / (img.getWidth() / (entity.getWidth() * Config.B2PIX));
      float scaleY = 1 / (img.getHeight() / (entity.getHeight() * Config.B2PIX));
      if (scaleX != scaleY) {
        logger.error("Image scaling error: Width/Height have not the same scaling factor. Entity["
            + entity.name() + "]");
      }
      float scale = scaleX;
      b2dScaledImg = img.getScaledCopy(scale);
    } catch (SlickException e) {
      logger.error("Error while creating an ImageWrapper: " + data);
    }
  }
  public ImageWrapper(Texture data, EntityType entity) {
    img = new Image(data);
    float scaleX = 1 / (img.getWidth() / (entity.getWidth() * Config.B2PIX));
    float scaleY = 1 / (img.getHeight() / (entity.getHeight() * Config.B2PIX));
    if (scaleX != scaleY) {
      logger.error("Image scaling error: Width/Height have not the same scaling factor. Entity["
          + entity.name() + "]");
    }
    float scale = scaleX;
    b2dScaledImg = img.getScaledCopy(scale);
  }
  /**
   * 
   * @return this img data;
   */
  public Image getScaled() {
    return img;
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
