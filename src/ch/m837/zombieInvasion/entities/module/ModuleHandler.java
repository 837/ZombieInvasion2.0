package ch.m837.zombieInvasion.entities.module;

import java.util.ArrayList;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.module.modules.PhysicsModule;
import ch.m837.zombieInvasion.entities.module.modules.SelectionModule;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class ModuleHandler {
  /*
   * Needs to be expanded for each new Module
   * 
   */
  private ArrayList<SelectionModule> selectionModules = new ArrayList<>();
  private ArrayList<PhysicsModule> physicsModules = new ArrayList<>();

  public void addModules(Module... modules) {
    for (int i = 0; i < modules.length; i++) {
      Module module = modules[i];
      World.getEntityHandler().addModulToEntity(module);

      /*
       * Needs to be expanded for each new Module
       * 
       */
      if (module instanceof SelectionModule) {
        selectionModules.add((SelectionModule) module);
      } else if (module instanceof PhysicsModule) {
        physicsModules.add((PhysicsModule) module);
      }
    }
  }


  public void addSimplePhysicsModuleExample(){
    Vector2 position = Vector2.Zero;

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody; //others: static body, kinetic body (means it can move but will not react to forces)
    bodyDef.allowSleep = true; // redundant - i think default is already true...
    bodyDef.bullet = false;// redundant - set to true if object is supposed to move very very fast
    bodyDef.fixedRotation = false;// redundant,
    bodyDef.position.set(position);

    //add body to World
    Body body = World.getB2World().createBody(bodyDef);

    FixtureDef fixture = new FixtureDef();
    fixture.isSensor = false; //sensors register collissions but they're not affected by forces
    fixture.shape = new CircleShape();
    fixture.shape.setRadius(2);
    fixture.density = 1;
    fixture.friction = 0.1f;

    body.createFixture(fixture); //bodies can have multiple shapes

    PhysicsModule p = new PhysicsModule("bla",body);

  }

  /*
   * Needs to be expanded for each new Module
   * 
   */
  public ArrayList<Module> getAllModules() {
    ArrayList<Module> allModules = new ArrayList<>();
    allModules.addAll(selectionModules);
    allModules.addAll(physicsModules);
    return allModules;
  }


  public ArrayList<PhysicsModule> getPhysicsModules() {
    return physicsModules;
  }

  public ArrayList<SelectionModule> getSelectionModules() {
    return selectionModules;
  }
}
