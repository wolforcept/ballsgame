package wolforce.ballsgame.objs;

import com.badlogic.gdx.graphics.Color;

import wolforce.ballsgame.GameController;

public class Particle extends Obj {
	int age = 0, maxAge;
	float r, g, b;

	public Particle(int age, float x, float y, float vx, float vy, float r, float g, float b, int size) {
		this.age = 0;
		this.maxAge = age;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.r = r;
		this.g = g;
		this.b = b;
		this.size = size;
	}

	@Override
	public void step(GameController game) {
		super.step(game);
		this.vx *= 0.95;
		this.vy *= 0.95;
		age++;
		if (Math.random() < 0.5)
			age++;
		if (age >= maxAge)
			remove = true;
	}

	public Color getColor() {
		return new Color(r, g, b, (maxAge - age) / (float) maxAge);
	}
}
