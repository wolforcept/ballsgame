package wolforce.ballsgame.objs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import wolforce.ballsgame.BallsGame;
import wolforce.ballsgame.GameController;
import wolforce.ballsgame.types.BoostType;
import wolforce.ballsgame.types.PowerUpType;
import wolforce.ballsgame.types.WeaponType;

public class Player extends Obj {

	public static final int EXTRA_HEALTH_PER_POWERUP = 20;
	public static final float EXTRA_SPEED_PER_POWERUP = 0.2f;
	public static final int EXTRA_RANGE_PER_POWERUP = 20;
	public static final int EXTRA_BULLET_SPEED_PER_POWERUP = 1;
	public static final float EXTRA_ATTACK_SPEED_PER_POWERUP = 0.85f;
	public static final int EXTRA_DAMAGE_PER_POWERUP = 5;
	private static final int MAX_BOOST_DURATION = 1000;

	public static Color[] playerColors = { new Color(0, 0, 1, 1), new Color(1, 0, 1, 1), new Color(1, .5f, .5f, 1) };
	public static int playerIndex = 0;

	public float speed = 3;
	public float projSpd = 5;
	public float shootVx = 0, shootVy = 0;
	public int level = 1, exp = 0;
	public int cd = 0;
	public WeaponType weapon = WeaponType.STARTER;

	private int maxHp = 100;
	public int hp = maxHp;
	public int revive = 0;

	public int extraHealth = 0, extraSpeed = 0, extraAttackSpeed = 0, extraDamage = 0, extraBulletSpeed = 0,
			extraRange = 0;
	public BoostType boost = null;
	private int boostDuration = 0;
	public float[] axis = { 0, 0, 0, 0 };

	public Player() {
		this.color1 = playerColors[playerIndex];
		playerIndex++;
		this.x = Gdx.graphics.getWidth() / 2;
		this.y = Gdx.graphics.getHeight() / 2;
		this.size = 16;
	}

	public int getMaxHp() {
		return maxHp + extraHealth * EXTRA_HEALTH_PER_POWERUP;
	}

	public float getSpeed() {
		float value = 1 + extraSpeed * EXTRA_SPEED_PER_POWERUP;
		if (boost == BoostType.SPEED)
			return value * 2;
		return value;
	}

	public int getRange() {
		int value = 250 + EXTRA_RANGE_PER_POWERUP * extraRange;
		if (boost == BoostType.RANGE)
			return value * 2;
		return value;
	}

	public float getBulletSpeed() {
		float value = 4 + EXTRA_BULLET_SPEED_PER_POWERUP * extraBulletSpeed;
		if (boost == BoostType.BULLET_SPEED)
			return value * 2;
		return value;
	}

	public int getAttackSpeed() {
		float nr = 50;
		for (int i = 0; i < extraAttackSpeed; i++)
			nr *= EXTRA_ATTACK_SPEED_PER_POWERUP;
		if (boost == BoostType.ATTACK_SPEED)
			return (int) (nr / 2);
		return (int) nr;
	}

	public int getDamage() {
		int value = 10 + extraDamage * EXTRA_DAMAGE_PER_POWERUP;
		if (boost == BoostType.DAMAGE)
			return value * 2;
		return value;
	}

	public void addPowerUp(PowerUpType type) {
		type.useOn(this);
	}

	@Override
	public void step(GameController game) {

		if (boost != null) {
			boostDuration--;
			if (boostDuration <= 0)
				boost = null;
		}

		if (hp <= 0) {
			for (Player p : game.getLivePlayers()) {
				if (dist(p) < size + p.size) {
					revive++;
					if (revive == getMaxHp()) {
						revive = 0;
						hp = getMaxHp() / 3;
					}
				}
			}
			return;
		}

		if (Math.abs(vx) > 0.1)
			x += vx * getSpeed() / 1.5f;
		if (Math.abs(vy) > 0.1)
			y += vy * getSpeed() / 1.5f;

		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x > Gdx.graphics.getWidth())
			x = Gdx.graphics.getWidth();
		if (y > Gdx.graphics.getHeight())
			y = Gdx.graphics.getHeight();
		if (cd > 0)
			cd--;
		if (cd <= 0 && (shootVx * shootVx > 0.1 || shootVy * shootVy > 0.1)) {

			float sum = Math.abs(shootVx) + Math.abs(shootVy);
			this.cd = weapon.shoot(game, this, shootVx / sum, shootVy / sum);
			BallsGame.stats.shotsFired++;
		}
	}

	public void addHp(int value) {
		hp = Math.min(getMaxHp(), hp + value);
	}

	protected void hurtMonster(Projectile p, Monster m) {
		m.hp -= p.damage;
		BallsGame.stats.damageDealt += p.damage;

		if (m.hp <= 0) {
			gainExp(m.exp);
			BallsGame.stats.enemiesKilled++;
		}
	}

	private void gainExp(int value) {
		int needed = getNeededExp();
		if (this.exp + value > needed) {
			level++;
			if (level > BallsGame.stats.maxLevel)
				BallsGame.stats.maxLevel = level;
			this.exp = value - (needed - this.exp);
		} else
			this.exp += value;
		BallsGame.stats.expGained += value;
	}

	public int getNeededExp() {
		int nextLevel = level + 1;
		return (int) (nextLevel * Math.log(nextLevel) * 10);
	}

	public boolean isExtraFull() {
		return extraHealth + extraSpeed + extraAttackSpeed + extraBulletSpeed + extraDamage + extraRange >= level;
	}

	public void setBoost(BoostType boostType) {
		this.boost = boostType;
		this.boostDuration = MAX_BOOST_DURATION;
	}

	public void hurtPlayer(Player p, int damage) {
		p.hp -= damage;
		BallsGame.stats.healthLost += damage;
		if (p.hp < 0) {
			BallsGame.stats.deaths++;
		}
	}

}
