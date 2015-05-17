package ch.zombieInvasion.Eventhandling;

import java.util.ArrayList;

public class Event {
	private EventType event;
	private long delayMillis;
	private boolean persistent = false;
	private ArrayList<Object> additionalInfos;
	private Object additionalInfo;

	public Event(long delayMillis, EventType event, ArrayList<Object> additionalInfos) {
		this.event = event;
		this.delayMillis = delayMillis;
		this.additionalInfos = additionalInfos;
	}

	public Event(long delayMillis, EventType event, Object additionalInfo) {
		this.event = event;
		this.delayMillis = delayMillis;
		this.additionalInfo = additionalInfo;
	}

	public EventType getEvent() {
		return event;
	}

	public long getDelayMillis() {
		return delayMillis;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public ArrayList<Object> getAdditionalInfos() {
		return additionalInfos;
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
