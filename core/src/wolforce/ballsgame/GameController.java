package wolforce.ballsgame;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import wolforce.ballsgame.objs.Monster;
import wolforce.ballsgame.objs.Obj;
import wolforce.ballsgame.objs.Particle;
import wolforce.ballsgame.objs.Player;
import wolforce.ballsgame.objs.Projectile;
import wolforce.ballsgame.objs.pickups.BoostPickup;
import wolforce.ballsgame.objs.pickups.HealthPackPickup;
import wolforce.ballsgame.objs.pickups.PowerUpPickup;
import wolforce.ballsgame.objs.pickups.WeaponPickup;
import wolforce.ballsgame.types.BoostType;
import wolforce.ballsgame.types.MonsterType;
import wolforce.ballsgame.types.PowerUpType;
import wolforce.ballsgame.types.WeaponType;

public class GameController {

	public BallsGame game;

	public LinkedList<Monster> monsters;
	public LinkedList<Projectile> projs;
	public LinkedList<Obj> other;
	public LinkedList<Particle> particles;

	ShapeRenderer shape;
	SpriteBatch batch;
	public Texture imgSpeed, imgHealth, imgRange, imgDamage, imgBulletSpeed, imgAttackSpeed;
	public Texture imgStarter, imgShotgun, imgMinigun, imgSniper, imgHealgun;

	int monsterNr = 3;
	int monsterHp = 5;
	private boolean gameStarted = true;

	public GameController(BallsGame game) {

		this.game = game;

		Player.playerIndex = 0;

		monsters = new LinkedList<>();
		projs = new LinkedList<>();
		other = new LinkedList<>();
		particles = new LinkedList<>();

		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		imgSpeed = new Texture("speed.png");
		imgHealth = new Texture("health.png");
		imgRange = new Texture("range.png");
		imgDamage = new Texture("damage.png");
		imgBulletSpeed = new Texture("bullet_speed.png");
		imgAttackSpeed = new Texture("attack_speed.png");

		imgStarter = new Texture("starter.png");
		imgShotgun = new Texture("shotgun.png");
		imgMinigun = new Texture("minigun.png");
		imgSniper = new Texture("sniper.png");
		imgHealgun = new Texture("healgun.png");

	}

	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		String id = controller.getUniqueId();
		Player player = game.getOrCreatePlayer(id);
		if (player.hp <= 0) {
			player.vx = 0;
			player.vy = 0;
		} else {
			switch (axisIndex) {
			case 0:
				player.vx = value * player.speed;
				break;
			case 1:
				player.vy = -value * player.speed;
				break;
			case 2:
				player.shootVx = value;
				break;
			case 3:
				player.shootVy = -value;
			}
		}
		return false;
	}

	public List<Player> getLivePlayers() {
		return game.getPlayers().stream().filter(p -> p.hp > 0).collect(Collectors.toList());
	}

	public void addMonster() {
		if (monsters.size() >= monsterNr && Math.random() < .8)
			return;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		float mx = (float) Math.random() * w;
		float my = (float) Math.random() * h;
		if (Math.random() < 0.5)
			mx = Math.random() < 0.5 ? 0 : w;
		else
			my = Math.random() < 0.5 ? 0 : h;
		Monster m = new Monster(monsterHp, mx, my, MonsterType.getRandom(this));
		m.type.create(this, m);
		monsters.add(m);

		if (Math.random() < 0.02) {
			monsterNr++;
			monsterHp++;
		}
	}

	public void addProjectile(Projectile projectile) {
		this.projs.add(projectile);
	}

	public void addOther(Obj obj) {
		this.other.add(obj);
	}

	public void particle(float x, float y, Color color) {
		float vx = (float) (-1 + Math.random() * 2);
		float vy = (float) (-1 + Math.random() * 2);
		particle(80, x, y, vx, vy, color, 5);
	}

	public void particle(float x, float y, float distributionSize, Color color) {
		float vx = (float) (-1 + Math.random() * 2);
		float vy = (float) (-1 + Math.random() * 2);
		float xx = (float) (x - distributionSize / 2 + Math.random() * distributionSize);
		float yy = (float) (y - distributionSize / 2 + Math.random() * distributionSize);
		particle(80, xx, yy, vx, vy, color, 5);
	}

	public void particle(int age, float x, float y, float vx, float vy, Color color, int size) {
		particles.add(new Particle(age, x, y, vx, vy, color.r, color.g, color.b, size));
	}

	public void render() {

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		shape.begin(ShapeType.Filled);
		shape.setColor(new Color(1, 1, 1, 0.5f));
		shape.rect(0, 0, w, h);

		if (!gameStarted) {
			shape.end();
			return;
		}

		if (getLivePlayers().size() == 0)
			BallsGame.game.gameEnd();

		if (Math.random() < 0.02 + 0.001 * monsterNr) {
			addMonster();
		}

		for (Iterator<Projectile> it = projs.iterator(); it.hasNext();) {
			Projectile p = it.next();
			p.step(this);
			drawObj(p);
			if (p.remove == true || p.x < p.size || p.y < p.size || p.x > w + p.size || p.y > h + p.size)
				it.remove();
		}

		for (Iterator<Monster> it = monsters.iterator(); it.hasNext();) {
			Monster monster = it.next();
			monster.step(this);
			drawObj(monster);
			if (monster.remove || monster.hp <= 0) {
				monster.remove(this);
				it.remove();
			}
		}

		for (Iterator<Obj> it = other.iterator(); it.hasNext();) {
			Obj o = it.next();
			o.step(this);
			drawObj(o);
			o.draw(this, shape);
			if (o.remove) {
				o.remove(this);
				it.remove();
			}
		}

		for (Player p : game.getPlayers()) {
			p.step(this);
			drawObj(p, p.hp > 0 ? p.color1 : p.color1.cpy().add(0, 0, 0, -.8f), null);
			if (p.hp > 0) {
				shape.setColor(0, 1, 0, 1);
				shape.rect(p.x - p.size, p.y + p.size, p.size * 2 * p.hp / p.getMaxHp(), 2);
			} else {
				shape.setColor(.4f, .4f, .4f, .5f);
				shape.rect(p.x - p.size, p.y + p.size, p.size * 2 * p.revive / p.getMaxHp(), 2);
			}
		}

		for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle o = it.next();
			o.step(this);
			shape.setColor(o.getColor());
			shape.circle(o.x, o.y, o.size);
			if (o.remove)
				it.remove();
		}

		// GUI

		Collection<Player> ps = game.getPlayers();
		if (ps.size() > 0) {
			int guiSize = w / ps.size();
			int guiX = 0;

			for (Player player : ps) {
				int xx = (int) (guiX + guiSize * .1f);

				shape.setColor(player.color1);
				shape.arc(guiX, 0, 60, 0, 90);

				drawRect(xx, 10, guiSize * .8f, guiSize * .8f * player.hp / player.getMaxHp(), 20,
						new Color(0, 1, 0, 1));

				drawRect(xx, 34, guiSize * .5f, guiSize * .5f * player.exp / player.getNeededExp(), 10,
						new Color(1, .8f, 0, 1));

				Texture weapTex = player.weapon.getTexture(this);
				drawImage(weapTex, xx, 0);

				int puz = 24; // power up size
				int dx = 0;
				if (player.boost != null) {
					shape.setColor(1, .5f, 0, .5f);
					shape.rect(xx, 50, 18, 18);
					dx = puz;
				}
				shape.setColor(0, 1, 1, .5f);
				for (int i = 0; i < player.level; i++)
					shape.rect(xx + i * puz + dx, 50, 18, 18);

				int j = 0;
				if (player.boost != null) {
					drawImage(player.boost.getTexture(this), xx + 1, 51);
					j++;
				}
				for (PowerUpType type : PowerUpType.values()) {
					for (int i = 0; i < type.getOf(player); i++) {
						drawImage(type.getTexture(this), xx + j * puz, 51);
						j++;
					}
				}

				guiX += guiSize;
			}
		}
		shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

	}

	private void drawRect(int x, int y, float w1, float w2, int h, Color color) {
		shape.setColor(0, 0, 0, 1);
		shape.rect(x, y, w1, h);
		shape.setColor(color.r * .8f, color.g * .8f, color.b * .8f, color.a);
		shape.rect(x + 1, y + 1, w1 - 2, h - 2);
		shape.setColor(color);
		shape.rect(x + 1, y + 1, Math.max(0, w2 - 2), h - 2);
	}

	public void drawImage(Texture img, float x, float y) {
		shape.end();
		batch.begin();
		batch.draw(img, x, y);
		batch.end();
		shape.begin(ShapeType.Filled);
	}

	public void drawObj(Obj o) {
		drawObj(o, o.color1, o.color2);
	}

	public void drawObj(Obj o, Color color1, Color color2) {
		if (o.color2 != null) {
			shape.setColor(color2);
			shape.circle(o.x, o.y, o.size + o.border);
		}
		if (o.color1 != null) {
			shape.setColor(color1);
			shape.circle(o.x, o.y, o.size);
		}
	}

	public void dispose() {
		batch.dispose();

		imgSpeed.dispose();
		imgAttackSpeed.dispose();
		imgBulletSpeed.dispose();
		imgDamage.dispose();
		imgHealth.dispose();
		imgRange.dispose();
		imgSpeed.dispose();

		imgShotgun.dispose();
		imgSniper.dispose();
		imgHealgun.dispose();

	}

	public Obj createRandomPickup(float x, float y) {
		double r = Math.random();
		if (r < .3) {
			return new HealthPackPickup(x, y);
		} else if (r < .6) {
			return new PowerUpPickup(x, y, PowerUpType.getRandom());
		} else if (r < .9) {
			return new BoostPickup(x, y, BoostType.getRandom());
		}
		return new WeaponPickup(x, y, WeaponType.getRandom());
	}

}
