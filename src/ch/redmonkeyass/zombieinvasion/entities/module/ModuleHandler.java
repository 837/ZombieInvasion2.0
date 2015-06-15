package ch.redmonkeyass.zombieinvasion.entities.module;

import java.util.ArrayList;

import ch.redmonkeyass.zombieinvasion.World;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.SimpleImageRenderModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.debugmodules.DebugRendererModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.mouse.MouseSelectionModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.MovementModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.PhysicsModule;
import ch.redmonkeyass.zombieinvasion.entities.module.modules.SelectionModule;

public class ModuleHandler {
  /*
   * Needs to be expanded for each new Module
   * 
   */
  private ArrayList<SelectionModule> selectionModules = new ArrayList<>();
  private ArrayList<PhysicsModule> physicsModules = new ArrayList<>();
  private ArrayList<SimpleImageRenderModule> simpleImageRenderModules = new ArrayList<>();
  private ArrayList<MovementModule> movementModules = new ArrayList<>();

  // DEBUGMODULES
  private ArrayList<DebugRendererModule> debugRendererModules = new ArrayList<>();


  // MouseModules
  private ArrayList<MouseSelectionModule> mouseSelectionModule = new ArrayList<>();



  public void addModules(Module... modules) {
    for (Module module : modules) {
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
      }
    }

  }



  /*
   * Needs to be expanded for each new Module
   * 
   */
  public ArrayList<Module> getAllModules() {
    ArrayList<Module> allModules = new ArrayList<>();
    allModules.addAll(selectionModules);
    allModules.addAll(physicsModules);
    allModules.addAll(simpleImageRenderModules);
    allModules.addAll(movementModules);
    
    //MouseModules
    allModules.addAll(mouseSelectionModule);

    // DebugModules
    allModules.addAll(debugRendererModules);
    return allModules;
  }



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


  // DebugModules
  public ArrayList<DebugRendererModule> getDebugRendererModules() {
    return debugRendererModules;
  }

  // MouseModules
  public ArrayList<MouseSelectionModule> getMouseSelectionModule() {
    return mouseSelectionModule;
  }
}