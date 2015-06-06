package ch.m837.zombieInvasion.gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.entityFactories.EntityFactory;
import ch.m837.zombieInvasion.entities.entityFactories.EntityType;
import ch.m837.zombieInvasion.input.InputHandler;
import ch.zombieInvasion.Eventhandling.EventDispatcher;
import ch.zombieInvasion.util.Images;

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
  private InputHandler inputHandler = null;


  @Override
  public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    EntityFactory.createEntity(EntityType.PLAYER_TEST);



    inputHandler = new InputHandler(gc);

    World.getCamera().setMapData(Images.MENU_BACKGROUND.get().getWidth(), Images.MENU_BACKGROUND.get().getHeight());
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    g.translate(-World.getCamera().getCamPosX(), -World.getCamera().getCamPosY());
    
    g.drawImage(Images.MENU_BACKGROUND.get(), 0, 0);
    World.getModuleHandler().getSimpleImageRenderModules().forEach(m -> m.RENDER(gc, sbg, g));
    World.getModuleHandler().getPhysicsModules().forEach(m -> m.RENDER(gc, sbg, g));

  }

  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int d) throws SlickException {
    loops = 0;
    while (System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {

      // GAME UPDATE CODE GOES HERE

      // XXX TEST

      EventDispatcher.getEvents().parallelStream()
          .filter(event -> event.getReceiverID().equals("GLOBAL")).forEach(e -> {
            switch (e.getEvent()) {
              case A_PRESSED:
                World.getCamera().move(new Vector2(-20, 0));
                break;
              case D_PRESSED:
                World.getCamera().move(new Vector2(20, 0));
                break;
              case S_PRESSED:
                World.getCamera().move(new Vector2(0, 20));
                break;
              case W_PRESSED:
                World.getCamera().move(new Vector2(0, -20));
                break;
            }
          });


      World.getEntityHandler().UPDATE_ENTITIES();
      World.getModuleHandler().getSelectionModules().forEach(m -> m.UPDATE(gc, sbg));
      World.getModuleHandler().getPhysicsModules().forEach(m -> m.UPDATE(gc, sbg));
      World.getModuleHandler().getMovementModules().forEach(m -> m.UPDATE(gc, sbg));


      World.getB2World().step(1.0f / TICKS_PER_SECOND, 6, 2);
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
