package ch.redmonkeyass.zombieInvasion.gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.util.Images;

public class MainMenu extends BasicGameState {
  private final int ID;

  public MainMenu(int ID) {
    this.ID = ID;
  }

  @Override
  public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    g.drawImage(Images.MENU_BACKGROUND.get(), 0, 0);
    g.drawString("Press SPACE to start!", 100, 100);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int d) throws SlickException {
    if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
      sbg.enterState(Config.GAME);
    }
  }

  @Override
  public int getID() {
    return ID;
  }

}
