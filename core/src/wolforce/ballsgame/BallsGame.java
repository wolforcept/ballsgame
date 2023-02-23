package wolforce.ballsgame;

import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

import wolforce.ballsgame.screens.ScreenMain;
import wolforce.ballsgame.objs.Player;
import wolforce.ballsgame.screens.HasControllers;
import wolforce.ballsgame.screens.ScreenGame;

public class BallsGame extends Game {

	public static BallsGame game;
	public static GameStats stats;

	private HashMap<String, Player> players = new HashMap<>();;

	Screen mainMenu;

	@Override
	public void create() {

		mainMenu = new ScreenMain(this);

		game = this;
		stats = new GameStats();
		setScreen(mainMenu);

		Controllers.addListener(new ControllerAdapter() {
			@Override
			public boolean axisMoved(Controller controller, int axisIndex, float value) {
				Screen currScreen = getScreen();
				if (currScreen instanceof HasControllers) {
					return ((HasControllers) currScreen).axisMoved(controller, axisIndex, value);
				}
				return false;
			}
		});
	}

	public void startGame() {
		stats = new GameStats();
		setScreen(new ScreenGame(this, new GameController(this)));
	}

	public void gameEnd() {
		setScreen(mainMenu);
	}

	public Player getOrCreatePlayer(String id) {
		if (players.containsKey(id))
			return players.get(id);
		Player player = new Player();
		players.put(id, player);
		return player;
	}

	public Collection<Player> getPlayers() {
		return players.values();
	}

}
