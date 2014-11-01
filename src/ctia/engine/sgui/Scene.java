package ctia.engine.sgui;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class Scene extends JComponent {
	protected SceneHandler owner;

	public void link(SceneHandler owner) { this.owner = owner; }
	public void unlink() { this.owner = null; }
	// ought to be called after current scene is active
	public void begin() { } // used to set titles and add listeners
}
