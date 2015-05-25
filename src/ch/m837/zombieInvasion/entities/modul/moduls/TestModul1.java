package ch.m837.zombieInvasion.entities.modul.moduls;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import ch.m837.zombieInvasion.World;
import ch.m837.zombieInvasion.entities.dataHandling.DataType;
import ch.m837.zombieInvasion.entities.modul.Modul;
import ch.m837.zombieInvasion.entities.modul.RenderableModul;
import ch.m837.zombieInvasion.entities.modul.UpdatableModul;
import ch.zombieInvasion.Eventhandling.Event;
import ch.zombieInvasion.Eventhandling.EventType;

public class TestModul1 extends Modul implements RenderableModul, UpdatableModul {

  public TestModul1(String entityID) {
    super(entityID);
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object getData(DataType dataType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void UPDATE(GameContainer gc, StateBasedGame sbg) {
    ArrayList<Event> events = World.getEntityHandler().getEventsFrom(getEntityID());
    
    events.parallelStream().filter(event -> event.getEvent() == EventType.TESTEVENT).findAny()
        .ifPresent(eventPresent -> {
          System.out.println(getEntityID() + " received Event from: " + eventPresent.getSenderID()
              + "  Loop: " + (int) eventPresent.getAdditionalInfo());
        });
  }

  @Override
  public void RENDER(GameContainer gc, StateBasedGame sbg, Graphics g) {
    // TODO Auto-generated method stub

  }
}
