package games.bambooInvaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

public class Cell {

	private static List<Cell> cells;
	private static Image[] bamboos;
	private static Map<Integer, int[]> bambooThresholds;
	private static Map<Integer, float[]> bambooSpeedCoefficients; // 1, .75, .25 : modifie la vitesse de déplacement du Dino
	private static Map<Integer, float[]> bambooGaugeCoefficients; // Effet de cette case sur les cases adjacentes
	private static Map<Integer, Float> actionCountdownCoefficient;

	public static void load(String path) {
		Cell.cells = new ArrayList<Cell>();
		String json = AppLoader.loadData(path);
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0, li = array.length(); i < li; ++i) {
			}
		} catch (JSONException error) {}
	}

	private int type;
	private int bambooStage;
	private int bambooGauge;
	private boolean fertile;

	public Cell(int type) {
		this.type = type;
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
