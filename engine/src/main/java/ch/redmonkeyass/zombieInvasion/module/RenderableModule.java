package ch.redmonkeyass.zombieInvasion.module;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Interface for Modules which should be <b>RENDERED</b>.
 *
 * @author Matthias
 */
public interface RenderableModule {
    void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g);

    default boolean castShadow() {
        return true;
    }
}
