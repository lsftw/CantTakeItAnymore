package flyingpeople.data.level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import flyingpeople.data.Settings;
import flyingpeople.data.Utility;

public final class RandomLevelGenerator {
	protected static final int INTENSITY_PRINT_INTERVAL = Settings.valueInt("fps") * 10; // in ticks, print events occur at most this often
	protected static final int DELAY_VARIANCE = Settings.valueInt("fps"); // in ticks

	// TODO allow intensity limit to vary
	protected static final double INTENSITY_COEFFICIENT = 5.0; // higher = intensity raises faster
	protected static final double INTENSITY_LIMIT = DELAY_VARIANCE * 5; // intensity's peak, sharp drop upon reaching
	// intensity limit intentionally set higher than maximum delay, allows intense times to be lengthened

	private static final int NUMBER_ENEMIES = 4; // for Level's EventType enum, limits types of enemies that can be spawned

	private static final int LEVEL_DURATION = Settings.valueInt("fps")*60*60*5; // (ticks/sec * sec/min * min/hour) hour long match
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final Random rand = new Random(System.currentTimeMillis());

	public enum LevelStyle {
		NORMAL, SABOTEUR
	}

	static { // generates generated level directory
		File directory = new File("generated");
		directory.mkdir();
	}

	private RandomLevelGenerator() {};

	public static Level generateLevel(LevelStyle ls) { // TODO make file name have more readable name
		long time = System.currentTimeMillis();
		String levelFile = "generated/level_" + time + ".txt";

		FileWriter writer = null;
		try {
			writer = new FileWriter(levelFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// level generation time!
		try {
			writer.append("// Randomly generated level made on millisec time: " + time + LINE_SEPARATOR);
			writer.append("//  aka human-readable date: " + new Date(time) + LINE_SEPARATOR);
			writer.append("// Lasts for " + formatDuration(LEVEL_DURATION) + LINE_SEPARATOR);
			generateLevel(writer, ls);
		} catch (IOException ex) {
			ex.printStackTrace(); // TODO add pop up warnings
		}

		return Level.loadLevel(levelFile);
	}

	private static void generateLevel(FileWriter writer, LevelStyle ls) throws IOException {
		int curTick = 0;
		int sx, sy;
		int lastPrintTick = 0;
		double intensity = 0;
		// TODO measure intensity to determine delay and targetPowerBoost, and allow power drops [such as for bosses]
		// TODO add some sort of cumulative intensity that never drops
		String toAppend;
		int delay; // temp
		int enemyNum; // temp
		String enemy; // temp
		int powerLevel; // lower = higher chance of getting attributes
		int targetPowerBoost; // random
		String position; // temp
		String attributes; // temp

		while (curTick < LEVEL_DURATION) {
			toAppend = "";
			attributes = "";
			delay = (int)(rand.nextInt(DELAY_VARIANCE)+10 - intensity); // higher intensity = lower delay
			if (delay <= 0) delay = 1; // avoid negative and div/0
			if (ls == LevelStyle.SABOTEUR) {
				enemyNum = EventType.SABOTEUR.ordinal();
			} else {
				enemyNum = rand.nextInt(NUMBER_ENEMIES);
			}
			enemy = EventType.values()[enemyNum].toString().toLowerCase();
			if (enemy.equals("warlord")) {
				if (rand.nextInt(25) != 0) { // 4% chance to actually spawn warlord
					enemy = "fighter";
				}
			} else if (enemy.equals("hunter")) {
				if (rand.nextInt(25) != 0) { // 4% chance to actually spawn hunter
					enemy = "saboteur"; // TODO set to scout instead
				}
			}
			// power level and attributes
			// TODO make sizes automatically calculated? maybe
			if (enemy.equals("fighter")) {
				powerLevel = 100;
				sx = 30; sy = 30;
			} else if (enemy.equals("guardian")) {
				powerLevel = 600;
				sx = 40; sy = 27;
			} else if (enemy.equals("protector")) {
				powerLevel = 800;
				sx = 43; sy = 23;
			} else if (enemy.equals("warlord")) {
				powerLevel = 4000;
				sx = 86; sy = 90;
			} else if (enemy.equals("saboteur")) {
				powerLevel = 800;
				sx = 36; sy = 27;
			} else if (enemy.equals("sentinel")) {
				powerLevel = 700;
				sx = 45; sy = 30;
			} else if (enemy.equals("hunter")) {
				powerLevel = 5000;
				sx = 69; sy = 69;
			} else {
				powerLevel = 99999;
				sx = 1337; sy = 404;
				Utility.printError("Uhoh! Unrecognized enemy! (this shouldn't show up)");
			}
			targetPowerBoost = rand.nextInt(1000) - rand.nextInt(powerLevel / 4);
			if (targetPowerBoost > 300 && rand.nextInt(5) == 0) {
				if (attributes.length() > 0) attributes += ",";
				attributes += "durable";
				targetPowerBoost -= 300;
			}
			if (targetPowerBoost > 500 && rand.nextInt(4) == 0) {
				if (attributes.length() > 0) attributes += ",";
				attributes += "huge";
				targetPowerBoost -= 500;
				sx *= 3;
				sy *= 3;
			}
			// more likely to get later attributes to compensate for boost usage from earlier attributes
			if (targetPowerBoost > 700 && rand.nextInt(3) == 0) {
				if (attributes.length() > 0) attributes += ",";
				attributes += "fast";
				targetPowerBoost -= 700;
			}
			position = rand.nextInt(800 - sx) + "," + (-sy);

			curTick += delay;
			toAppend = curTick + " " + enemy + " " + position + (attributes.length() > 0 ? "," : "") + attributes;

			writer.append(toAppend + LINE_SEPARATOR);
			if (enemy.equals("warlord") || enemy.equals("hunter")) {
				if (rand.nextInt(5) != 0) { // 20% to not pause even though boss
					writer.append(curTick + " awaitbossdestruction " + LINE_SEPARATOR);
				} else {
					writer.append(curTick + " print Hey, " + enemy + ", we're not waiting. Let's go!" + LINE_SEPARATOR);
				}
			}
			//the lower delay was, the higher intensity becomes
			intensity += Math.sqrt(INTENSITY_COEFFICIENT / delay); // sqrt() to make delay of 1 & delay of 2 not change intensity TOO differently
			if (intensity >= INTENSITY_LIMIT) {
				writer.append(curTick + " print HAHAHAHA! Final intensity of " + intensity + "!" + LINE_SEPARATOR);
				intensity = 0;
			} else if (curTick - lastPrintTick > INTENSITY_PRINT_INTERVAL) {
				writer.append(curTick + " print Intensity level " + intensity + LINE_SEPARATOR);
				lastPrintTick = curTick;
			}
		}
		writer.close();
	}

	private static String formatDuration(int duration) {
		if (duration < 0) {
			return "NEGATIVE TIME OH NO SOMETHING WENT WRONG!";
		} else if (duration == 0) {
			return "Zero Time. Hooray.";
		}
		// now for more serious durations
		int durCount[] = {0,0,0,0,0,0,0};
		final int durLengths[] = {Settings.valueInt("fps")*60*60*24*365,Settings.valueInt("fps")*60*60*24*30,Settings.valueInt("fps")*60*60*24*7,
				Settings.valueInt("fps")*60*60*24,Settings.valueInt("fps")*60*60,Settings.valueInt("fps")*60,Settings.valueInt("fps")};
		final String durNames[] = {"year","month","week","day","hour","minute","second"};
		int endIndex = -1; // for printing "and"
		for (int i = 0; i < durLengths.length; i++) {
			if (duration / durLengths[i] > 0) {
				durCount[i] += duration / durLengths[i];
				duration = duration % durLengths[i];
				endIndex = i;
			}
		}
		if (duration > 0) endIndex = -1;
		// and now to format that info
		StringBuffer durStr = new StringBuffer();
		boolean needComma = false;
		for (int i = 0; i < durCount.length; i++) {
			if (durCount[i] > 0) {
				if (needComma) {
					durStr.append(", ");
					if (endIndex == i) {
						durStr.append("and ");
					}
				}
				durStr.append(durCount[i] + " " + durNames[i] + (durCount[i] > 1 ? "s" : ""));
				needComma = true;
			}
		}
		// and ticks
		if (duration > 0) {
			if (needComma) {
				durStr.append(", ");
				if (endIndex == -1) {
					durStr.append("and ");
				}
			}
			durStr.append(duration + " \"tick" + (duration > 1 ? "s\"" : "\""));
		}
		return durStr.toString();
	}
}
