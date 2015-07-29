package ch.redmonkeyass.zombieInvasion.util;

import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.GL11.*;

/**
 * provides functionality to
 * -load shaderprograms
 * -create FBO with textures
 * Created by P on 28.07.2015.
 */
public class GL_Helper {

  public static int generateFrameBufferObject() {
    boolean FBOEnabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
    if (!FBOEnabled) throw new UnsupportedOperationException("cannot create fbo, please update Graphics Card drivers");

    IntBuffer buffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); // allocate a 1 int byte buffer
    EXTFramebufferObject.glGenFramebuffersEXT(buffer); // generate

    return buffer.get();
  }

  public static void renderToFBO(int FBO_ID, int width, int height) {
    // prepare FBO render pass *********************
    EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, FBO_ID);
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    glViewport(0, 0, width, height);  // set The Current Viewport to the fbo size

    //glBindTexture(GL_TEXTURE_2D, 0);  // unlink textures because if we dont it all is gonna fail
    //glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, FBO_ID);   // switch to rendering on our FBO

    //glLoadIdentity();

    //ready to render naow *********************
  }

  public static void stopRenderingToFBO() {
    EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
    GL11.glPopAttrib();
  }

  /**
   * loads a shader program from file
   *
   * @param vertPath vertex shader
   * @param fragPath fragment shader
   * @return a valid programID
   * <p>
   * shaderprogram can be used like this
   * if(shaderLoadedCorrectly)
   * ARBShaderObjects.glUseProgramObjectARB(program);
   * shaders must be released after use
   * if(shaderLoadedCorrectly)
   * ARBShaderObjects.glUseProgramObjectARB(0);
   */
  public static int loadShaderProgram(String vertPath, String fragPath) {
    int vertShader = 0, fragShader = 0;
    int program;
    try {
      vertShader = createShader(vertPath, ARBVertexShader.GL_VERTEX_SHADER_ARB);
      fragShader = createShader(fragPath, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    } finally {
      if (vertShader == 0 || fragShader == 0)
        throw new IllegalArgumentException("couldn't load shaders");
    }

    program = ARBShaderObjects.glCreateProgramObjectARB();

    if (program == 0) throw new UnsupportedOperationException("couln't create program");

        /*
        * if the vertex and fragment shaders setup sucessfully,
        * attach them to the shader program, link the sahder program
        * (into the GL context I suppose), and validate
        */
    ARBShaderObjects.glAttachObjectARB(program, vertShader);
    ARBShaderObjects.glAttachObjectARB(program, fragShader);

    ARBShaderObjects.glLinkProgramARB(program);
    if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
      System.err.println(ARBShaderObjects.glGetInfoLogARB(program, ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));
      throw new UnsupportedOperationException("couldn't link shaderprogram");
    }

    ARBShaderObjects.glValidateProgramARB(program);
    if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
      System.err.println(ARBShaderObjects.glGetInfoLogARB(program, ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));
      throw new RuntimeException("invalid program!");
    }
    return program;
  }

  private static int createShader(String filename, int shaderType) throws Exception {
    int shader = 0;
    try {
      shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

      if (shader == 0)
        return 0;

      ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
      ARBShaderObjects.glCompileShaderARB(shader);

      if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
        throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

      return shader;
    } catch (Exception exc) {
      ARBShaderObjects.glDeleteObjectARB(shader);
      throw exc;
    }
  }

  private static String readFileAsString(String filename) throws Exception {
    StringBuilder source = new StringBuilder();

    FileInputStream in = new FileInputStream(filename);

    Exception exception = null;

    BufferedReader reader;
    try {
      reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

      Exception innerExc = null;
      try {
        String line;
        while ((line = reader.readLine()) != null)
          source.append(line).append('\n');
      } catch (Exception exc) {
        exception = exc;
      } finally {
        try {
          reader.close();
        } catch (Exception exc) {
          if (innerExc == null)
            innerExc = exc;
          else
            exc.printStackTrace();
        }
      }

      if (innerExc != null)
        throw innerExc;
    } catch (Exception exc) {
      exception = exc;
    } finally {
      try {
        in.close();
      } catch (Exception exc) {
        if (exception == null)
          exception = exc;
        else
          exc.printStackTrace();
      }

      if (exception != null)
        throw exception;
    }

    return source.toString();
  }

  private static String getLogInfo(int obj) {
    return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
  }

  public static void checkFrameBuffer(int fbo_ID) {
    glBindTexture(GL_TEXTURE_2D, 0);
    glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fbo_ID);

    int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
    switch (framebuffer) {
      case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
        break;
      case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
        throw new RuntimeException("FrameBuffer: " + fbo_ID
            + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
      case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
        throw new RuntimeException("FrameBuffer: " + fbo_ID
            + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
      case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
        throw new RuntimeException("FrameBuffer: " + fbo_ID
            + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
      case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
        throw new RuntimeException("FrameBuffer: " + fbo_ID
            + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
      case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
        throw new RuntimeException("FrameBuffer: " + fbo_ID
            + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
      case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
        throw new RuntimeException("FrameBuffer: " + fbo_ID
            + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
      default:
        throw new RuntimeException("Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer);
    }
    glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);

  }

  public static int generateTexture(int FBO_ID, int width, int height) {
    int colorTextureID = glGenTextures();

    glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, FBO_ID);
    // initialize color texture
    glBindTexture(GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);               // make it linear filterd
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
    glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureID, 0);
    glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);

    return colorTextureID;

  }
}
