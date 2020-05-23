package games.bambooInvaders;

import app.AppLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.*;
import java.util.Random;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

public class Grid {

	private static String[][] grids;

	private String name;
	private int width;
	private int height;
	private Cell[][] cells;
	private int x;
	private int y;
	private Random random;

	public static void load(String filename) {
		String json = AppLoader.loadData(filename);
		JSONArray array = new JSONArray();
		try {
			array = new JSONArray(json);
		} catch (JSONException error) {}
		int length = array.length();
		String[][] grids = new String[length][];
		for (int i = 0; i < length; ++i) {
			JSONObject object = new JSONObject();
			try {
				object = array.getJSONObject(i);
			} catch (JSONException error) {}
		   String name = "";
		   try {
			   name = object.getString("name");
		   } catch (JSONException error) {}
			String path = "";
			try {
				path = object.getString("path");
			 } catch (JSONException error) {}
			grids[i] = new String[]{name, path};
		}
		Grid.grids = grids;
	}

	public static String[][] getGrids() {
		return Grid.grids;
	}

	public Grid(String name, String path) {
		this.x = (int) ((Cell.getSize() * sqrt(3)) / 2); //TODO : changer en fonction du positionnement de l'interface graphique
		this.y = Cell.getSize(); //TODO : changer en fonction du positionnement de l'interface graphique
		this.name = name;
		String json = AppLoader.loadData(path);
		JSONObject object = new JSONObject();
		try {
			object = new JSONObject(json);
		} catch (JSONException error) {}
		this.width = 0;
		try {
			width = object.getInt("width");
		} catch (JSONException error) {}
		this.height = 0;
		try {
			height = object.getInt("height");
		} catch (JSONException error) {}
		Cell[][] cells = new Cell[height][width];
		JSONArray array = new JSONArray();
		try {
			array = object.getJSONArray("cells");
		} catch (JSONException error) {}
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
		this.cells = cells;
		this.random = new Random();
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				Cell cell = this.cells[i][j];
				float coefficient = cell.getBambooGaugeCoefficient();
				int[] cellPlace = Grid.convertMemoryToAxialCoord(i, j);
				for (int k = 0; k < 6; ++k) {
					int[] neighborPlace = new int[]{cellPlace[0], cellPlace[1]};
					switch (k) {
						case 0: {
							--neighborPlace[0];
							break;
						}
						case 1: {
							--neighborPlace[0];
							++neighborPlace[1];
							break;
						}
						case 2: {
							++neighborPlace[1];
							break;
						}
						case 3: {
							++neighborPlace[0];
							break;
						}
						case 4: {
							++neighborPlace[0];
							--neighborPlace[1];
							break;
						}
						case 5: {
							--neighborPlace[1];
							break;
						}
					}
					int[] neighborIJ = Grid.convertAxialToMemoryCoord(neighborPlace[0], neighborPlace[1]);
					try {
						Cell neighbor = this.cells[neighborIJ[0]][neighborIJ[1]];
						int bambooGauge = neighbor.getBambooGauge();
						bambooGauge += this.random.nextInt(delta + 1) * coefficient;
						neighbor.setBambooGauge(bambooGauge);
					} catch (Exception exception) {}
				}
			}
		}
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				this.cells[i][j].update(container, game, delta);
			}
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context, int scrollX, int scrollY) {
		/* Méthode exécutée environ 60 fois par seconde */
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				int[] axialCoord = convertMemoryToAxialCoord(i, j);
				Point cellDisplayCoord = getHexagonCoordinates(axialCoord[0], axialCoord[1]);
				this.cells[i][j].render(container, game, context, cellDisplayCoord.x - scrollX, cellDisplayCoord.y - scrollY);
			}
		}
	}

	/**
	 * Return cell of given axials coordinates, return Null if out of bound
	 * @param row
	 * @param col
	 * @return Cell
	 */
	public Cell getCell(int row, int col) {
		int[] memoryCoord = convertAxialToMemoryCoord(row, col);
		Cell result = null;
		try{
			result = this.cells[memoryCoord[0]][memoryCoord[1]];
		} catch (IndexOutOfBoundsException e) {
			result = null;
		}
		return result;
	}

	/**
	 * @param row : row_axial
	 * @param col : col_axial
	 * @return [row_memory, col_memory]
	 */
	public static int[] convertAxialToMemoryCoord(int row, int col) {
		return new int[]{row, col + (int) (floor(row / 2))};
	}

	/**
	 * @param row : row_memory
	 * @param col : col_memory
	 * @return [row_axial, col_axial]
	 */
	public static int[] convertMemoryToAxialCoord(int row, int col) {
		return new int[]{row, col - (int) floor(row / 2)};
	}

	public Point getHexagonCenter(int row, int col){
		int x = (int) (Cell.getSize() * (sqrt(3) * col + sqrt(3) / 2 * row)) + this.x;
		int y = (int) (Cell.getSize() * (3. / 2 * row)) + this.y;
	    return new Point(x, y);
	}

	/**
	 * @param row
	 * @param col
	 * @return Point en haut à gauche de l'hexagone à partir de ses coordonnées axiales
	 */
	public Point getHexagonCoordinates(int row, int col){
		Point hexagonCenter = getHexagonCenter(row, col);
		return new Point(hexagonCenter.x - Cell.getWidth() / 2, hexagonCenter.y - Cell.getHeight() / 2) ;
	}

	public int[] findNestPlace(boolean reversed) {
		if (!reversed) {
			for (int i = 0; i < height; ++i) {
				for (int j = 0; j < width; ++j) {
					Cell cell = this.cells[i][j];
					if (cell.getType() != 0) {
						continue;
					}
					return convertMemoryToAxialCoord(i, j);
				}
			}
			return new int[]{0, 0};
		}
		for (int i = height - 1; i >= 0; --i) {
			for (int j = width - 1; j >= 0; --j) {
				Cell cell = this.cells[i][j];
				if (cell.getType() != 0) {
					continue;
				}
				return convertMemoryToAxialCoord(i, j);
			}
		}
		return new int[]{height, width};
	}

}
