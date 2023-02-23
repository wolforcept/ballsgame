package wolforce.ballsgame.objs.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Obj;
import wolforce.ballsgame.objs.Player;

public abstract class Pickupable extends Obj {

	private static final int MAX_AGE = 1000;
	private int age = 0;
	private int unpickable = 60;

	public Pickupable(float x, float y) {
		this.x = x;
		this.y = y;
		this.setVelocity(Math.random() * 2 - 1, Math.random() * 2 - 1);
	}

	@Override
	public void draw(GameController game, ShapeRenderer shape) {
		drawUnder(game, shape);
		if (unpickable > 0) {
			shape.setColor(1, 1, 1, unpickable / 60f);
			Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND); // Or GL20
			shape.circle(x, y, size + border);
		}
	}

	@Override
	public void step(GameController game) {
		super.step(game);
		vx *= 0.95;
		vy *= 0.95;
		age++;
		if (age >= MAX_AGE) {
			remove = true;
			return;
		}
		if (unpickable > 0) {
			unpickable--;
			return;
		}
		for (Player p : game.getLivePlayers()) {
			if (dist(p) < size + p.size) {
				onPickup(game, p);
				remove = true;
			}
		}
	}

	protected void drawUnder(GameController game, ShapeRenderer shape) {
		Texture tex = getTexture(game);
		game.drawImage(tex, x - tex.getWidth() / 2, y - tex.getHeight() / 2);
	}

	protected abstract void onPickup(GameController game, Player p);

	protected abstract Texture getTexture(GameController game);

}
