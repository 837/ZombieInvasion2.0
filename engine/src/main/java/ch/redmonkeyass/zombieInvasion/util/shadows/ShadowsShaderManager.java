package ch.redmonkeyass.zombieInvasion.util.shadows;

import ch.redmonkeyass.zombieInvasion.Config;
import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.pbuffer.FBOGraphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by P on 05.08.2015.
 */
public class ShadowsShaderManager {

	private final static ShaderProgram distanceProgram;
	private final static ShaderProgram distortionProgram;
	private final static ShaderProgram reductionProgram;
	private final static ShaderProgram drawProgram;
	private final static ShaderProgram blurProgram;

	private FrameBuffer distanceFBO;
	private FrameBuffer distortionFBO;
	private final ArrayList<FrameBuffer> reductionCalcFBO = new ArrayList<>();
	private FrameBuffer shadowsFBO;
	private FrameBuffer blurFBO;
	private QuadVAO vao;
	private UpdatableQuadVAO streamDrawVAO;
	private final FloatBuffer mvpMatrixBuffer;
	private final int w;
	private final int h;
	private final int trueW;
	private final int trueH;
	private boolean blurEnabled = true;

	public ShadowsShaderManager(FloatBuffer mvpMatrixBuffer, int w, int h, int trueW, int trueH) {
		this.mvpMatrixBuffer = mvpMatrixBuffer;
		this.h = h;
		this.w = w;
		this.trueW = trueW;
		this.trueH = trueH;


		init();
	}

	//load shaderprograms on first use of this class
	static {
		distanceProgram = new ShaderProgram(Config.RESSOURCE_FOLDER + "shaderinos/passthroughVBO.vert", Config.RESSOURCE_FOLDER + "shaderinos/calcDistances.frag");
		distortionProgram = new ShaderProgram(Config.RESSOURCE_FOLDER + "shaderinos/passthroughVBO.vert", Config.RESSOURCE_FOLDER + "shaderinos/distortImage.frag");
		reductionProgram = new ShaderProgram(Config.RESSOURCE_FOLDER + "shaderinos/passthroughVBO.vert", Config.RESSOURCE_FOLDER + "shaderinos/horizontalReduction.frag");
		drawProgram = new ShaderProgram(Config.RESSOURCE_FOLDER + "shaderinos/passthroughVBO.vert", Config.RESSOURCE_FOLDER + "shaderinos/drawShadows.frag");
		blurProgram = new ShaderProgram(Config.RESSOURCE_FOLDER + "shaderinos/passthroughVBO.vert", Config.RESSOURCE_FOLDER + "shaderinos/simpleGaussianBlur.frag");

		//load uniform locations into cache
		reductionProgram.enableUniform("mvp", "sourceDimensions");
		distanceProgram.enableUniform("mvp", "textureDimension");
		drawProgram.enableUniform("mvp", "renderTargetSize");
		distortionProgram.enableUniform("mvp");
		blurProgram.enableUniform("mvp", "dir");
	}

	private void init() {
		distanceFBO = new FrameBuffer(FrameBuffer.type.FLOAT, false, w, h, GL_LINEAR);
		distortionFBO = new FrameBuffer(FrameBuffer.type.FLOAT, false, w, h, GL_NEAREST);
		shadowsFBO = new FrameBuffer(FrameBuffer.type.FLOAT, false, w, h, GL_LINEAR);
		blurFBO = new FrameBuffer(FrameBuffer.type.FLOAT, false, w, h, GL_LINEAR);

		final int nReductions = (int) (Math.log(w) / Math.log(2) + 1e-12);
		for (int i = 1; i < nReductions; i++) {
			reductionCalcFBO.add(new FrameBuffer(FrameBuffer.type.FLOAT, false, (int) Math.pow(2, i), h, GL_NEAREST));
		}

		vao = new QuadVAO(w, h);
		streamDrawVAO = new UpdatableQuadVAO(w, h);
	}

	/**
	 * all textures are implicitly sent to the default texture unit (GL13.GL_TEXTURE0);
	 *
	 * @param shadowCasters an Image where opaque pixels throw shadows
	 * @param target        output is stored in the texture of target FBO
	 */
	public void renderShadows(Image shadowCasters, FBOGraphics target) {

		//************* distance step
		//select framebuffer
		distanceFBO.setAsActiveFBO();
		glClear(GL_COLOR_BUFFER_BIT);

		//activate shader, while in use it will affect all drawing operations
		distanceProgram.useProgram();
		distanceProgram.sendUniform2f(
				"textureDimension", shadowCasters.getWidth(), shadowCasters.getHeight());
		distanceProgram.sendUniformMatrix4("mvp", mvpMatrixBuffer);
		shadowCasters.bind();
		vao.drawQuad();

		//************ distortion step
		distortionFBO.setAsActiveFBO();
		glClear(GL_COLOR_BUFFER_BIT);
		distortionProgram.useProgram();
		distortionProgram.sendUniformMatrix4("mvp", mvpMatrixBuffer);

		distanceFBO.bindTexture();
		vao.drawQuad();

		//*************** reductionCalcFBO
		reductionProgram.useProgram();

		reductionCalcFBO.get(reductionCalcFBO.size() - 1).setAsActiveFBO();
		glClear(GL_COLOR_BUFFER_BIT);

		reductionProgram.sendUniform2f(
				"sourceDimensions", 1.0f / distortionFBO.getWidth(), 1.0f / distortionFBO.getHeight());
		reductionProgram.sendUniformMatrix4("mvp", mvpMatrixBuffer);
		distortionFBO.bindTexture();
		streamDrawVAO.update(w / 2, h);
		QuadVAO.drawWithCurrentlyBoundVAO();

		for (int i = 1; i < reductionCalcFBO.size(); i++) {
			int n = reductionCalcFBO.size() - 1 - i;
			reductionCalcFBO.get(n).setAsActiveFBO();
			glClear(GL_COLOR_BUFFER_BIT);
			reductionProgram.sendUniform2f(
					"sourceDimensions", 1.0f / reductionCalcFBO.get(n + 1).getWidth(), 1.0f / reductionCalcFBO.get(n + 1).getHeight());
			reductionProgram.sendUniformMatrix4("mvp", mvpMatrixBuffer);
			reductionCalcFBO.get(n + 1).bindTexture();

			streamDrawVAO.update(reductionCalcFBO.get(n).getWidth(), reductionCalcFBO.get(n).getHeight());
			QuadVAO.drawWithCurrentlyBoundVAO();

		}

		//************* draw shadows
		shadowsFBO.setAsActiveFBO();
		if (!blurEnabled) {
			Graphics.setCurrent(target);
		}
		glClear(GL_COLOR_BUFFER_BIT);
		drawProgram.useProgram();

		reductionCalcFBO.get(0).bindTexture();

		drawProgram.sendUniformMatrix4("mvp", mvpMatrixBuffer);
		drawProgram.sendUniform2f("renderTargetSize", w, h);
		vao.drawQuad();

		if (!blurEnabled) return;
		//********************************
		// blur filter vertical
		blurFBO.setAsActiveFBO();
		blurProgram.useProgram();

		glClear(GL_COLOR_BUFFER_BIT);
		shadowsFBO.bindTexture();
		//vertical blur
		blurProgram.sendUniform2f("dir", 0, 1);
		blurProgram.sendUniformMatrix4("mvp", mvpMatrixBuffer);
		vao.drawQuad();

		//blur horizontal to target FBO
		Graphics.setCurrent(target);
		//horizontal blur
		blurProgram.sendUniform2f("dir", 1, 0);
		shadowsFBO.bindTexture();
		vao.drawQuad();

		ShaderProgram.disablePrograms();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
	}

	public static Matrix4f toOrtho2D(Matrix4f m, float x, float y, float width, float height, float near, float far) {
		return toOrtho(m, x, x + width, y, y + height, near, far);
	}

	/**
	 * Sets the given matrix to an orthographic projection matrix, and returns it. If the given matrix
	 * is null, a new one will be created and returned.
	 *
	 * @param m    the matrix to re-use, or null to create a new matrix
	 * @param near near clipping plane
	 * @param far  far clipping plane
	 * @return the given matrix, or a newly created matrix if none was specified
	 */
	public static Matrix4f toOrtho(Matrix4f m, float left, float right, float bottom, float top,
	                               float near, float far) {
		if (m == null)
			m = new Matrix4f();
		float x_orth = 2.0f / (right - left);
		float y_orth = 2.0f / (top - bottom);
		float z_orth = -2.0f / (far - near); //not really needed

		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near) / (far - near);

		m.m00 = x_orth;
		m.m11 = y_orth;
		m.m22 = z_orth;
		m.m30 = tx;
		m.m31 = ty;
		m.m32 = tz;
		m.m33 = 1;
		return m;
	}

	public void setBlurEnabled(boolean blurEnabled) {
		this.blurEnabled = blurEnabled;
	}
}

