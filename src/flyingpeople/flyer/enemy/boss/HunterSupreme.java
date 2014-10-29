package flyingpeople.flyer.enemy.boss;

import flyingpeople.core.Zone;
import flyingpeople.data.Settings;

public class HunterSupreme extends Hunter {
	public HunterSupreme(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
		scoreValue = 25000;
		maxSpeed = 6.5;
		eliminateClip = 10;
		minSnareDelay = Settings.valueInt("fps") / 3;
		snareDelay = Settings.valueInt("fps") * 2;
	}
}
