package ch.redmonkeyass.zombieInvasion.util.shadows;

import org.lwjgl.opengl.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glGetError;

/**
 * Created by P on 23.02.2016.
 */
public class ShaderProgram {
    private final int program;
    private boolean linked;

    //caching uniform names,locations so we don't have to do a location lookup every frame
    protected Map<String,Integer> uniforms = new HashMap<>(10);

    public ShaderProgram(String vertexShaderPath,String fragmentShaderPath){
        program = loadShaderProgram(vertexShaderPath,fragmentShaderPath);
    }

    /*This is a somewhat incomplete list of glUniform functions
    no name and typechecking whatsoever, because it's a game engine
    not a family show
     */
    /**
     * can only use names previously cached with
     */
    public void sendUniformMatrix4(String name, FloatBuffer mat){
        assert uniforms.get(name)!=null : "fucked up uniforms, probably because "+name+" is not in the cache";
        GL20.glUniformMatrix4(uniforms.get(name),false,mat);
    }
    public void sendUniform2f(String name,float f1, float f2){
        assert uniforms.get(name)!=null : "fucked up uniforms, probably because "+name+" is not in the cache";
        GL20.glUniform2f(uniforms.get(name),f1,f2);
    }
    public void enableUniform(String name){
        int location = GL20.glGetUniformLocation(program,name);
        assert location != 0 : "program doesn't contain uniform "+name;
        uniforms.put(name,location);

        int glError = glGetError();
        if (glError != 0) System.err.println("gl error while fetching uniform location: " + glError);
    }
    public void enableUniform(String... names){
        for (String name : names) {
            enableUniform(name);
        }
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
                throw new IllegalArgumentException("couldn't load shaders, likely path is incorrect");
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
                throw new RuntimeException("Error creating shader " + filename + ": " + getLogInfo(shader));

            return shader;
        } catch (Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
    }

    private static String readFileAsString(String filename) throws IOException {
        //TODO specify file encoding (this code will break on other platforms)
        return new String(Files.readAllBytes(Paths.get(filename)));

    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public void useProgram(){
        ARBShaderObjects.glUseProgramObjectARB(program);
    }
    public static void disablePrograms(){
        ARBShaderObjects.glUseProgramObjectARB(0);
    }
}
