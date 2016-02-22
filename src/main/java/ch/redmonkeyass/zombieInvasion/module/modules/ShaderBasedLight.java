package ch.redmonkeyass.zombieInvasion.module.modules;

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
public class ShaderBasedLight {
}
