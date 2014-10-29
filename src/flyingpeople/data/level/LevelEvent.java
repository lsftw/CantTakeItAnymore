package flyingpeople.data.level;

// Used by Level
public class LevelEvent {
	public final long tick;
	public final EventType type;
	public final String[] parameters;

	public LevelEvent(long tick, EventType type,
			String ... parameters) {
		this.tick = tick;
		this.type = type;
		this.parameters = parameters;
	}

	public void trigger(Level level) {
		type.trigger(this, level);
	}
	public String toString() {
		return tick + " " + type;
	}
}
