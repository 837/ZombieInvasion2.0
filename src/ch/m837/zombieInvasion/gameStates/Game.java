package ch.m837.zombieInvasion.gameStates;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.Entity;
import ch.m837.zombieInvasion.entities.entityFactories.EntityFactory;
import ch.m837.zombieInvasion.entities.entityFactories.EntityType;
import ch.m837.zombieInvasion.entities.modul.moduls.TestModul1;
import ch.m837.zombieInvasion.entities.modul.moduls.TestModul2;
import ch.zombieInvasion.Eventhandling.EventDispatcher;
import ch.zombieInvasion.Eventhandling.EventType;

public class Game extends BasicGameState {
  private final int ID;

  public Game(int ID) {
    this.ID = ID;
  }

  private final int TICKS_PER_SECOND = 30;
  private final double timePerTick = 1000 / TICKS_PER_SECOND;
  private final int MAX_FRAMESKIP = 5;
  private double next_game_tick = System.currentTimeMillis();
  private int loops;
  private double extrapolation;

  private World world = new World();

  @Override
  public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    EntityFactory.createEntity(EntityType.TEST_ENTITY_1);
    EntityFactory.createEntity(EntityType.TEST_ENTITY_1);
    EntityFactory.createEntity(EntityType.TEST_ENTITY_2);
    EntityFactory.createEntity(EntityType.TEST_ENTITY_2);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    // GAME RENDER CODE GOES HERE

  }

  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int d) throws SlickException {
    loops = 0;
    while (System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {

      // GAME UPDATE CODE GOES HERE

      // XXX TEST

      World.getEntityHandler().UPDATE_ENTITIES();

      // XXX Kann man natürlich zusammenfassen
      World.getModulHandler().getTestModuls1().parallelStream()
          .forEach(testModul1 -> testModul1.UPDATE(gc, sbg));

      // XXX Kann man natürlich zusammenfassen
      World.getModulHandler().getTestModuls2().parallelStream()
          .forEach(testModul2 -> testModul2.UPDATE(gc, sbg));

     
      EventDispatcher.dispatchEvents();

      // XXX TEST



      next_game_tick += timePerTick;
      loops++;
    }

    if (next_game_tick < System.currentTimeMillis())

    {
      next_game_tick = System.currentTimeMillis();
    }

    extrapolation = 1 - (next_game_tick - System.currentTimeMillis()) / timePerTick;

  }

  @Override
  public int getID() {
    return ID;
  }

}
