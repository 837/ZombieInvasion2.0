package ch.redmonkeyass.zombieInvasion.util.shadows;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by P on 22.02.2016.
 */
public class QuadVAO {
    private final int VAOhandle;
    private final int uvBuffer;
    protected int vertexBuffer;
    private final int indicesBuffer;


    QuadVAO(int w, int h){
        //indices for a quad made up of two triangles
        indicesBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, (ShortBuffer) BufferUtils.createShortBuffer(6).put(new short[]{
                0, 1, 2,
                2, 3, 0
        }).flip(), GL_STATIC_DRAW); //flip() optimizes the buffer for read operations
        //create vertex and uv buffers for a w * h quad
        vertexBuffer = glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL15.GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[]{
                0, 0, 0.0f,
                w, 0, 0.0f,
                w, h, 0.0f,
                0, h, 0.0f})
                .flip(), GL15.GL_STATIC_DRAW);
        uvBuffer = glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER, uvBuffer);
        glBufferData(GL15.GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.createFloatBuffer(8).put(new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,

        }).flip(), GL_STATIC_DRAW);

        //create vertex array object
        VAOhandle = glGenVertexArrays();

        //link the buffers created above and the vao together
        bindVAO();
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, uvBuffer);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer);

        //unbind vao so we don't accidentally fuck up something
        unbindVAO();
    }

    public void bindVAO(){
        glBindVertexArray(VAOhandle);
    }
    public static void unbindVAO(){
        glBindVertexArray(0);
    }

    public void drawQuad(){
        bindVAO();
        drawWithCurrentlyBoundVAO();
    }

    public static void drawWithCurrentlyBoundVAO(){
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
    }
}
