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


	private static String[] bamboos = {"/images/bambooInvaders/bamboos/bamboo_young.png", "/images/bambooInvaders/bamboos/bamboo_old.png"};
	private static String[] names;
	private static String[] backgrounds;
	private static boolean[] fertilities;
	private static int[][] bambooThresholds;
	private static float[][] bambooGaugeCoefficients; // Effet de cette case sur les cases adjacentes
	private static int[] dinoActionDurations;
	private static float[][] dinoSpeedCoefficients; // 1, .75, .25 : modifie la vitesse de déplacement du Dino
	private static int size = 80; // Distance entre le centre de l'hexagone et ces sommets

	public static void load(String filename) {
		String json = AppLoader.loadData(filename);
		JSONArray array = new JSONArray();
		try {
			array = new JSONArray(json);
		} catch (JSONException error) {}
		int length = array.length();
		String[] names = new String[length];
		String[] backgrounds = new String[length];
		boolean[] fertilities = new boolean[length];
		int[][] bambooThresholds = new int[length][];
		float[][] bambooGaugeCoefficients = new float[length][];
		int[] dinoActionDurations = new int[length];
		float[][] dinoSpeedCoefficients = new float[length][];
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
			backgrounds[i] = background;
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
			int dinoActionDuration = 0;
			try {
				dinoActionDuration = object.getInt("dino_action_duration");
			} catch (JSONException error) {}
			dinoActionDurations[i] = dinoActionDuration;
			JSONArray speedCoefficientsArray = new JSONArray();
			try {
				speedCoefficientsArray = object.getJSONArray("dino_speed_coefficients");
			} catch (JSONException error) {}
			float[] speedCoefficients = new float[3];
			for (int j = 0; j < 3; ++j) {
				float speedCoefficient = 0;
				try {
					speedCoefficient = (float) speedCoefficientsArray.getDouble(j);
				} catch (JSONException error) {}
				speedCoefficients[j] = speedCoefficient;
			}
			dinoSpeedCoefficients[i] = speedCoefficients;
		}
		Cell.names = names;
		Cell.backgrounds = backgrounds;
		Cell.fertilities = fertilities;
		Cell.bambooThresholds = bambooThresholds;
		Cell.bambooGaugeCoefficients = bambooGaugeCoefficients;
		Cell.dinoActionDurations = dinoActionDurations;
		Cell.dinoSpeedCoefficients = dinoSpeedCoefficients;
	}

	private int type;
	private Image background;
	private Image[] bambooImages;
	private int bambooStage;
	private int bambooGauge;

	public Cell(int type) {
		this.type = type;
		this.background = AppLoader.loadPicture(backgrounds[type]);
		this.bambooImages = new Image[2];
		for (int i = 0; i < bamboos.length; i++) {
			this.bambooImages[i] = AppLoader.loadPicture(bamboos[i]);
		}
		this.bambooStage = 0;
		this.bambooGauge = 0;
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		if (!this.isFertile()) {
			return;
		}
		for (;;) {
			int threshold = this.getNextBambooThreshold();
			if (threshold == -1) {
				return;
			}
			int gauge = this.getBambooGauge();
			if (gauge < threshold) {
				return;
			}
			gauge -= threshold;
			this.setBambooGauge(gauge);
			this.setBambooStage(this.getBambooStage() + 1);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context, int x, int y) {
		/* Méthode exécutée environ 60 fois par seconde */
		x += container.getWidth() / 2;
		y += container.getHeight() / 2;
		context.drawImage(this.background, x, y, x + getWidth(), y + getHeight(), 0,0, this.background.getWidth(), this.background.getHeight());

		if (this.bambooStage > 0){ // S'il y a un bamboo, on l'affiche
			Image bambooImage = this.bambooImages[this.bambooStage-1];
			context.drawImage(bambooImage, x, y, x + getWidth(), y + getHeight(), 0,0, bambooImage.getWidth(), bambooImage.getHeight()); // TODO : tester
		}
	}

	public boolean isFertile() {
		return fertilities[this.type];
	}

	public int getNextBambooThreshold(){
		if (this.bambooStage == 2){
			return -1; // No next threshold
		}
		return bambooThresholds[this.type][this.bambooStage];
	}

	public float getBambooGaugeCoefficient(){
		return bambooGaugeCoefficients[this.type][this.bambooStage];
	}

	public int getDinoActionDuration(){
		return dinoActionDurations[this.type];
	}

	public float getDinoSpeedCoefficient(){
		return dinoSpeedCoefficients[this.type][this.bambooStage];
	}

	public int getType() {
		return this.type;
	}

	public Image getBackground(){
		return this.background;
	}

	public void setBambooStage(int bambouStage) {
		this.bambooStage = bambouStage;
	}

	public int getBambooStage() {
		return this.bambooStage;
	}

	public void setBambooGauge(int bambooGauge) {
		this.bambooGauge = bambooGauge;
	}

	public int getBambooGauge() {
		return this.bambooGauge;
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

}
