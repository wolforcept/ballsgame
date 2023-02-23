package wolforce.ballsgame.objs;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.types.MonsterType;

public class Monster extends Obj {

	public MonsterType type;
	public int hp;
	public int exp = 5;
	public int damage = 20;
	public float speed = 1;

	public Monster(int hp, float x, float y, MonsterType type) {
		this.hp = hp;
		this.x = x;
		this.y = y;
		this.type = type;
		this.color1 = new Color(1f, 0, 0, 1f);
		this.color2 = new Color(0.8f, 0, 0, 1f);
		this.border = 2;
		this.size = 16;
	}

	@Override
	public void step(GameController game) {
		List<Player> players = game.getLivePlayers();
		if (players.size() == 0)
			return;
		super.step(game);
		Player cp = null; // closest player
		float dist = 10000;

		for (Player p : players) {
			float d = dist(p);
			if (d < dist) {
				cp = p;
				dist = d;
			}
		}
		if (cp != null) {
			type.monsterStep(game, this, cp, dist);
		}
	}

	@Override
	public void remove(GameController game) {
		if (Math.random() < 0.5)
			game.addOther(game.createRandomPickup(x, y));
		for (int i = 0; i < 10; i++)
			game.particle(x, y, size, color1);
	}
}
