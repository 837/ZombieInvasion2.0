package ch.m837.zombieInvasion.entities.entityFactories;

import java.util.UUID;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.Entity;
import ch.m837.zombieInvasion.entities.modul.moduls.TestModul1;
import ch.m837.zombieInvasion.entities.modul.moduls.TestModul2;

public class EntityFactory {
  static public void createEntity(EntityType type) {
    switch (type) {
      case TEST_ENTITY_1:
        createTestEntity1();
        break;

      case TEST_ENTITY_2:
        createTestEntity2();
        break;
    }
  }

  static private void createTestEntity1() {
    String id = "TEST_ENTITY_1: " + UUID.randomUUID();
    World.getEntityHandler().addEntity(new Entity(id));
    World.getModulHandler().addModul(new TestModul1(id));
    World.getModulHandler().addModul(new TestModul2(id));
  }

  static private void createTestEntity2() {
    String id = "TEST_ENTITY_2: " + UUID.randomUUID();
    World.getEntityHandler().addEntity(new Entity(id));
    World.getModulHandler().addModul(new TestModul1(id));
    World.getModulHandler().addModul(new TestModul2(id));
  }
}
