package ch.m837.zombieInvasion.input;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.state.StateBasedGame;

import com.badlogic.gdx.math.Vector2;

import ch.zombieInvasion.Eventhandling.EventDispatcher;
import ch.zombieInvasion.Eventhandling.EventType;

public class InputHandler {
  public InputHandler(GameContainer gc) {
    Input input = gc.getInput();
    addListener(input);
  }

  public void UPDATE(GameContainer gc, StateBasedGame sbg) {

  }

  private void addListener(Input input) {
    InputListener listener = new InputListener() {

      @Override
      public void controllerUpReleased(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerUpPressed(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerRightReleased(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerRightPressed(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerLeftReleased(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerLeftPressed(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerDownReleased(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerDownPressed(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerButtonReleased(int arg0, int arg1) {
        // TODO Auto-generated method stub

      }

      @Override
      public void controllerButtonPressed(int arg0, int arg1) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyReleased(int arg0, char arg1) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyPressed(int arg0, char arg1) {
        // TODO Auto-generated method stub

      }

      @Override
      public void setInput(Input arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public boolean isAcceptingInput() {
        /*
         * Must be set to true to receive input
         */
        return true;
      }

      @Override
      public void inputStarted() {
        // TODO Auto-generated method stub

      }

      @Override
      public void inputEnded() {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseWheelMoved(int arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseReleased(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mousePressed(int arg0, int arg1, int arg2) {
        if (arg0 == 0/* 0=LeftButton */) {
          EventDispatcher.createEvent(0, EventType.LEFT_CLICK, new Vector2(arg1, arg2),
              "INPUT_LISTENER", "GLOBAL");
        } else if (arg0 == 1/* 1=RightButton */) {
          EventDispatcher.createEvent(0, EventType.RIGHT_CLICK, new Vector2(arg1, arg2),
              "INPUT_LISTENER", "GLOBAL");
        }
      }

      @Override
      public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

      }
    };
    input.addListener(listener);
  }
}
