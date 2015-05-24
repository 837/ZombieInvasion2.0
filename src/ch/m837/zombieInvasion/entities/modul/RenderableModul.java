package ch.m837.zombieInvasion.entities.modul;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface RenderableModul {
  abstract public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g);
}
