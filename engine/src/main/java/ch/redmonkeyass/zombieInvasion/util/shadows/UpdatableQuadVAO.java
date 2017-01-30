package ch.redmonkeyass.zombieInvasion.util.shadows;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * Created by P on 22.02.2016.
 *
 * has stream vbo instead of static one which is optimized for getting resized
 */
public class UpdatableQuadVAO extends QuadVAO {
    UpdatableQuadVAO(int w, int h) {
        super(w, h);

        glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL15.GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[]{
                0, 0, 0.0f,
                w, 0, 0.0f,
                w, h, 0.0f,
                0, h, 0.0f})
                .flip(), GL15.GL_STREAM_DRAW);

        bindVAO();
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        unbindVAO();
    }

    public void update(int w, int h){
        glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL15.GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[]{
                0, 0, 0.0f,
                w, 0, 0.0f,
                w, h, 0.0f,
                0, h, 0.0f})
                .flip(), GL15.GL_STREAM_DRAW);

        bindVAO();
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        //do not unbind vao here
    }
}
