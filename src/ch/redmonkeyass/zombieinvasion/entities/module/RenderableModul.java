package ch.redmonkeyass.zombieinvasion.entities.module;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface RenderableModul {
   void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g);
}
