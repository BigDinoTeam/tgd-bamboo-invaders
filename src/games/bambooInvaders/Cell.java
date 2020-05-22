package games.bambooInvaders;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Map;

public class Cell {

	private int type;
	private static Image[] bamboos;
	private static Map<Integer, int[]> bambooThresholds;
	private static Map<Integer, float[]> bambooSpeedCoefficients; // 1, .75, .25 : modifie la vitesse de déplacement du Dino
	private static Map<Integer, float[]> bambooGaugeCoefficients; // Effet de cette case sur les cases adjacentes
	private static Map<Integer, Float> actionCountdownCoefficient;
	private int bambooStage;
	private int bambooGauge;
	private boolean fertile;

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

	public int getNextBambooThreshold(){
		if (this.bambooStage == 2){
			return -1; // No next threshold
		}
		return bambooThresholds.get(this.type)[this.bambooStage + 1];
	}

	public float getBambooSpeedCoefficient(){
		return bambooSpeedCoefficients.get(this.type)[this.bambooStage];
	}

	public float getCurrentBambooGaugeCoefficient(){
		return bambooGaugeCoefficients.get(this.type)[this.bambooStage];
	}

	public float getActionCountdownCoefficient(){
		return actionCountdownCoefficient.get(this.type);
	}

}
