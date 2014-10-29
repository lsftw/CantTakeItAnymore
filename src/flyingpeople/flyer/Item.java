package flyingpeople.flyer;

import flyingpeople.core.Flyer;
import flyingpeople.core.Zone;

// TODO implement items
public abstract class Item extends Flyer {
	public Item(Zone container, double xpos, double ypos) {
		super(container, xpos, ypos);
	}
}
