package ch.redmonkeyass.zombieInvasion.module.modules;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.module.RenderableModule;
import ch.redmonkeyass.zombieInvasion.util.shadows.ShadowsShaderManager;
import com.badlogic.gdx.math.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.pbuffer.FBOGraphics;
import org.newdawn.slick.state.StateBasedGame;

import java.nio.FloatBuffer;

/**
 * Created by P on 19.02.2016.
 *
 * creates a shadow texture roughly according to this procedure
 * http://www.catalinzima.com/2010/07/my-technique-for-the-shader-based-dynamic-2d-shadows/
 *
 *
 * preconditions:
 * - All textures with the cast-shadow-flag enabled are rendered onto a texture "shadowcasters"
 *
 * for each light:
 * 1. determine whether light is actually on the screen
 * 2. fetch the the region of the shadowcasters texture that is within the lights radius
 * 3. create shadowmap and draw shadows onto shadows texture
 * 4. apply blur effect
 *
 * post-procedure:
 * - The lighting system blends all the shadows together
 *
 * TODO can the blending be done with the color of the light already added to the shadow texture?
 * TODO lighting system
 * TODO texture module needs cast-shadow-flag
 * TODO three step render system
 *  (render shadow casting to texture, render shadows and shadowcasters, render non shadow casting)
 *
 */
public class ShaderBasedLight implements RenderableModule {
    private FBOGraphics shadowFBO;
    private Image shadowTexture;
    private final int lightRadius;
    ShadowsShaderManager shadowsShaderManager;
    Vector2f position; //center of the lights circle
    Rectangle lightWindow; //

    public ShaderBasedLight(int lightRadius, FloatBuffer mvpMatrix){
        this.lightRadius = lightRadius;
        shadowsShaderManager = new ShadowsShaderManager(mvpMatrix,lightRadius,lightRadius, Config.WIDTH,Config.HEIGHT);
        try {
            shadowTexture = new Image(lightRadius,lightRadius);
            shadowFBO = new FBOGraphics(shadowTexture);
        } catch (SlickException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean castShadow() {
        return false; // :(
    }

    @Override
    public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
        // 1. determine whether light is actually on the screen, otherwise return
        if(! WorldHandler.getCamera().overlapsWithCamera(lightWindow)) return;

        // 2. fetch the the region of the shadowcasters texture that is within the lights radius

    }

    private void drawShadows(FBOGraphics target){

    }
}
