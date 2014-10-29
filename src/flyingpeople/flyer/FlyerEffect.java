package flyingpeople.flyer;

import flyingpeople.core.Flyer;

public class FlyerEffect extends Flyer {
	protected Flyer owner; // the flyer that this effect is following/attached to
	protected double xoff, yoff;

	public FlyerEffect(Flyer owner, double xoff, double yoff) {
		super(owner.getZone(), owner.getPx() + xoff, owner.getPy() + yoff);
		this.owner = owner;
		this.xoff = xoff;
		this.yoff = yoff;
	}
	public void dt() { // doesn't need default flyer dt(), just follow owner
		this.px = owner.getPx() + xoff;
		this.py = owner.getPy() + yoff;
	}
}
