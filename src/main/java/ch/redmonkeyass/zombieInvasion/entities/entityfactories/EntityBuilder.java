package ch.redmonkeyass.zombieInvasion.entities.entityfactories;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.entities.module.Module;

public class EntityBuilder {
  private Vector2 startPosition;
  private final EntityType entityType;
  private int numOfEntitiesToSpawn = 1;
  private ArrayList<Module> additionalModules = new ArrayList<>();

  public static EntityBuilder createBuilder(final EntityType entityType) {
    return new EntityBuilder(entityType);
  }

  private EntityBuilder(final EntityType entityType) {
    this.entityType = entityType;
  }

  public EntityBuilder startPosition(Vector2 startPosition) {
    this.startPosition = startPosition;
    return this;
  }

  public EntityBuilder numOfEntitiesToSpawn(int numOfEntitiesToSpawn) {
    this.numOfEntitiesToSpawn = numOfEntitiesToSpawn;
    return this;
  }

  public EntityBuilder addAdditionalModules(Module... modules) {
    for (int i = 0; i < modules.length; i++) {
      additionalModules.add(modules[i]);
    }
    return this;
  }

  public void createEntity() {
    EntityFactory.createEntity(this);
  }

  public ArrayList<Module> getAdditionalModules() {
    return additionalModules;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public int getNumOfEntitiesToSpawn() {
    return numOfEntitiesToSpawn;
  }

  public Vector2 getStartPosition() {
    return startPosition;
  }
}
