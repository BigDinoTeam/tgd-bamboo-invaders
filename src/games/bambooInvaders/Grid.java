package games.bambooInvaders;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;

import static java.lang.Math.floor;

public class Grid {

	private Cell[][] cells;

	public Grid(){
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

	/**
	 * Return cell of given axials coordinates
	 * @param row
	 * @param col
	 * @return Cell
	 */
	public Cell getCell(int row, int col){
		int[] memoryCoord = convertAxialToMemoryCoord(row, col);
		return this.cells[memoryCoord[0]][memoryCoord[1]];
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
