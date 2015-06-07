package ch.zombieInvasion.Eventhandling;

import java.util.Optional;

import com.badlogic.gdx.math.Vector2;

public class Event {
  private EventType event;
  private long delayMillis;
  private boolean persistent = false;
  private Object additionalInfo;
  private String senderID;
  private String receiverID;

  public Event(long delayMillis, EventType event, Object additionalInfo, String senderID,
      String receiverID) {
    this.event = event;
    this.delayMillis = delayMillis;
    this.additionalInfo = additionalInfo;
    this.senderID = senderID;
    this.receiverID = receiverID;
  }

  public EventType getEvent() {
    return event;
  }

  public String getSenderID() {
    return senderID;
  }

  public String getReceiverID() {
    return receiverID;
  }

  public long getDelayMillis() {
    return delayMillis;
  }

  public boolean isPersistent() {
    return persistent;
  }

  /**
   * Return the additional Info from an Event
   * 
   * Special cases: Vector2: returns a copy of vector, not the actual vector.
   * 
   * @param the type you want
   * @return Optional<type> or an empty Optional if the type does not match the found object
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getAdditionalInfo(Class<T> type) {
    if (additionalInfo.getClass().equals(type)) {
      if (type.equals(Vector2.class)) {
        return (Optional<T>) Optional.ofNullable(((Vector2) additionalInfo).cpy());
      }
      return Optional.ofNullable(type.cast(additionalInfo));
    }
    return Optional.empty();
  }

  public void setDelayMillis(long delayMillis) {
    this.delayMillis = delayMillis;
  }

  public void setPersistent(boolean persistent) {
    this.persistent = persistent;
  }



}
