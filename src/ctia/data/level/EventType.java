package ctia.data.level;

import ctia.core.Zone;
import ctia.data.Settings;
import ctia.data.Utility;
import ctia.entity.enemy.Enemy;
import ctia.scene.BattleScene;

// Used by Level
// * To add a new enemy: make a new enum with (true),
//    and add a case in triggerEnemy()
public enum EventType {
	// enemy
//	FIGHTER(true),
	// non-enemy
	AWAITBOSSDESTRUCTION(false),
	LEVELEND(false),
	TICKRESET(false),
	// debug/note
	PRINT(false);

	protected boolean isEnemy;
	private EventType(boolean isEnemy) {
		this.isEnemy = isEnemy;
	}
	// Triggering Events
	protected void trigger(LevelEvent event, Level level) {
		if (isEnemy) {
			triggerEnemy(event, level);
		} else {
			triggerEvent(event, level);
		}
	}
	// Enemy Events
	private void triggerEnemy(LevelEvent event, Level level) {
		Enemy e;
		//
		Zone zone = level.zone;
		int x = Integer.parseInt(event.parameters[0]);
		int y = Integer.parseInt(event.parameters[1]);

		if (Settings.valueBoolean("hud_on_top")) {
			y += BattleScene.HUD_HEIGHT;
		}

		switch(this) {
		// Stage 1
//		case FIGHTER:
//			e = new Fighter(zone, x, y);
//			break;

		default:
			Utility.printError("Unrecognized enemy event type \"" + event.type + "\" on tick #" + level.curTick);
			return; // return to avoid calling prepareAndAdd()
		}
//		level.prepareAndAdd(event, e);
	}
	// Non-Enemy Events
	private void triggerEvent(LevelEvent event, Level level) {
		switch(this) {
		case AWAITBOSSDESTRUCTION:
			level.await(AWAITBOSSDESTRUCTION);
			break;
		case LEVELEND:
			level.zone.endLevel();
			break;
		case TICKRESET:
			level.curTick = 0;
			break;
		case PRINT:
			if (Settings.valueBoolean("enable_print")) {
				System.out.println(event.parameters[0]);
			}
			break;

		default:
			Utility.printError("Unrecognized non-enemy event type \"" + event.type + "\" on tick #" + level.curTick);
			break;
		}
	}
}
