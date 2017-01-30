package ch.redmonkeyass.zombieInvasion.module;

import ch.redmonkeyass.zombieInvasion.WorldHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class ModuleHandler {
  private HashMap<Class<?>, ArrayList<? extends Module>> modulesMap = new HashMap<>();

  public void addModules(Module... modules) {
    Arrays.stream(modules).forEach(module -> {
      WorldHandler.getEntityHandler().addModuleToEntity(module);
      addModuleToList(module.getClass(), module);
    });
  }

  public <T> void removeModulesFrom(String entityID, Class<T>... concreteModuleClazzes) {
    Arrays.stream(concreteModuleClazzes).forEach(module -> {
      WorldHandler.getEntityHandler().removeModuleFromEntity(module, entityID);
      Module moduleM = modulesMap.get(module).stream()
          .filter(m -> m.getEntityID().equals(entityID)).findAny().orElse(null);
      modulesMap.get(module).remove(moduleM);
    });
  }

  @SuppressWarnings("unchecked")
  public <T> void addModuleToList(Class<T> listType, Module module) {
    if (!modulesMap.containsKey(listType)) {
      modulesMap.put(listType, (ArrayList<? extends Module>) new ArrayList<T>());
    }
    ((ArrayList<T>) modulesMap.get(listType)).add((T) module);
  }

  public <T> void removeModuleFromList(Class<T> listType, String entityID) {
    ArrayList<Module> t = (ArrayList<Module>) modulesMap.get(listType);
    if (t != null) {
      t.removeIf(module -> module.getEntityID().equals(entityID));
    }
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<ArrayList<T>> getModulesOf(Class<T> listType) {
    return Optional.ofNullable(((ArrayList<T>) modulesMap.get(listType)));
  }

  public void UPDATE_MODULEHANDLER() {
    WorldHandler.getEventDispatcher().getEvents().stream().sequential()
        .filter(event -> event.getReceiverID().equals("MODULE_HANDLER")).forEach(e -> {
          switch (e.getEvent()) {
            case REMOVE_ENTITY:
              modulesMap.forEach((clazz, list) -> list
                  .removeIf(module -> module.getEntityID().equals(e.getSenderID())));
              break;
          }
        });
  }
}


