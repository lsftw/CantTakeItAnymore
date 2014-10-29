package flyingpeople.data;

import flyingpeople.core.Zone;

// TODO finish implementing array-based level cache for simulating flyer size etc
public final class ZoneSimulator {
	public static final int MAX_CONCURRENT_SIMULATIONS = 1;
	private static ZoneSim[] zones = new ZoneSim[MAX_CONCURRENT_SIMULATIONS];
	//
	private ZoneSimulator() { }
	//
	public ZoneSim getZoneSim() {
		ZoneSim zone;
		while ((zone = tryGetZoneSim()) == null); // forgive me
		return zone;
	}
	private ZoneSim tryGetZoneSim() {
		for (int i = 0; i < zones.length; i++) {
			if (!zones[i].locked) {
				zones[i].locked = true;
				return zones[i];
			}
		}
		return null;
	}
	public void releaseZoneSim(ZoneSim zone) { // release a zone when you are done with it to clear it out
		for (int i = 0; i < zones.length; i++) {
			if (zones[i] == zone) {
				zone.reset();
				return;
			}
		}
		// TODO error? if that zone didn't belong or just silently ignore
	}



	// Zone simulation
	public static class ZoneSim {
		protected static int zonesMade = 0;

		protected Zone zone; // TODO zone initialization
		protected boolean locked = false;
		protected int id;

		public ZoneSim() {
			id = zonesMade++;
		}

		public Zone getZone() { return zone; }
		public void reset() {
			//zone = new Zone();
			locked = false;
			// TODO Auto-generated method stub
		}
	}
}
