package ch.redmonkeyass.zombieInvasion.module;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
/**
 * Interface for Modules which should be <b>UPDATED</b>.
 * @author Matthias
 *
 */
public interface UpdatableModul {
  abstract public void UPDATE(GameContainer gc, StateBasedGame sbg);
}
