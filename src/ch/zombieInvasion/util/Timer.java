package ch.zombieInvasion.util;

public class Timer {
	private long startTime;

	public Timer() {
		restart();
	}

	public void restart() {
		startTime = System.nanoTime();
	}

	public double getSeconds() {
		return (System.nanoTime() - startTime)/1000000000.0;
	}
}
