package ch.m837.zombieInvasion.entities.modul;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public interface UpdatableModul {
  abstract public void UPDATE(GameContainer gc, StateBasedGame sbg);
}
