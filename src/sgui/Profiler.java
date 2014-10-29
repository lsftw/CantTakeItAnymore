package sgui;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Profiler extends Thread {
	private volatile boolean terminateRequest;
	private final long profileInterval = 2000;
	private final int maxDepth = 5;

	public void run() {
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		try {
			while (!terminateRequest) {
				long[] threadIds = threadBean.getAllThreadIds();
				ThreadInfo[] infs = threadBean.getThreadInfo(threadIds, maxDepth);

				for (ThreadInfo inf : infs) {
					StackTraceElement[] str = inf.getStackTrace();
					if (str == null)
						continue;
					StackTraceElement el = mostInterestingElement(str);
					if (el != null)
						incrementCountOf(el);
				}
				dumpinfo();
				Thread.sleep(profileInterval);
			}
		} catch (InterruptedException iex) {
			Thread.currentThread();
			Thread.interrupted();
			throw new RuntimeException("Profiler interrupted");
		}
	}
	private StackTraceElement mostInterestingElement(StackTraceElement[] st) {
		for (int n = st.length,i=0; i < n; i++) {
			StackTraceElement el = st[i];
			if (el.getLineNumber() >= 0)
				return el;
		}
		return null;
	}

	private static class ProfileInfo {
		private int tickCount;
	}

	private Map<StackTraceElement,ProfileInfo> lineCounts =
			new HashMap<StackTraceElement,ProfileInfo>(500);

	private void incrementCountOf(StackTraceElement el) {
		ProfileInfo inf = lineCounts.get(el);
		if (inf == null) {
			lineCounts.put(el, inf = new ProfileInfo());
		}
		inf.tickCount++;
	}

	public void dumpinfo() {
		System.out.println("===== ===== PROFILER INFO DUMP ===== =====");
		Set<StackTraceElement> ste = lineCounts.keySet();
		for (StackTraceElement element : ste) {
			System.out.println(element);
			System.out.println("encountered " +  lineCounts.get(element).tickCount + " times");
		}
	}
	public void stopAsap() {
		terminateRequest = true;
	}
}
