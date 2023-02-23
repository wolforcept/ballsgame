package wolforce.ballsgame.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;

import wolforce.ballsgame.BallsGame;
import wolforce.ballsgame.GameController;

public class ScreenGame extends ScreenAdapter implements HasControllers {

	GameController controller;
	BallsGame game;

	public ScreenGame(BallsGame game, GameController controller) {
		super();
		this.game = game;
		this.controller = controller;
	}

	@Override
	public void render(float delta) {
		controller.render();

	}

	@Override
	public void dispose() {
		controller.dispose();
	}

	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		return this.controller.axisMoved(controller, axisIndex, value);
	}
}
