package ch.redmonkeyass.game.entityfactories;

import ch.redmonkeyass.game.module.modules.MoveSelectedEntityToMouseClick;
import ch.redmonkeyass.game.module.modules.MovementModule;
import ch.redmonkeyass.game.module.modules.SelectionModule;
import ch.redmonkeyass.game.module.modules.SimpleImageRenderModule;
import ch.redmonkeyass.game.module.modules.debugmodules.DebugRendererModule;
import ch.redmonkeyass.game.module.modules.game.DebugConsoleModule;
import ch.redmonkeyass.game.module.modules.game.DebugRendererGameModule;
import ch.redmonkeyass.game.module.modules.mouse.MouseSelectionModule;
import ch.redmonkeyass.game.module.modules.mouse.MouseTileSelectionModule;
import ch.redmonkeyass.game.module.modules.zombieAI.FollowPlayerAI;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.Entity;
import ch.redmonkeyass.zombieInvasion.module.modules.EntityStatusModule;
import ch.redmonkeyass.zombieInvasion.module.modules.EventListenerModule;
import ch.redmonkeyass.zombieInvasion.util.Images;

public class EntityFactory {



  static public void createEntity(EntityBuilder entityBuilder) {
    for (int i = 0; i < entityBuilder.getNumOfEntitiesToSpawn(); i++) {
      switch (entityBuilder.getEntityType()) {
        case ADOLF:
          createAdolf(entityBuilder);
          break;
        case HANS:
          createHans(entityBuilder);
          break;
        case GERHART:
          createGerhart(entityBuilder);
          break;
        case ZOMBIE:
          createZombie(entityBuilder);
          break;
        case MOUSE:
          createMouseEntity(entityBuilder);
          break;
        case GAME:
          createGameEntity(entityBuilder);
          break;
      }
    }
  }



  private static void createAdolf(EntityBuilder entityBuilder) {
    String id = "ADOLF";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));

    WorldHandler.getModuleHandler().addModules(new SelectionModule(id),
        new MovementModule(id, 1, 1), new MoveSelectedEntityToMouseClick(id),
        new SimpleImageRenderModule(id, Images.ADOLF),
        EntityFactoryHelper.createPhysicsModule(entityBuilder, id));

    entityBuilder.getAdditionalModules()
        .forEach(m -> WorldHandler.getModuleHandler().addModules(m));


    // DebugModules
    WorldHandler.getModuleHandler().addModules(new DebugRendererModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createHans(EntityBuilder entityBuilder) {
    String id = "HANS";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));

    WorldHandler.getModuleHandler().addModules(new SelectionModule(id),
        new MovementModule(id, 1, 1), new MoveSelectedEntityToMouseClick(id),
        new SimpleImageRenderModule(id, Images.HANS),
        EntityFactoryHelper.createPhysicsModule(entityBuilder, id));

    entityBuilder.getAdditionalModules()
        .forEach(m -> WorldHandler.getModuleHandler().addModules(m));


    // DebugModules
    WorldHandler.getModuleHandler().addModules(new DebugRendererModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createGerhart(EntityBuilder entityBuilder) {
    String id = "GERHART";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));

    WorldHandler.getModuleHandler().addModules(new SelectionModule(id),
        new MovementModule(id, 1, 1), new MoveSelectedEntityToMouseClick(id),
        new SimpleImageRenderModule(id, Images.GERHART),
        EntityFactoryHelper.createPhysicsModule(entityBuilder, id));

    entityBuilder.getAdditionalModules()
        .forEach(m -> WorldHandler.getModuleHandler().addModules(m));

    // DebugModules
    WorldHandler.getModuleHandler().addModules(new DebugRendererModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createZombie(EntityBuilder entityBuilder) {
    String id = "ZOMBIE" + WorldHandler.getEntityHandler().getAllEntities().size() + 1;
    WorldHandler.getEntityHandler().addEntity(new Entity(id));

    WorldHandler.getModuleHandler().addModules(new EntityStatusModule(id),
        new MovementModule(id, 1, 1), new FollowPlayerAI(id, "HANS"),
        new SimpleImageRenderModule(id, Images.ZOMBIE),
        EntityFactoryHelper.createPhysicsModule(entityBuilder, id));

    entityBuilder.getAdditionalModules()
        .forEach(m -> WorldHandler.getModuleHandler().addModules(m));


    // DebugModules
    WorldHandler.getModuleHandler().addModules(new DebugRendererModule(id));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createMouseEntity(EntityBuilder entityBuilder) {
    String id = "MOUSE";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));
    WorldHandler.getModuleHandler().addModules(new MouseSelectionModule(id),
        new MouseTileSelectionModule(id));

    entityBuilder.getAdditionalModules()
        .forEach(m -> WorldHandler.getModuleHandler().addModules(m));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }

  private static void createGameEntity(EntityBuilder entityBuilder) {
    String id = "GAME";
    WorldHandler.getEntityHandler().addEntity(new Entity(id));

    WorldHandler.getModuleHandler().addModules(new DebugConsoleModule(id),
        new DebugRendererGameModule(id));

    entityBuilder.getAdditionalModules()
        .forEach(m -> WorldHandler.getModuleHandler().addModules(m));

    // EventModule
    WorldHandler.getModuleHandler().addModules(new EventListenerModule(id));
  }
}
