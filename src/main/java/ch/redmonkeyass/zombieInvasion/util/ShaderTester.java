package ch.redmonkeyass.zombieInvasion.util;

import ch.redmonkeyass.zombieInvasion.Config;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

/**
 * test functionality of GL_Helper
 * Created by P on 28.07.2015.
 */
public class ShaderTester {
  int distanceProgram;
  int distortionProgram;
  int reductionProgram;
  int drawProgram;
  int testProgram;
  //int shadowCastersFBO = GL_Helper.generateFrameBufferObject(Config.WIDTH,Config.HEIGHT);

  int texture;
  int fbo;

  public ShaderTester() {
    init();
  }

  private void init() {
    testProgram = GL_Helper.loadShaderProgram("res/shaderinos/passthrough.vert", "res/shaderinos/testFragger.frag");
    //distanceProgram = GL_Helper.loadShaderProgram("res/shaderinos/passthrough.vert","res/shaderinos/calcDistances.frag");

    fbo = GL_Helper.generateFrameBufferObject();
    texture = GL_Helper.generateTexture(fbo, Config.WIDTH, Config.HEIGHT);
    GL_Helper.checkFrameBuffer(fbo);

  }

  public void calculateShadows() {
    //GL_Helper.renderToFBO(shadowCastersFBO,Config.WIDTH,Config.HEIGHT);
    //ARBShaderObjects.glUseProgramObjectARB(distanceProgram);

  }

  public void drawToTexture() {
    GL_Helper.renderToFBO(fbo, Config.WIDTH, Config.HEIGHT);
  }

  public void drawTextureToScreen() {
    GL_Helper.stopRenderingToFBO();
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    GL11.glViewport(0, 0, Config.WIDTH, Config.HEIGHT);

    GL11.glBegin(GL11.GL_QUADS);
    GL11.glVertex2f(0, 0);
    GL11.glVertex2f(Config.WIDTH, 0);
    GL11.glVertex2f(Config.WIDTH, Config.HEIGHT);
    GL11.glVertex2f(0, Config.HEIGHT);
    GL11.glEnd();
  }

  public void startUsingTestShader() {
    ARBShaderObjects.glUseProgramObjectARB(testProgram);
  }

  public void stopUsingTestShader() {
    ARBShaderObjects.glUseProgramObjectARB(0);
  }


}
