package ch.redmonkeyass.zombieinvasion.entities.module;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public interface UpdatableModul {
  void UPDATE(GameContainer gc, StateBasedGame sbg);
}
