package ch.m837.zombieInvasion.gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SplashScreen extends BasicGameState {
  private final int ID;

  public SplashScreen(int ID) {
    this.ID = ID;
  }

  @Override
  public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int d) throws SlickException {
    // TODO Auto-generated method stub

  }

  @Override
  public int getID() {
    return ID;
  }

}
