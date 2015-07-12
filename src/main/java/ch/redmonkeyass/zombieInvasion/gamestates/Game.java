package ch.redmonkeyass.zombieInvasion.gamestates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityFactory;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityType;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.EntityStatusModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.EventListenerModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.LightEmitter;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.PhysicsModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.SelectionModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.SimpleImageRenderModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.ThetaStarMovementModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.game.DebugConsoleModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse.MouseSelectionModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse.MouseTileSelectionModule;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;
import ch.redmonkeyass.zombieInvasion.input.InputHandler;
import ch.redmonkeyass.zombieInvasion.util.Images;

public class Game extends BasicGameState {
  private final int ID;
  private double next_game_tick = System.currentTimeMillis();
  private int loops;
  private double extrapolation;
  private InputHandler inputHandler = null;
  private Logger logger = LogManager.getLogger(Game.class);

  public Game(int ID) {
    this.ID = ID;
  }

  @Override
  public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    EntityFactory.createEntity(EntityType.MOUSE);
    EntityFactory.createEntity(EntityType.GAME);


    EntityFactory.createEntity(EntityType.ADOLF);
    EntityFactory.createEntity(EntityType.HANS);
    EntityFactory.createEntity(EntityType.GERHART);


    inputHandler = new InputHandler(gc);

  }

  @Override
  public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    g.translate(-WorldHandler.getCamera().getPosition().x,
        -WorldHandler.getCamera().getPosition().y);

    g.drawImage(Images.MENU_BACKGROUND.get(), 0, 0);

    // WorldMap
    WorldHandler.getWorldMap().RENDER(gc, sbg, g);

    // XXX TEST START
    WorldHandler.getModuleHandler().getModulesOf(SimpleImageRenderModule.class)
        .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

    // XXX DEBUGRENDERER
    // WorldHandler.getModuleHandler().getModulesOf(DebugRendererModule.class)
    // .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

    // XXX MouseModules
    WorldHandler.getModuleHandler().getModulesOf(MouseSelectionModule.class)
        .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

    WorldHandler.getModuleHandler().getModulesOf(LightEmitter.class)
        .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

    WorldHandler.getModuleHandler().getModulesOf(MouseTileSelectionModule.class)
        .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

    WorldHandler.getModuleHandler().getModulesOf(DebugConsoleModule.class)
        .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

    // XXX TEST END
  }


  @Override
  public void update(GameContainer gc, StateBasedGame sbg, int d) throws SlickException {
    loops = 0;
    while (System.currentTimeMillis() > next_game_tick && loops < Config.MAX_FRAMESKIP) {

      // GAME UPDATE CODE GOES HERE

      // XXX TEST START

      WorldHandler.getCamera().UPDATE(gc, sbg);

      // EventModule
      WorldHandler.getModuleHandler().getModulesOf(EventListenerModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getEventDispatcher().getEvents().parallelStream()
          .filter(event -> event.getReceiverID().equals("GLOBAL")).forEach(e ->

      {
            switch (e.getEvent()) {
              case G_PRESSED:
                for (int i = 0; i < 10; i++) {
                  EntityFactory.createEntity(EntityType.ZOMBIE);
                }
                logger.trace("Spawned 10 new Entities");
                break;
              case K_PRESSED:
                WorldHandler.getEventDispatcher().createEvent(0, EventType.KILL_ENTITY, null,
                    "GAME", "GLOBAL");
                logger.trace("Removed all Entities");
                break;
            }
          });

      WorldHandler.getEntityHandler().UPDATE_ENTITYHANDLER();
      WorldHandler.getModuleHandler().UPDATE_MODULEHANDLER();

      WorldHandler.getModuleHandler().getModulesOf(SelectionModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getModuleHandler().getModulesOf(PhysicsModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getModuleHandler().getModulesOf(ThetaStarMovementModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getModuleHandler().getModulesOf(EntityStatusModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      // MouseModules
      WorldHandler.getModuleHandler().getModulesOf(MouseSelectionModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getModuleHandler().getModulesOf(LightEmitter.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getModuleHandler().getModulesOf(MouseTileSelectionModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getModuleHandler().getModulesOf(DebugConsoleModule.class)
          .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

      WorldHandler.getB2World().step(1.0f / Config.TICKS_PER_SECOND, 6, 2);
      WorldHandler.getEventDispatcher().dispatchEvents();

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
