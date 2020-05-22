package games.bambooInvaders;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import static java.lang.Math.floor;

public class Grid {

	private static List<String[]> grids;

	private Cell[][] cells;

	public static void load(String path) {
		Grid.grids = new ArrayList<String[]>();
		String json = AppLoader.loadData(path);
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0, li = array.length(); i < li; ++i) {
			}
		} catch (JSONException error) {}
	}

	public static List<String[]> getGrids() {
		return Grid.grids;
	}

	public Grid(String path) {
		this.cells = new Cell[0][0];
		String json = AppLoader.loadData(path);
		try {
			JSONObject object = new JSONObject(json);
			int width = 0;
			try {
				width = object.getInt("width");
			} catch (JSONException error) {}
			int height = 0;
			try {
				height = object.getInt("height");
			} catch (JSONException error) {}
			cells = new Cell[height][width];
			try {
				JSONArray array = object.getJSONArray("cells");
				for (int i = 0; i < height; ++i) {
					JSONArray subarray = new JSONArray();
					try {
						subarray = array.getJSONArray(i);
					} catch (JSONException error) {}
					for (int j = 0; j < width; ++j) {
						int type = 0;
						try {
							type = subarray.getInt(j);
						} catch (JSONException error) {}
						cells[i][j] = new Cell(type);
					}
				}
			} catch (JSONException error) {}
		} catch (JSONException error) {}
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
	}

	/**
	 * @param row : row_axial
	 * @param col : col_axial
	 * @return [row_memory, col_memory]
	 */
	public static int[] convertAxialToMemoryCoord(int row, int col){
		return new int[] {row, col + (int) (floor(row/2))};
	}

	/**
	 * @param row : row_memory
	 * @param col : col_memory
	 * @return [row_axial, col_axial]
	 */
	public static int[] convertMemoryToAxialCoord(int row, int col){
		return new int[] {row, col - (int) floor(row/2)};
	}

}
