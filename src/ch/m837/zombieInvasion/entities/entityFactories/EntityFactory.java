package ch.m837.zombieInvasion.entities.entityFactories;

import java.util.UUID;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.Entity;
import ch.m837.zombieInvasion.entities.module.modules.SelectionModule;

public class EntityFactory {
  static public void createEntity(EntityType type) {
    switch (type) {
      case TEST_ENTITY_1:
        createTestEntity1();
        break;

    }
  }

  static private void createTestEntity1() {
    String id = "TEST_ENTITY_1: " + UUID.randomUUID();
    World.getEntityHandler().addEntity(new Entity(id));
    World.getModuleHandler().addModules( new SelectionModule(id));
  }

}
