package games.bambooInvaders;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.util.Map;

public class Cell {

	private int type;
	static Image[] bamboos;
	public static Map<Integer, int[]> bambooThresholds;
	public static Map<Integer, float[]> bambooSpeedCoefficients; // 1, .75, .25
	public static Map<Integer, float[]> bambooGaugeCoefficients;
	public static Map<Integer, Float> actionCountdownCoefficient;
	public int bambooStage;
	public int bambooGauge;
	public boolean fertile;

	public Cell(){
		//TODO
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
	}
}
