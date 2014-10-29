package flyingpeople.display;

import flyingpeople.core.Flyer;

public class AbilityMessage extends FadeMessage {
	public AbilityMessage(Flyer flyer, int posIndex, String text, int duration) {
		this(flyer, posIndex, text, duration, true);
	}
	public AbilityMessage(Flyer flyer, int posIndex, String text, int duration, boolean showBelow) {
		super(text, (int)flyer.getPx(),
				(int)flyer.getPy()
				+ MessageDisplay.getInstance().getFontHeight() * posIndex
				+ (showBelow ? flyer.getSy() : 0),
				duration);
	}
}
