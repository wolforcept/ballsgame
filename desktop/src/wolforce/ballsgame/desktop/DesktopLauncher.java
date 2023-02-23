package wolforce.ballsgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import wolforce.ballsgame.BallsGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Title";
		cfg.useGL30 = true;
		cfg.width = 1820;
		cfg.height = 1024;
		new LwjglApplication(new BallsGame(), cfg);
	}
}
