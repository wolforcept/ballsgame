package wolforce.ballsgame.screens;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import wolforce.ballsgame.BallsGame;
import wolforce.ballsgame.objs.Player;

public class ScreenMain extends ScreenAdapter implements HasControllers {

	private static final float border = Gdx.graphics.getHeight() * .02f;
	private static final float playerBoxHeight = Gdx.graphics.getHeight() * .15f;

	private BallsGame game;
	ShapeRenderer shape;
	SpriteBatch batch;

	Skin skin;
	Stage stage;

	public ScreenMain(BallsGame game) {
		this.game = game;
		this.shape = new ShapeRenderer();
	}

	@Override
	public void show() {

		skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		float x = border;

		x += createButton("Start Game", x, border, () -> {
			BallsGame.game.startGame();
		}).getWidth() + border;

		x += createButton("Options", x, border, () -> {
		}).getWidth() + border;

		x += createButton("Exit", x, border, () -> {
			System.exit(0);
		}).getWidth() + border;
	}

	private TextButton createButton(String text, float x, float y, Runnable runnable) {
		TextButton button = new TextButton(text, skin);
		button.setPosition(x, y);
		button.addListener(event -> {
			if (event instanceof ChangeEvent)
				runnable.run();
			return true;
		});
		stage.addActor(button);
		return button;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(delta);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		Collection<Player> players = game.getPlayers();
		int nPlayers = players.size();

		shape.begin(ShapeType.Filled);
		if (nPlayers > 0) {
			float div = w * .95f / nPlayers;
			float border = w * .05f;
			float width = div - border;
			float x = border;
			for (Player player : players) {
				renderPlayer(player, x, h - border - playerBoxHeight, width, playerBoxHeight);
				x += width + border;
			}
		}
		shape.end();

		stage.draw();
	}

	private void renderPlayer(Player player, float x, float y, float w, float h) {
		shape.setColor(player.color1);
		shape.rect(x, y, w, h);

		if (Math.abs(player.axis[0]) > 0.5) {
			float[] hsv = player.color1.toHsv(new float[] { 0f, 0f, 0f });
			hsv[0] += player.axis[0] * 1.5;
			if (hsv[0] > 360)
				hsv[0] -= 360;
			if (hsv[0] < 0)
				hsv[0] += 360;
			player.color1 = new Color(1, 1, 1, 1f).fromHsv(hsv);
		}

		if (Math.abs(player.axis[1]) > 0.5) {
			float[] hsv = player.color1.toHsv(new float[] { 0f, 0f, 0f });

			if (hsv[2] == 1) {
				hsv[1] += player.axis[1] * .01;
				if (hsv[1] > 1)
					hsv[1] = 1;
				if (hsv[1] < .2)
					hsv[1] = 0.2f;
			}

			if (hsv[1] == 1) {
				hsv[2] -= player.axis[1] * .01;
				if (hsv[2] > 1)
					hsv[2] = 1;
				if (hsv[2] < 0.2)
					hsv[2] = 0.2f;
			}

			player.color1 = new Color(1, 1, 1, 1f).fromHsv(hsv);

		}

	}

	@Override
	public boolean axisMoved(Controller controller, int axis, float value) {
		String id = controller.getUniqueId();
		Player player = game.getOrCreatePlayer(id);
		player.axis[axis] = value;
		return false;
	}

}
