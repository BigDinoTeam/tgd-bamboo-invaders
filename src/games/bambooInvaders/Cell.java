package games.bambooInvaders;

import app.AppLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import static java.lang.Math.sqrt;

public class Cell {

	private static Image[] bamboos;
	private static String[] names;
	private static Image[] backgrounds;
	private static boolean[] fertilities;
	private static int[][] bambooThresholds;
	private static float[][] bambooSpeedCoefficients; // 1, .75, .25 : modifie la vitesse de déplacement du Dino
	private static float[][] bambooGaugeCoefficients; // Effet de cette case sur les cases adjacentes
	private static float[] actionCountdownCoefficients;
	private static int size; // Distance entre le centre de l'hexagone et ces sommets

	public static void load(String filename) {
		String json = AppLoader.loadData(filename);
		JSONArray array = new JSONArray();
		try {
			array = new JSONArray(json);
		} catch (JSONException error) {}
		int length = array.length();
		String[] names = new String[length];
		Image[] backgrounds = new Image[length];
		boolean[] fertilities = new boolean[length];
		int[][] bambooThresholds = new int[length][];
		float[][] bambooSpeedCoefficients = new float[length][];
		float[][] bambooGaugeCoefficients = new float[length][];
		float[] actionCountdownCoefficients = new float[length];
		for (int i = 0; i < length; ++i) {
			JSONObject object = new JSONObject();
			try {
				object = array.getJSONObject(i);
			} catch (JSONException error) {}
			String name = "";
			try {
				name = object.getString("name");
			} catch (JSONException error) {}
			names[i] = name;
			String background = "";
			try {
				background = object.getString("background");
			} catch (JSONException error) {}
			backgrounds[i] = AppLoader.loadPicture(background);
			try {
				object = array.getJSONObject(i);
			} catch (JSONException error) {}
			boolean fertility = false;
			try {
				fertility = object.getBoolean("fertility");
			} catch (JSONException error) {}
			fertilities[i] = fertility;
			JSONArray thresholdsArray = new JSONArray();
			try {
				thresholdsArray = object.getJSONArray("bamboo_thresholds");
			} catch (JSONException error) {}
			int[] thresholds = new int[2];
			for (int j = 0; j < 2; ++j) {
				int threshold = 0;
				try {
					threshold = thresholdsArray.getInt(j);
				} catch (JSONException error) {}
				thresholds[j] = threshold;
			}
			bambooThresholds[i] = thresholds;
			JSONArray speedCoefficientsArray = new JSONArray();
			try {
				speedCoefficientsArray = object.getJSONArray("bamboo_speed_coefficients");
			} catch (JSONException error) {}
			float[] speedCoefficients = new float[3];
			for (int j = 0; j < 3; ++j) {
				float speedCoefficient = 0;
				try {
					speedCoefficient = (float) speedCoefficientsArray.getDouble(j);
				} catch (JSONException error) {}
				speedCoefficients[j] = speedCoefficient;
			}
			bambooSpeedCoefficients[i] = speedCoefficients;
			JSONArray gaugeCoefficientsArray = new JSONArray();
			try {
				gaugeCoefficientsArray = object.getJSONArray("bamboo_gauge_coefficients");
			} catch (JSONException error) {}
			float[] gaugeCoefficients = new float[3];
			for (int j = 0; j < 3; ++j) {
				float gaugeCoefficient = 0;
				try {
					gaugeCoefficient = (float) gaugeCoefficientsArray.getDouble(j);
				} catch (JSONException error) {}
				gaugeCoefficients[j] = gaugeCoefficient;
			}
			bambooGaugeCoefficients[i] = gaugeCoefficients;
			float actionCountdownCoefficient = 0f;
			try {
				actionCountdownCoefficient = (float) object.getDouble("action_dountdown_coefficient");
			} catch (JSONException error) {}
			actionCountdownCoefficients[i] = actionCountdownCoefficient;
		}
	}

	private int type;
	private int bambooStage;
	private int bambooGauge;

	public Cell(int type) {
		this.type = type;
		this.bambooStage = 0;
		this.bambooGauge = 0;
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context, int x, int y) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
		context.drawImage(getBackground(), x, y);
	}

	public boolean isFertile() {
		return fertilities[this.type];
	}

	public int getNextBambooThreshold(){
		if (this.bambooStage == 2){
			return -1; // No next threshold
		}
		return bambooThresholds[this.type][this.bambooStage + 1];
	}

	public float getBambooSpeedCoefficient(){
		return bambooSpeedCoefficients[this.type][this.bambooStage];
	}

	public float getCurrentBambooGaugeCoefficient(){
		return bambooGaugeCoefficients[this.type][this.bambooStage];
	}

	public float getActionCountdownCoefficient(){
		return actionCountdownCoefficients[this.type];
	}

	public int getType() {
		return type;
	}

	public int getBambooStage() {
		return bambooStage;
	}

	public int getBambooGauge() {
		return bambooGauge;
	}

	public static int getSize(){
		return size;
	}

	public static int getWidth(){
		return (int) (sqrt(3) * size);
	}

	public static int getHeight(){
		return 2 * size;
	}

	public Image getBackground(){
		return backgrounds[this.type];
	}

}
