package ch.m837.zombieInvasion.gameStates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.Config;
import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.entityFactories.EntityFactory;
import ch.m837.zombieInvasion.entities.entityFactories.EntityType;
import ch.m837.zombieInvasion.input.InputHandler;
import ch.zombieInvasion.Eventhandling.EventType;
import ch.zombieInvasion.util.Images;

public class Game extends BasicGameState {
  private final int ID;

  public Game(int ID) {
    this.ID = ID;
  }


  private double next_game_tick = System.currentTimeMillis();
  private int loops;
  private double extrapolation;

  private InputHandler inputHandler = null;
  private Logger logger= LogManager.getLogger(Game.class);


  @Override
  public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    EntityFactory.createEntity(EntityType.MOUSE);

    EntityFactory.createEntity(EntityType.PLAYER_TEST);

    inputHandler = new InputHandler(gc);

    World.getCamera().setMapData(Images.MENU_BACKGROUND.get().getWidth(),
        Images.MENU_BACKGROUND.get().getHeight());
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    g.translate(-World.getCamera().getPosition().x, -World.getCamera().getPosition().y);

    g.drawImage(Images.MENU_BACKGROUND.get(), 0, 0);

    // XXX TEST START

    World.getModuleHandler().getSimpleImageRenderModules().forEach(m -> m.RENDER(gc, sbg, g));
    World.getModuleHandler().getPhysicsModules().forEach(m -> m.RENDER(gc, sbg, g));

    // XXX DEBUGRENDERER
    World.getModuleHandler().getDebugRendererModules().forEach(m -> m.RENDER(gc, sbg, g));

    // XXX MouseModules
    World.getModuleHandler().getMouseSelectionModule().forEach(m -> m.RENDER(gc, sbg, g));

    // XXX TEST END
  }


  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int d) throws SlickException {
    loops = 0;
    while (System.currentTimeMillis() > next_game_tick && loops < Config.MAX_FRAMESKIP) {

      // GAME UPDATE CODE GOES HERE

      // XXX TEST START

      World.getCamera().UPDATE(gc, sbg);


      World.getEventDispatcher().getEvents().parallelStream()
          .filter(event -> event.getReceiverID().equals("GLOBAL")).forEach(e -> {
            switch (e.getEvent()) {
              case G_PRESSED:
                for (int i = 0; i < 10; i++) {
                  EntityFactory.createEntity(EntityType.PLAYER_TEST);
                }

                logger.debug("Spawned 10 new Entities");
                break;
              case K_PRESSED:
                World.getEventDispatcher().createEvent(0, EventType.KILL_ENTITY, null, "GAME",
                    "GLOBAL");
               logger.debug("Removed all Entities");
                break;
            }
          });


      World.getEntityHandler().UPDATE_ENTITIES();
      World.getModuleHandler().getSelectionModules().forEach(m -> m.UPDATE(gc, sbg));
      World.getModuleHandler().getPhysicsModules().forEach(m -> m.UPDATE(gc, sbg));
      World.getModuleHandler().getMovementModules().forEach(m -> m.UPDATE(gc, sbg));

      // MouseModules
      World.getModuleHandler().getMouseSelectionModule().forEach(m -> m.UPDATE(gc, sbg));


      World.getB2World().step(1.0f / Config.TICKS_PER_SECOND, 6, 2);
      World.getEventDispatcher().dispatchEvents();

      // XXX TEST END
      next_game_tick += Config.TIME_PER_TICK;
      loops++;
    }

    if (next_game_tick < System.currentTimeMillis())

    {
      next_game_tick = System.currentTimeMillis();
    }

    extrapolation = 1 - (next_game_tick - System.currentTimeMillis()) / Config.TIME_PER_TICK;

  }

  @Override
  public int getID() {
    return ID;
  }

}
