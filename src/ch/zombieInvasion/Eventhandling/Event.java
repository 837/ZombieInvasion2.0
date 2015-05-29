package ch.zombieInvasion.Eventhandling;

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

  public Object getAdditionalInfo() {
    return additionalInfo;
  }

  public void setDelayMillis(long delayMillis) {
    this.delayMillis = delayMillis;
  }

  public void setPersistent(boolean persistent) {
    this.persistent = persistent;
  }

}
