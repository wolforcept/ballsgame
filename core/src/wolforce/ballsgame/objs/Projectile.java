package wolforce.ballsgame.objs;

import wolforce.ballsgame.BallsGame;
import wolforce.ballsgame.GameController;

public class Projectile extends Obj {

	public final Player player;
	public int damage;
	public int age = 0, maxAge;

	public Projectile(Player player, float x, float y, float size) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.size = size;
		BallsGame.stats.bulletsFired++;
	}

	@Override
	public void step(GameController game) {
		super.step(game);
		this.age += player.getBulletSpeed();
		if (age >= maxAge) {
			remove = true;
			return;
		}

		if (damage > 0)
			for (Monster m : game.monsters) {
				if (this.dist(m) < this.size + m.size) {
					player.hurtMonster(this, m);
					this.remove = true;
					return;
				}
			}
		// game.particle(30, x, y, (float) (Math.random() - 0.5), (float) (Math.random()
		// - 0.5), color1, 3);
	}
}
