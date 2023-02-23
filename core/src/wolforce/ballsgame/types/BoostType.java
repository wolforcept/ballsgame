package wolforce.ballsgame.types;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Texture;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Player;

public enum BoostType {
	SPEED(g -> g.imgSpeed), RANGE(g -> g.imgRange), DAMAGE(g -> g.imgDamage), BULLET_SPEED(g -> g.imgBulletSpeed),
	ATTACK_SPEED(g -> g.imgAttackSpeed);

	private final Function<GameController, Texture> getTexture;

	private BoostType(Function<GameController, Texture> getTexture) {
		this.getTexture = getTexture;
	}

	public Texture getTexture(GameController game) {
		return getTexture.apply(game);
	}

	public void useOn(Player player) {
		player.setBoost(this);
	}

	public static BoostType getRandom() {
		return values()[(int) (values().length * Math.random())];
	}

}
