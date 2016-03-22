package ch.redmonkeyass.zombieInvasion.util.shadows;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

/**
 * Created by P on 02.09.2015.
 */
public class FrameBuffer {
    private final int width, height;
    private int frameBufferHandle;
    private int textureHandle;
    private int depthBufferHandle;

    public FrameBuffer(type underLyingDataType, boolean createDepthBuffer, int width, int height, int minFilter) {
        this.width = width;
        this.height = height;

        switch (underLyingDataType) {

            case FLOAT:
                genFBOwithRGBA32F(width, height, minFilter);
                break;
            case INT:
                throw new UnsupportedOperationException("not implemented");
        }


    }

    public void setAsActiveFBO() {
        glBindFramebuffer(GL_FRAMEBUFFER_EXT, frameBufferHandle);
    }

    /**
     * binds the framebuffer's content to the active texture unit
     */
    public void bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
    }

    /**
     * shortcut to send this fbo's texture as uniform to the currently selected shader
     * as GL13.GL_TEXTURE0
     */
    public void sendTextureToSamplerLocation0(int uniformLocation) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        GL20.glUniform1i(uniformLocation, 0);
    }

    /**
     * shortcut to send this fbo's dimensions as uniform to the currently selected shader
     * vec2(width,height)
     */
    public void sendTextureDimensionsAsUniform(int uniformLocation) {
        GL20.glUniform2f(uniformLocation, width, height);

    }

    public int getFrameBufferHandle() {
        return frameBufferHandle;
    }

    public int getTextureHandle() {
        return textureHandle;
    }

    public int getDepthBufferHandle() {
        return depthBufferHandle;
    }

    /**
     * creates a new FBO with a float texture and initializes the object handles
     *
     * @param width
     * @param height
     * @return array with fboHandle at 0, textureID at 1, depthID at 2
     */
    private int[] genFBOwithRGBA32F(int width, int height, int minFilter) {

        IntBuffer buffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); // allocate a 1 int byte buffer
        EXTFramebufferObject.glGenFramebuffersEXT(buffer); // generate
        frameBufferHandle = buffer.get();
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, frameBufferHandle);

        textureHandle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureHandle);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_FLOAT, (FloatBuffer) null); // implicitly create new ByteBuffer

        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
                GL11.GL_TEXTURE_2D, textureHandle, 0);

        int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
        switch (framebuffer) {
            case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
                break;
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                throw new RuntimeException("FrameBuffer: " + frameBufferHandle
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                throw new RuntimeException("FrameBuffer: " + frameBufferHandle
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                throw new RuntimeException("FrameBuffer: " + frameBufferHandle
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                throw new RuntimeException("FrameBuffer: " + frameBufferHandle
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                throw new RuntimeException("FrameBuffer: " + frameBufferHandle
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                throw new RuntimeException("FrameBuffer: " + frameBufferHandle
                        + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
            default:
                throw new RuntimeException("Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer);
        }

        if (frameBufferHandle == 0)
            throw new RuntimeException("something went wrong during fbo creation");

        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);

        return new int[]{frameBufferHandle, textureHandle, 0};
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public enum type {
        FLOAT, INT
    }
}
