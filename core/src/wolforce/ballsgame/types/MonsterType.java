package wolforce.ballsgame.types;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Monster;
import wolforce.ballsgame.objs.Player;

public enum MonsterType {
	NORMAL, BIG {

		@Override
		public void create(GameController game, Monster m) {
			m.size *= 1.5;
			m.damage *= 2.0;
			m.speed *= .5;
		}
	},
	FAST {

		@Override
		public void create(GameController game, Monster m) {
			m.size *= .66;
			m.damage *= .5;
			m.speed *= 2;
		}
	};

	public void create(GameController game, Monster m) {
	};

	public void monsterStep(GameController game, Monster m, Player p, float dist) {
		if (dist < p.size + m.size) {
			p.hurtPlayer(p, (int) (m.damage * 1.5));
			m.remove = true;
			return;
		}
		double dir = Math.atan2(p.y - m.y, p.x - m.x);
		m.vx = (float) Math.cos(dir) * m.speed;
		m.vy = (float) Math.sin(dir) * m.speed;
	}

	public static MonsterType getRandom(GameController game) {
		double r = Math.random();
		if (r < .6)
			return MonsterType.FAST;
		if (r < .8)
			return MonsterType.NORMAL;
		return MonsterType.BIG;
	}
}
