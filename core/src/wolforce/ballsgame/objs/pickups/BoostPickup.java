package wolforce.ballsgame.objs.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Player;
import wolforce.ballsgame.types.BoostType;

public class BoostPickup extends Pickupable {
	BoostType type;

	public BoostPickup(float x, float y, BoostType type) {
		super(x, y);
		this.type = type;

		this.size = 10;
		this.border = 2;
		this.color1 = new Color(1, .5f, 0, 1);
		this.color2 = new Color(color1.r * .8f, color1.g * .8f, color1.b * .8f, 1);
	}

	@Override
	protected void onPickup(GameController game, Player p) {
		p.setBoost(type);
	}

	@Override
	protected Texture getTexture(GameController game) {
		return type.getTexture(game);
	}

}
