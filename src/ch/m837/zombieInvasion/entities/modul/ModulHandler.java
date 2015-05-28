package ch.m837.zombieInvasion.entities.modul;

import java.util.ArrayList;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.modul.moduls.PhysicModul;
import ch.m837.zombieInvasion.entities.modul.moduls.SelectionModul;

public class ModulHandler {
  /*
   * Needs to be expanded for each new Modul
   * 
   */
  private ArrayList<SelectionModul> selectionModuls = new ArrayList<>();
  private ArrayList<PhysicModul> physicsModuls = new ArrayList<>();

  public void addModuls(Modul... moduls) {
    for (int i = 0; i < moduls.length; i++) {
      Modul modul = moduls[i];
      World.getEntityHandler().addModulToEntity(modul);

      /*
       * Needs to be expanded for each new Modul
       * 
       */
      if (modul instanceof SelectionModul) {
        selectionModuls.add((SelectionModul) modul);
      } else if (modul instanceof PhysicModul) {
        physicsModuls.add((PhysicModul) modul);
      }
    }
  }

  /*
   * Needs to be expanded for each new Modul
   * 
   */
  public ArrayList<Modul> getAllModuls() {
    ArrayList<Modul> allModuls = new ArrayList<>();
    allModuls.addAll(selectionModuls);
    allModuls.addAll(physicsModuls);
    return allModuls;
  }


  public ArrayList<PhysicModul> getPhysicsModuls() {
    return physicsModuls;
  }

  public ArrayList<SelectionModul> getSelectionModuls() {
    return selectionModuls;
  }
}
