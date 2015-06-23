package ch.redmonkeyass.zombieInvasion.entities.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import ch.redmonkeyass.zombieInvasion.World;

public class ModuleHandler {
  private HashMap<Class<?>, ArrayList<? extends Module>> modulesMap = new HashMap<>();

  public void addModules(Module... modules) {
    Arrays.stream(modules).forEach(module -> {
      World.getEntityHandler().addModulToEntity(module);
      addModuleToList(module.getClass(), module);
    });
  }

  @SuppressWarnings("unchecked")
  public <T> void addModuleToList(Class<T> listType, Module module) {
    if (!modulesMap.containsKey(listType)) {
      modulesMap.put(listType, (ArrayList<? extends Module>) new ArrayList<T>());
    }
    ((ArrayList<T>) modulesMap.get(listType)).add((T) module);
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<ArrayList<T>> getModulesOf(Class<T> listType) {
    return Optional.ofNullable(((ArrayList<T>) modulesMap.get(listType)));
  }

  public void UPDATE_MODULEHANDLER() {
    World.getEventDispatcher().getEvents().stream().sequential()
        .filter(event -> event.getReceiverID().equals("MODULE_HANDLER")).forEach(e -> {
          switch (e.getEvent()) {
            case REMOVE_ENTITY:
              modulesMap.forEach((clazz, list) -> {
                list.removeIf(module -> module.getEntityID().equals(e.getSenderID()));
              });
              break;
          }
        });
  }
}

