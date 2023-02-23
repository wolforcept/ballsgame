package wolforce.ballsgame.objs.pickups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import wolforce.ballsgame.GameController;
import wolforce.ballsgame.objs.Player;
import wolforce.ballsgame.types.WeaponType;

public class WeaponPickup extends Pickupable {

	WeaponType weapon;

	public WeaponPickup(float x, float y, WeaponType weapon) {
		super(x, y);
		this.weapon = weapon;

		this.size = 10;
		this.border = 2;
		this.color1 = new Color(1, 1, 0, 1);
		this.color2 = new Color(color1.r * .8f, color1.g * .8f, color1.b * .8f, 1);
	}

	@Override
	protected void onPickup(GameController game, Player p) {
		p.weapon = weapon;
	}

	@Override
	protected Texture getTexture(GameController game) {
		return weapon.getTexture(game);
	}

}
