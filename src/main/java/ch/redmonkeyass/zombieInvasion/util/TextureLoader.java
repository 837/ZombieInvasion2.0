package ch.redmonkeyass.zombieInvasion.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
 
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
public class TextureLoader {
  private HashMap<String, Texture> textures = new HashMap<>();

  public TextureLoader(String texturePath) {
    init();
    loop();
    File f = new File(texturePath);
    for (Path img : Utils.getAllFilePathsInDirectory(f.toPath())) {

      if (img.toString().endsWith(".png") || img.toString().endsWith(".PNG")) {
        try {
          // load texture from PNG file
          textures.put(texturePath, org.newdawn.slick.opengl.TextureLoader.getTexture("PNG",
              ResourceLoader.getResourceAsStream(img.toString())));
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        System.out.println(img);
      }
    }
  }
  
//We need to strongly reference callback instances.
  private GLFWErrorCallback errorCallback;
  private GLFWKeyCallback   keyCallback;
  // The window handle
  private long window;
  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if ( glfwInit() != GL11.GL_TRUE )
        throw new IllegalStateException("Unable to initialize GLFW");

    // Configure our window
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

    int WIDTH = 300;
    int HEIGHT = 300;

    // Create the window
    window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
    if ( window == NULL )
        throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
        }
    });

    // Get the resolution of the primary monitor
    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    // Center our window
    glfwSetWindowPos(
        window,
        (vidmode.getWidth() - WIDTH) / 2,
        (vidmode.getHeight() - HEIGHT) / 2
    );

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
}
  private void loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    // Set the clear color
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
//    while ( glfwWindowShouldClose(window) == GL_FALSE ) {
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
//
//        glfwSwapBuffers(window); // swap the color buffers
//
//        // Poll for window events. The key callback above will only be
//        // invoked during this call.
//        glfwPollEvents();
//    }
}
}


