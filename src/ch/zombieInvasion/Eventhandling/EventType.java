package ch.zombieInvasion.Eventhandling;

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
  LEFT_CLICK, RIGHT_CLICK, LEFT_DOWN, RIGHT_DOWN, LEFT_RELEASED, RIGHT_RELEASED, LEFT_DRAGGED, RIGHT_DRAGGED, LEFT_CLICK_SELECTION,
  /*
   * Keyboard
   */
  W_PRESSED, A_PRESSED, S_PRESSED, D_PRESSED, G_PRESSED, K_PRESSED, 
  /*
   * EntityEvents
   */
  KILL_ENTITY

}
