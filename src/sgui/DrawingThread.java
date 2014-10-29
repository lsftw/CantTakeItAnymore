package sgui;

// a thread that continuously dts and redraws a simple gui
public class DrawingThread extends Thread {
	public static final long NANOSECONDS_TO_SECONDS = 1000000000;
	public static final long FPS_COUNT_RESET_INTERVAL = 50; // in frames

	protected BasicSGui instance;
	protected TimeTracker timer = new TimeTracker(0);

	protected long countFrames = 0;
	protected long duration = 0;
	protected long frameResetCount = FPS_COUNT_RESET_INTERVAL;

	public DrawingThread(BasicSGui instance) {
		this.instance = instance;
	}

	public void start() {
		super.start();
		instance.redraw();
	}
	// Constantly update screen image
	public void run() {
		long timeBefore, timeNow;
		while (true) {
			timeNow = System.nanoTime();
			timeBefore = timeNow;
			if (timer.isReady()) {
				timer.awaitNext(timeNow);

				instance.dt();
				instance.redraw();

				countFrames++;
			}
			duration += System.nanoTime() - timeBefore;
			if (countFrames >= frameResetCount) {
				averageFpsCount();
			}
		}
	}
	protected void averageFpsCount() {
		countFrames = calcFps();
		duration = NANOSECONDS_TO_SECONDS;
		frameResetCount = countFrames + FPS_COUNT_RESET_INTERVAL;
	}

	public long calcFps() {
		if (duration != 0) {
			return countFrames * NANOSECONDS_TO_SECONDS / duration;
		}
		return 0;
	}

	public void setFpsCap(long fpsCap) {
		if (fpsCap != 0) {
			timer.setInterval(NANOSECONDS_TO_SECONDS / fpsCap);
		} else {
			timer.setInterval(Long.MAX_VALUE);
		}
	}
	public long getFpsCap() {
		long interval = timer.getInterval();
		if (interval != 0) {
			return NANOSECONDS_TO_SECONDS / interval;
		}
		return Long.MAX_VALUE; // can't return positive infinity
	}

	// uses nanoseconds for all parameters and fields
	protected static class TimeTracker {
		protected long timeLast = 0; // starts out ready
		protected long interval;

		public TimeTracker(long interval) {
			this.interval = interval;
		}

		public boolean isReady() {
			return System.nanoTime() >= timeLast + interval;
		}
		public void awaitNext(long timeNow) {
			timeLast = timeNow;
		}

		public void setInterval(long newInterval) {
			interval = newInterval;
		}
		public long getInterval() {
			return interval;
		}
	}
}
