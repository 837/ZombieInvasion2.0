package ch.redmonkeyass.zombieInvasion.util;

import ch.redmonkeyass.zombieInvasion.Config;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

/**
 * test functionality of GLHelper
 * Created by P on 28.07.2015.
 */
public class ShaderTester {
  int distanceProgram;
  int distortionProgram;
  int reductionProgram;
  int drawProgram;
  int testProgram;
  //int shadowCastersFBO = GLHelper.generateFrameBufferObject(Config.WIDTH,Config.HEIGHT);

  int texture;
  int fbo;

  public ShaderTester() {
    init();
  }

  private void init() {
    testProgram = GLHelper.loadShaderProgram("res/shaderinos/passthrough.vert", "res/shaderinos/testFragger.frag");
    //distanceProgram = GLHelper.loadShaderProgram("res/shaderinos/passthrough.vert","res/shaderinos/calcDistances.frag");

    fbo = GLHelper.generateFrameBufferObject();
    texture = GLHelper.generateTexture(fbo, Config.WIDTH, Config.HEIGHT);
    GLHelper.checkFrameBuffer(fbo);

  }

  public void calculateShadows() {
    //GLHelper.renderToFBO(shadowCastersFBO,Config.WIDTH,Config.HEIGHT);
    //ARBShaderObjects.glUseProgramObjectARB(distanceProgram);

  }

  public void drawToTexture() {
    GLHelper.renderToFBO(fbo, Config.WIDTH, Config.HEIGHT);
  }

  public void drawTextureToScreen() {
    GLHelper.stopRenderingToFBO();
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
