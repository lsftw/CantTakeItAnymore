package ctia.data.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import ctia.core.Zone;
import ctia.data.SequentialStreamReader;
import ctia.data.Utility;
import ctia.display.Art;
import ctia.entity.enemy.Enemy;

// Determines what happens at how many ticks in a Zone,
//  mapping tick counts to events
// Also keeps track of all the Levels
public final class Level {
	public static final String LEVEL_LIST_FILEPATH = "data/levellist.txt";

	protected static Map<String, Level> levelsByName = new HashMap<String, Level>();
	protected static List<Level> allLevels = new ArrayList<Level>(); // used for getting next level
	private static LevelReader levelReader;
	// Instance Fields
	String name;
	protected Zone zone;
	LevelEvent[] events;
	private int curEvent = 0;
	protected long curTick = 0;
	// Tick Freezing
	private boolean tickFrozen = false; // stop ticking until a wait is complete
	private boolean waitBoss = false; // stop ticking until a boss is destroyed

	static {
		loadLevels();
	}

	Level() { }

	public static Level getLevel(String levelName) {
		return levelsByName.get(levelName);
	}
	public static Level getNextLevel(Level currentLevel) {
		int curIndex = allLevels.indexOf(currentLevel);
		if (curIndex == -1 || curIndex == allLevels.size() - 1) return null; // no such level/last level

		return allLevels.get(curIndex + 1);
	}
	public void link(Zone zone) {
		this.zone = zone;
	}
	public void unlink() {
		this.zone = null;
	}
	public void tick() {
		if (!tickFrozen) { // events on current tick aren't triggered when a tick is frozen
			curTick++;
			while (curEvent < events.length && events[curEvent].tick <= curTick) {
				events[curEvent].trigger(this);
				curEvent++;
				if (tickFrozen) {
					break;
				}
			}
		}
	}
	public void reset() {
		curTick = 0;
		curEvent = 0;
	}
	protected void prepareAndAdd(LevelEvent event, Enemy e) { // helper of trigger(LevelEvent)
		// prepares attributes and adds to zone
		for (int i = 2; i < event.parameters.length; i++) {
			e.morphAttribute(event.parameters[i]);
		}
		e.applyAttributes();
		zone.addentity(e);
	}
	protected void await(EventType awaitType) {
		tickFrozen = true;
		switch (awaitType) {
		case AWAITBOSSDESTRUCTION:
			waitBoss = true;
			break;
		default:
			Utility.printError("Invalid await tick event type \"" + awaitType + "\" on tick #" + curTick);
			break;
		}
	}
	public void bossDestroyed() {
		if (waitBoss) {
			waitBoss = false;
			if (tickFrozen) {
				tickFrozen = false;
			}
		}
	}
	public String toString() {
		return name;
	}

	private static void loadLevels() {
		SequentialStreamReader levelsListReader = new LevelListReader(new File(LEVEL_LIST_FILEPATH));
		try {
			levelsListReader.readStream();
		} catch (FileNotFoundException ex) {
			try {
				if (Art.isRunningInJar()) {
					levelsListReader = new LevelListReader(Level.class.getResourceAsStream("/" + LEVEL_LIST_FILEPATH));
					levelsListReader.readStream();
					return;
				}
			} catch (FileNotFoundException e) { }
			JOptionPane.showMessageDialog(null, "Level list file missing from " + LEVEL_LIST_FILEPATH + "! " +
					"Game cannot load and will now exit. Perhaps the data folder is missing?",
					"Missing Level List File", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	public static Level loadLevel(String levelFile) { // load a Level from a level file
		levelReader = new LevelReader(new File(levelFile));
		try {
			levelReader.readStream();
		} catch (FileNotFoundException ex) {
			try {
				if (Art.isRunningInJar()) {
					levelReader = new LevelReader(Level.class.getResourceAsStream("/" + levelFile));
					levelReader.readStream();
					return levelReader.getLastLoadedLevel();
				}
			} catch (FileNotFoundException e) { }
			JOptionPane.showMessageDialog(null, "Level file missing from " + levelFile + "!" +
					"Game cannot load and will now exit. Perhaps the data/level folder is missing?",
					"Missing Level File", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return levelReader.getLastLoadedLevel();
	}
}
