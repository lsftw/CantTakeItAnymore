package ctia.display;

import ctia.core.Entity;

public class AbilityMessage extends FadeMessage {
	public AbilityMessage(Entity entity, int posIndex, String text, int duration) {
		this(entity, posIndex, text, duration, true);
	}
	public AbilityMessage(Entity entity, int posIndex, String text, int duration, boolean showBelow) {
		super(text, (int)entity.getPx(),
				(int)entity.getPy()
				+ MessageDisplay.getInstance().getFontHeight() * posIndex
				+ (showBelow ? entity.getSy() : 0),
				duration);
	}
}
