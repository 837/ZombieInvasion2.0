package ch.m837.zombieInvasion.entities.modul.moduls;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.modul.Modul;
import ch.m837.zombieInvasion.entities.modul.UpdatableModul;

public class TestModul2 extends Modul implements  UpdatableModul {

  public TestModul2(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getData(DataType dataType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    // TODO Auto-generated method stub

  }
}
