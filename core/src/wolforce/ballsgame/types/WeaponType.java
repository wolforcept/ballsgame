package wolforce.ballsgame.types;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Player;
import wolforce.ballsgame.objs.Projectile;

public enum WeaponType {

	STARTER(g -> g.imgStarter) {
		@Override
		public int shoot(final GameController game, final Player p, float vx, float vy) {
			float bulletSpeed = p.getBulletSpeed();
			Projectile proj = new Projectile(p, p.x, p.y, 6);
			proj.setVelocity(vx * bulletSpeed, vy * bulletSpeed);
			proj.damage = p.getDamage();
			proj.color1 = p.color1;
			proj.maxAge = p.getRange();
			game.addProjectile(proj);
			return p.getAttackSpeed() / 2;
		}
	},
	SHOTGUN(g -> g.imgShotgun) {
		@Override
		public int shoot(GameController game, final Player p, float vx, float vy) {
			float bulletSpeed = p.getBulletSpeed();
			for (int i = 0; i < 10; i++) {
				float rvx = (float) (Math.random() * 2.0 - 1);
				float rvy = (float) (Math.random() * 2.0 - 1);
				Projectile proj = new Projectile(p, p.x, p.y, 3);
				proj.setVelocity(vx * bulletSpeed + rvx, vy * bulletSpeed + rvy);
				proj.damage = p.getDamage();
				proj.color1 = p.color1;
				proj.maxAge = p.getRange() / 2;
				game.addProjectile(proj);
			}
			return p.getAttackSpeed() * 2;
		}
	},
	MINIGUN(g -> g.imgMinigun) {
		@Override
		public int shoot(GameController game, final Player p, float vx, float vy) {
			float rvx = (float) (Math.random() - .5);
			float rvy = (float) (Math.random() - .5);
			Projectile proj = new Projectile(p, p.x, p.y, 4);
			proj.damage = (int) (p.getDamage() * .33);
			float bulletSpeed = p.getBulletSpeed() * 2;
			proj.setVelocity(vx * bulletSpeed + rvx, vy * bulletSpeed + rvy);
			proj.maxAge = p.getRange();
			proj.color1 = p.color1;
			game.addProjectile(proj);
			return p.getAttackSpeed() / 6;
		}

	},
	SNIPER(g -> g.imgSniper) {
		@Override
		public int shoot(GameController game, final Player p, float vx, float vy) {
			float bulletSpeed = p.getBulletSpeed();
			Projectile proj = new Projectile(p, p.x, p.y, 10) {
				public void step(GameController game) {
					super.step(game);
					if (remove)
						damage -= p.getDamage() / 2;
					if (damage >= p.getDamage())
						remove = false;
				};
			};
			proj.damage = p.getDamage() * 4;
			proj.setVelocity(vx * bulletSpeed * 2, vy * bulletSpeed * 2);
			proj.maxAge = p.getRange() * 2;
			proj.color1 = p.color1;
			game.addProjectile(proj);
			return p.getAttackSpeed() * 3;
		}
	},
	HEALGUN(g -> g.imgHealgun) {
		@Override
		public int shoot(GameController game, final Player p, float vx, float vy) {
			float bulletSpeed = p.getBulletSpeed();
			Projectile proj = new Projectile(p, p.x, p.y, 4) {
				@Override
				public void step(GameController game) {
					super.step(game);
					for (Player p2 : game.game.getPlayers()) {
						if (p2.equals(p))
							continue;
						if (dist(p2) < size + p2.size) {
							p2.addHp(1);
							this.remove = true;
							return;
						}
					}
				}
			};
			proj.damage = 0;
			float rvx = (float) (Math.random() * 2.0 - 1);
			float rvy = (float) (Math.random() * 2.0 - 1);
			proj.setVelocity(vx * bulletSpeed + rvx, vy * bulletSpeed + rvy);
			proj.maxAge = (int) (p.getRange() / 2.5);
			proj.color1 = Color.GREEN;
			game.addProjectile(proj);
			return 5;
		}
	};

	public abstract int shoot(GameController game, Player player, float vx, float vy);

	Function<GameController, Texture> getTexture;

	private WeaponType(Function<GameController, Texture> getTexture) {
		this.getTexture = getTexture;
	}

	public Texture getTexture(GameController game) {
		return getTexture.apply(game);
	}

	public static WeaponType getRandom() {
		return values()[1 + (int) ((values().length - 1) * Math.random())];
	}
}
