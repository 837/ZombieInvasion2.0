package ch.m837.zombieInvasion.entities.modul;

import java.util.ArrayList;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.modul.moduls.TestModul1;
import ch.m837.zombieInvasion.entities.modul.moduls.TestModul2;

public class ModulHandler {
  private ArrayList<TestModul1> testModuls1 = new ArrayList<>();
  private ArrayList<TestModul2> testModuls2 = new ArrayList<>();

  public void addModuls(Modul... moduls) {
    for (int i = 0; i < moduls.length; i++) {
      Modul modul = moduls[i];
      World.getEntityHandler().addModulToEntity(modul);

      if (modul instanceof TestModul1) {
        testModuls1.add((TestModul1) modul);
      } else if (modul instanceof TestModul2) {
        testModuls2.add((TestModul2) modul);
      }
    }
  }

  public ArrayList<Modul> getAllModuls() {
    ArrayList<Modul> allModuls = new ArrayList<>();
    allModuls.addAll(testModuls1);
    allModuls.addAll(testModuls2);
    return allModuls;
  }

  public ArrayList<TestModul1> getTestModuls1() {
    return testModuls1;
  }

  public ArrayList<TestModul2> getTestModuls2() {
    return testModuls2;
  }

}
