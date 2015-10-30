package ch.redmonkeyass.zombieInvasion.entities.module;

/**
 * Interface for Modules which should be <b>RENDERED</b>.
 * 
 * @author Matthias
 *
 */
public interface RenderableModul {
  abstract public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g);
}
