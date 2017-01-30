package ch.redmonkeyass.zombieInvasion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

public class TextureLoader {
  private HashMap<String, Texture> textures = new HashMap<>();
  private Logger logger = LogManager.getLogger(TextureLoader.class);

  public TextureLoader(String texturePath) {
    File f = new File(texturePath);
    for (Path img : Utils.getAllFilePathsInDirectory(f.toPath())) {
      try {
        if (img.toString().endsWith(".png") || img.toString().endsWith(".PNG")) {
          // load texture from PNG file
          textures.put(img.getFileName().toString().replaceAll(".png", "").replaceAll(".PNG", ""),
              org.newdawn.slick.opengl.TextureLoader.getTexture("PNG",
                  ResourceLoader.getResourceAsStream(img.toString())));
          continue;
        }
        if (img.toString().endsWith(".jpg") || img.toString().endsWith(".JPG")) {
          // load texture from JPG file
          textures.put(img.getFileName().toString().replaceAll(".jpg", "").replaceAll(".JPG", ""),
              org.newdawn.slick.opengl.TextureLoader.getTexture("JPG",
                  ResourceLoader.getResourceAsStream(img.toString())));
          continue;
        }

      } catch (IOException e) {
        logger.error("Error while loading texture to from res. \n" + e);
      }
    }
  }

  public HashMap<String, Texture> getTextures() {
    return textures;
  }

  public Texture getTextureByName(String key) {
    return textures.getOrDefault(key, textures.get("NOTFOUNDTEXTURE"));
  }
}


