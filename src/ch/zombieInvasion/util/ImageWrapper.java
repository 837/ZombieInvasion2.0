package ch.zombieInvasion.util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageWrapper {
  Image img;

  public ImageWrapper(String data) {
    try {
      img = new Image(data);
    } catch (SlickException e) {
      LOGGER.LOG("Error while creating an ImageWrapper: " + data);
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
