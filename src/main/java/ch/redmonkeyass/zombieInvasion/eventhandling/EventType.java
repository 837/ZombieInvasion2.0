package ch.redmonkeyass.zombieInvasion.eventhandling;

public enum EventType {
  /*
   * General
   */
  TESTEVENT, AREA_SELECTION,
  /*
   * InputEvents
   */
  /*
   * Mouse
   */
  LEFT_CLICK, RIGHT_CLICK, LEFT_DOWN, RIGHT_DOWN, LEFT_RELEASED, RIGHT_RELEASED, LEFT_DRAGGED, RIGHT_DRAGGED, MOUSE_MOVED, LEFT_CLICK_SELECTION,
  /*
   * Keyboard
   */
  W_PRESSED, A_PRESSED, S_PRESSED, D_PRESSED, G_PRESSED, K_PRESSED, DEBUG_CONSOLE_KEY_F1_PRESSED, RIGHT_ARROW_PRESSED, LEFT_ARROW_PRESSED, UP_ARROW_PRESSED, DOWN_ARROW_PRESSED,
  /*
   * EntityEvents
   */
  KILL_ENTITY, REMOVE_ENTITY

}

