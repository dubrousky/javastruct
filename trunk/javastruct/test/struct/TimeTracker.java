package struct;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Global Chronometer Class.
 * 
 */
public class TimeTracker {

	public static int MAX_TIMETRACKER_USERS = 100;
	private static long BILLION = 1000000000;
	private static long MILLION = 1000000;
	private static DecimalFormat formatter = new DecimalFormat("#.####");
	private static HashMap<String, NanoTimerElement> users = new HashMap<String, NanoTimerElement>();

	public static void startClock(String name) {
		if (users.size() > MAX_TIMETRACKER_USERS) {
			return;
		}
		if (users.get(name) != null) {
			return;
		}
		NanoTimerElement timer = new NanoTimerElement(name);
		users.put(name, timer);
	}

	public static void resetClock(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return;
		timer.reset();
	}

	public static long getElapsedTime(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return -1;
		timer.refresh();
		return timer.getElapsedTime();
	}

	public static long getElapsedTimeMillis(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return -1;
		timer.refresh();
		return timer.getElapsedTime();
	}

	public static long getTimeDelta(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return -1;
		timer.refresh();
		return timer.getDiff();
	}

	public static long getTimeDeltaMillis(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return -1;
		timer.refresh();
		return timer.getDiff() / MILLION;
	}

	public static String getElapsedTimeString(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return "Invalid chronometer: " + name;
		;
		timer.refresh();
		return "Delta: " + formatter.format((double) timer.getDiff() / BILLION)
				+ " s. Elapsed: "
				+ formatter.format((double) timer.getElapsedTime() / BILLION)
				+ " s.";
	}

	public static String getElapsedTimeStringAsMicros(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return "Invalid chronometer: " + name;
		;
		timer.refresh();
		return "Delta: " + timer.getDiff() / 1000 + "mics. Elapsed: "
				+ timer.getElapsedTime() / 1000 + " mics.";
	}

	public static String getElapsedTimeStringAsNanos(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return "Invalid chronometer: " + name;
		;
		timer.refresh();
		return "Delta: " + timer.getDiff() / 1000 + "nanos. Elapsed: "
				+ timer.getElapsedTime() + " nanos.";
	}

	public static String getElapsedTimeStringAsMillis(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return "Invalid chronometer: " + name;
		;
		timer.refresh();
		return "Delta: " + timer.getDiff() / MILLION + "ms. Elapsed: "
				+ timer.getElapsedTime() / MILLION + " ms.";
	}

	public static long getItemCountPerSecond(String name, long elements) {
		NanoTimerElement timer = users.get(name);
		timer.refresh();
		if (timer == null)
			return -1;
		if (elements < 0)
			return -1;
		if (timer.getElapsedTime() == 0)
			return -1;
		long items = (BILLION * elements) / timer.getElapsedTime();
		return items;
	}

	public static String stopClock(String name) {
		NanoTimerElement timer = users.get(name);
		if (timer == null)
			return name + " : Invalid chronometer";
		timer.refresh();
		users.remove(name);
		return "" + (double) timer.elapsedTime / BILLION + "s." + "("
				+ timer.elapsedTime / MILLION + " ms)";
	}
}

class NanoTimerElement {
	String name;
	long startTime = 0;
	long stopTime = 0;
	long lastTime = 0;
	long creationTime = 0;
	long elapsedTime = 0;
	long diff = 0;

	public NanoTimerElement(String name) {
		creationTime = System.nanoTime();
		startTime = creationTime;
		lastTime = creationTime;
		this.name = name;
	}

	public void refresh() {
		diff = System.nanoTime() - lastTime;
		lastTime = System.nanoTime();
		elapsedTime = lastTime - startTime;
	}

	public void reset() {
		creationTime = System.currentTimeMillis();
		startTime = creationTime;
		lastTime = creationTime;
	}

	public long getDiff() {
		return diff;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public long getLastTime() {
		return lastTime;
	}

	public String getName() {
		return name;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
	}
}
