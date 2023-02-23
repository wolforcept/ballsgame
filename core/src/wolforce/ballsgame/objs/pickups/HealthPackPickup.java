package wolforce.ballsgame.objs.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Player;

public class HealthPackPickup extends Pickupable {

	public HealthPackPickup(float x, float y) {
		super(x, y);

		this.size = 10;
		this.border = 2;
		this.color1 = new Color(0, 1, 0, 1);
		this.color2 = new Color(color1.r * .8f, color1.g * .8f, color1.b * .8f, 1);
	}

	@Override
	protected void onPickup(GameController game, Player p) {
		p.addHp(5);
	}

	@Override
	protected Texture getTexture(GameController game) {
		return game.imgHealth;
	}

}
