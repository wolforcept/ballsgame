package wolforce.ballsgame.objs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import wolforce.ballsgame.GameController;

public abstract class Obj {

	public boolean remove = false;
	public float x, y, vx = 0, vy = 0;
	public float size, border;
	public Color color1 = null, color2 = null;

	public void setVelocity(double vx, double vy) {
		this.vx = (float) vx;
		this.vy = (float) vy;
	}

	public void step(GameController game) {
		x += vx;
		y += vy;
	}

	public void remove(GameController game) {
	}

	public float dist(Obj o) {
		return new Vector2(x, y).sub(o.x, o.y).len();
	}

	public void draw(GameController game, ShapeRenderer shape) {
	}

}
