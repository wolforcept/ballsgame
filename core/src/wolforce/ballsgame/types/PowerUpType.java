package wolforce.ballsgame.types;

import java.util.function.Consumer;
import java.util.function.Function;

import com.badlogic.gdx.graphics.Texture;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Player;

public enum PowerUpType {
	POWERUP_HEALTH(true, //
			g -> g.imgHealth, //
			p -> {
				p.extraHealth++;
				p.hp += Player.EXTRA_HEALTH_PER_POWERUP;
			}, //
			p -> p.extraHealth//
	), POWERUP_SPEED(true, //
			g -> g.imgSpeed, //
			p -> p.extraSpeed++, //
			p -> p.extraSpeed//
	), POWERUP_RANGE(true, //
			g -> g.imgRange, //
			p -> p.extraRange++, //
			p -> p.extraRange//
	), POWERUP_DAMAGE(true, //
			g -> g.imgDamage, //
			p -> p.extraDamage++, //
			p -> p.extraDamage//
	), POWERUP_BULLET_SPEED(true, //
			g -> g.imgBulletSpeed, //
			p -> p.extraBulletSpeed++, //
			p -> p.extraBulletSpeed//
	), POWERUP_ATTACK_SPEED(true, //
			g -> g.imgAttackSpeed, //
			p -> p.extraAttackSpeed++, //
			p -> p.extraAttackSpeed//
	);

	private final Function<GameController, Texture> getTexture;
	private final Function<Player, Integer> getOf;
	private final Consumer<Player> useOn;
	public final boolean isExtra;

	private PowerUpType(boolean isExtra, Function<GameController, Texture> getTexture, Consumer<Player> useOn,
			Function<Player, Integer> getOf) {
		this.isExtra = isExtra;
		this.getTexture = getTexture;
		this.useOn = useOn;
		this.getOf = getOf;
	}

	public Texture getTexture(GameController game) {
		return getTexture.apply(game);
	}

	public static PowerUpType random() {
		return values()[(int) (Math.random() * values().length)];
	}

	public void useOn(Player player) {
		if (!isExtra || !player.isExtraFull())
			useOn.accept(player);
	}

	public int getOf(Player player) {
		if (getOf == null)
			return 0;
		return getOf.apply(player);
	}

	public static PowerUpType getRandom() {
		return values()[(int) (values().length * Math.random())];
	}

}
