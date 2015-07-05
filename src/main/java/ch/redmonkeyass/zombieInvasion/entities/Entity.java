package ch.redmonkeyass.zombieInvasion.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.entities.module.Module;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.EntityStatusModule.Entity_Status;
import ch.redmonkeyass.zombieInvasion.eventhandling.Event;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;

public class Entity {
  private final String ID;

  private ArrayList<Module> modules = new ArrayList<>();
  private HashMap<DataType, Integer> moduleValueMap = new HashMap<>();


  public Entity(String ID) {
    this.ID = ID;
  }

  public void UPDATE_ENTITY() {
    World.getEntityHandler().getDataFrom(ID, DataType.ENTITY_STATUS, Entity_Status.class)
        .ifPresent(status -> {
          if (status == Entity_Status.DEAD) {
            modules.forEach(Module::prepareModuleForRemoval);
            World.getEventDispatcher().createEvent(0, EventType.REMOVE_ENTITY, null, ID,
                "ENTITY_HANDLER", "MODULE_HANDLER");
          }
        });
  }

  public String getID() {
    return ID;
  }

  public Optional<Object> getData(DataType dataType) {
    Integer index = moduleValueMap.get(dataType);
    if (index != null && index < modules.size()) {
      return modules.get(index).getData(dataType);
    } else {
      for (int i = 0; i < modules.size(); i++) {
        Object data = modules.get(i).getData(dataType).orElse(null);
        if (data != null) {
          moduleValueMap.put(dataType, i);
          return Optional.of(data);
        }
      }
    }
    return Optional.empty();
  }


  public void addModule(Module module) {
    modules.add(module);
    moduleValueMap.clear();
  }

  public void removeModul(Module module) {
    modules.remove(module);
    module.prepareModuleForRemoval();
    moduleValueMap.clear();
  }

  public Optional<ArrayList<Event>> getEvents() {
    /*
     * TODO this is a bit stupid... for some reason
     * Optional.ofNullable(World.....getDataFrom(...).orElse(null) seems to work...? wtf also method
     * reference cast => bad return type ???
     */

    // return World.getEntityHandler().getDataFrom(ID, DataType.EVENTS, ArrayList.class);


    return World.getEntityHandler().getDataFrom(ID, DataType.EVENTS, ArrayList.class)
        .map(e -> e);
  }
}
