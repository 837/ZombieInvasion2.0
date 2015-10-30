package ch.redmonkeyass.zombieInvasion.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;

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

      }

      @Override
      public void keyPressed(int arg0, char arg1) {
        switch (arg0) {
          case Input.KEY_W:
            WorldHandler.getEventDispatcher().createEvent(0, EventType.W_PRESSED, null, "INPUT_LISTENER",
                "GLOBAL");
            break;
          case Input.KEY_A:
            WorldHandler.getEventDispatcher().createEvent(0, EventType.A_PRESSED, null, "INPUT_LISTENER",
                "GLOBAL");
            break;
          case Input.KEY_S:
            WorldHandler.getEventDispatcher().createEvent(0, EventType.S_PRESSED, null, "INPUT_LISTENER",
                "GLOBAL");
            break;
          case Input.KEY_D:
            WorldHandler.getEventDispatcher().createEvent(0, EventType.D_PRESSED, null, "INPUT_LISTENER",
                "GLOBAL");
            break;
          case Input.KEY_G:
            WorldHandler.getEventDispatcher().createEvent(0, EventType.G_PRESSED, null, "INPUT_LISTENER",
                "GLOBAL");
            break;
          case Input.KEY_K:
            WorldHandler.getEventDispatcher().createEvent(0, EventType.K_PRESSED, null, "INPUT_LISTENER",
                "GLOBAL");
            break;
          case Input.KEY_F1:
            WorldHandler.getEventDispatcher().createEvent(0, EventType.DEBUG_CONSOLE_KEY_F1_PRESSED, null, "INPUT_LISTENER",
                "GLOBAL");
            break;
        }
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
        Vector2 position = new Vector2(arg1, arg2);

        if (arg0 == 0/* 0=LeftButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.LEFT_RELEASED, position.cpy(),
              "INPUT_LISTENER", "MOUSE");
        } else if (arg0 == 1/* 1=RightButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.RIGHT_RELEASED, position.cpy(),
              "INPUT_LISTENER", "MOUSE");
        }
      }

      @Override
      public void mousePressed(int arg0, int arg1, int arg2) {
        Vector2 position = new Vector2(arg1, arg2);

        if (arg0 == 0/* 0=LeftButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.LEFT_DOWN, position.cpy(),
              "INPUT_LISTENER", "MOUSE");
        } else if (arg0 == 1/* 1=RightButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.RIGHT_DOWN, position.cpy(),
              "INPUT_LISTENER", "MOUSE");
        }
      }

      @Override
      public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
        Vector2 oldPos = new Vector2(arg2, arg3);
        Vector2 newPos = new Vector2(arg0, arg1);
        Vector2[] positions = {oldPos.cpy(), newPos.cpy()};

        WorldHandler.getEventDispatcher().createEvent(0, EventType.MOUSE_MOVED, positions,
              "INPUT_LISTENER", "MOUSE");
        
      }

      @Override
      public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
        Vector2 oldPos = new Vector2(arg2, arg3);
        Vector2 newPos = new Vector2(arg0, arg1);
        Vector2[] positions = {oldPos.cpy(), newPos.cpy()};

        if (input.isMouseButtonDown(0)/* 0=LeftButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.LEFT_DRAGGED, positions,
              "INPUT_LISTENER", "MOUSE");
        } else if (input.isMouseButtonDown(1)/* 0=LeftButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.RIGHT_DRAGGED, positions,
              "INPUT_LISTENER", "MOUSE");
        }
      }

      @Override
      public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
        Vector2 position = new Vector2(arg1, arg2);

        if (arg0 == 0/* 0=LeftButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.LEFT_CLICK, position.cpy(),
              "INPUT_LISTENER", "GLOBAL");
        } else if (arg0 == 1/* 1=RightButton */) {
          WorldHandler.getEventDispatcher().createEvent(0, EventType.RIGHT_CLICK, position.cpy(),
              "INPUT_LISTENER", "GLOBAL");
        }
      }
    };
    input.addListener(listener);
  }
}
