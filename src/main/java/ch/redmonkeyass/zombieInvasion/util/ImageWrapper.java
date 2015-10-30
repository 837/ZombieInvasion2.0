package ch.redmonkeyass.zombieInvasion.util;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityType;

public class ImageWrapper {
  Texture texture;
  private Logger logger = LogManager.getLogger(ImageWrapper.class);
  private EntityType entity;

  public ImageWrapper(String data, EntityType entity) {
    this.entity = entity;
    try {
      texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(data));

    } catch (IOException e) {
      logger.error("Error while creating an ImageWrapper: " + data);
    }
  }

  public Texture getTexture() {
    return texture;
  }
}
