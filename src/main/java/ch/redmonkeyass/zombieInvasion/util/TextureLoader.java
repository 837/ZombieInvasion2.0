package ch.redmonkeyass.zombieInvasion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

public class TextureLoader {
  private HashMap<String, Texture> textures = new HashMap<>();

  public TextureLoader(String texturePath) {
    File f = new File(texturePath);
    for (Path img : Utils.getAllFilePathsInDirectory(f.toPath())) {
      String[] splitedName = img.getFileName().toString().split(".");
      if (splitedName.length > 0) {
        if (splitedName[splitedName.length - 1].matches("\\.(?i:)(?:png|PNG)$")) {
          try {
            // load texture from PNG file
            textures.put(texturePath, org.newdawn.slick.opengl.TextureLoader.getTexture("PNG",
                ResourceLoader.getResourceAsStream("res/image.png")));
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          System.out.println(img);
        }
      }
    }
  }



}
