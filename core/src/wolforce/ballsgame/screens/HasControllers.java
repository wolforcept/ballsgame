package wolforce.ballsgame.screens;

import com.badlogic.gdx.controllers.Controller;

public interface HasControllers {

	boolean axisMoved(Controller controller, int axisIndex, float value);

}
