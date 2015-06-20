package ch.redmonkeyass.zombieInvasion.entities.module;

import java.util.ArrayList;
import java.util.Arrays;

import ch.redmonkeyass.zombieInvasion.World;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.EntityStatusModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.EventListenerModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.MovementModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.PhysicsModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.SelectionModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.SimpleImageRenderModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.debugmodules.DebugRendererModule;
import ch.redmonkeyass.zombieInvasion.entities.module.modules.mouse.MouseSelectionModule;

public class ModuleHandler {
  // private HashMap<Class<? extends Module>, ArrayList<? extends Module>> modulesMap =
  // new HashMap<>();

  /*
   * Needs to be expanded for each new Module
   * 
   */
  private ArrayList<SelectionModule> selectionModules = new ArrayList<>();
  private ArrayList<PhysicsModule> physicsModules = new ArrayList<>();
  private ArrayList<SimpleImageRenderModule> simpleImageRenderModules = new ArrayList<>();
  private ArrayList<MovementModule> movementModules = new ArrayList<>();
  private ArrayList<EntityStatusModule> entityStatusModules = new ArrayList<>();


  // DEBUGMODULES
  private ArrayList<DebugRendererModule> debugRendererModules = new ArrayList<>();


  // MouseModules
  private ArrayList<MouseSelectionModule> mouseSelectionModule = new ArrayList<>();

  // EventModule
  private ArrayList<EventListenerModule> eventListenerModules = new ArrayList<>();


  public ModuleHandler() {
    // modulesMap.put(SelectionModule.class, selectionModules);
    // modulesMap.put(PhysicsModule.class, physicsModules);
    // modulesMap.put(SimpleImageRenderModule.class, simpleImageRenderModules);
    // modulesMap.put(MovementModule.class, movementModules);
    // modulesMap.put(EntityStatusModule.class, entityStatusModules);
    // modulesMap.put(DebugRendererModule.class, debugRendererModules);
    // modulesMap.put(MouseSelectionModule.class, mouseSelectionModule);
    // modulesMap.put(EventListenerModule.class, eventListenerModules);
  }

  public void addModules(Module... modules) {
    Arrays.stream(modules).forEach(module -> {
      World.getEntityHandler().addModulToEntity(module);
      /*
       * Needs to be expanded for each new Module
       *
       */
      if (module instanceof SelectionModule) {
        selectionModules.add((SelectionModule) module);
      } else if (module instanceof PhysicsModule) {
        physicsModules.add((PhysicsModule) module);
      } else if (module instanceof SimpleImageRenderModule) {
        simpleImageRenderModules.add((SimpleImageRenderModule) module);
      } else if (module instanceof MovementModule) {
        movementModules.add((MovementModule) module);
      } else if (module instanceof DebugRendererModule) { // DebugModules
        debugRendererModules.add((DebugRendererModule) module);
      } else if (module instanceof MouseSelectionModule) { // MouseModules
        mouseSelectionModule.add((MouseSelectionModule) module);
      } else if (module instanceof EventListenerModule) {
        eventListenerModules.add((EventListenerModule) module);
      } else if (module instanceof EntityStatusModule) {
        entityStatusModules.add((EntityStatusModule) module);
      }

    });
  }



//   /*
//   * Needs to be expanded for each new Module
//   *
//   */
//   public ArrayList<Module> getAllModules() {
//   ArrayList<Module> allModules = new ArrayList<>();
//   allModules.addAll(selectionModules);
//   allModules.addAll(physicsModules);
//   allModules.addAll(simpleImageRenderModules);
//   allModules.addAll(movementModules);
//   allModules.addAll(entityStatusModules);
//  
//   // MouseModules
//   allModules.addAll(mouseSelectionModule);
//  
//   // DebugModules
//   allModules.addAll(debugRendererModules);
//  
//   // EventModule
//   allModules.addAll(eventListenerModules);
//  
//   return allModules;
//   }
  


  public ArrayList<PhysicsModule> getPhysicsModules() {
    return physicsModules;
  }

  public ArrayList<SelectionModule> getSelectionModules() {
    return selectionModules;
  }

  public ArrayList<SimpleImageRenderModule> getSimpleImageRenderModules() {
    return simpleImageRenderModules;
  }

  public ArrayList<MovementModule> getMovementModules() {
    return movementModules;
  }

  public ArrayList<EntityStatusModule> getEntityStatusModules() {
    return entityStatusModules;
  }

  // DebugModules
  public ArrayList<DebugRendererModule> getDebugRendererModules() {
    System.out.println(debugRendererModules.size());
    return debugRendererModules;
  }

  // MouseModules
  public ArrayList<MouseSelectionModule> getMouseSelectionModule() {
    return mouseSelectionModule;
  }

  // EventModule
  public ArrayList<EventListenerModule> getEventListenerModules() {
    return eventListenerModules;
  }
}
