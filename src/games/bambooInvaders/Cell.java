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
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppLoader;

public class Cell {

	private static List<Cell> cells;
	static Image[] bamboos;
	public static Map<Integer, int[]> bambooThresholds;
	public static Map<Integer, float[]> bambooSpeedCoefficients; // 1, .75, .25
	public static Map<Integer, float[]> bambooGaugeCoefficients;
	public static Map<Integer, Float> actionCountdownCoefficient;

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
	public int bambooStage;
	public int bambooGauge;
	public boolean fertile;

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
}
