package games.bambooInvaders;

import app.AppLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.*;

public class Dino {

	private Image dino;

	private int bambooCounter;
	private int initialActionCountdown;
	private int actionCountdown;
	private int i;
	private int j;
	private int nextI;
	private int nextJ;
	private boolean flip;
	private Grid grid;
	private int score;
	private boolean isRegurgitating;
	private int timeRegurgitating;

	private final int timeRegurgiteBamboo = 50; // 50 ms
	private final int countdownPerBamboo = 50; // 50 ms

	public Dino(Grid grid) {
		this.score = 0;
		this.grid = grid;
		int[] ij = grid.findNestPlace();
		this.i = ij[0];
		this.j = ij[1];
		this.nextI = ij[0];
		this.nextJ = ij[1];
		this.flip = false;
		this.initialActionCountdown = 0;
		this.actionCountdown = 0;
		this.bambooCounter = 0;
		this.timeRegurgitating = 0;
		this.isRegurgitating = false;
		this.dino = AppLoader.loadPicture("/images/bambooInvaders/dino.png");
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		this.score += delta;

		this.actionCountdown -= delta;
		if (this.actionCountdown <= 0) {
			this.i = this.nextI;
			this.j = this.nextJ;
			this.actionCountdown = this.initialActionCountdown = checkInput(container,delta);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		context.drawImage(
			dino,
			container.getWidth() / 2 - Cell.getWidth() / 3,
			container.getHeight() / 2 - Cell.getHeight() / 3,
			container.getWidth() / 2 + Cell.getWidth() / 3,
			container.getHeight() / 2 + Cell.getHeight() / 3,
			this.flip ? dino.getWidth() : 0,
			0,
			this.flip ? 0 : dino.getWidth(),
			dino.getHeight()
		);
	}

	private int checkInput(GameContainer container, int delta) {
		Input input = container.getInput();

		if (this.isRegurgitating) {
			regurgitate(delta);
		}
		this.isRegurgitating = false;

		if (input.isKeyDown(Input.KEY_S)) {
			// Regurgite si dans un nid
			Cell cell = grid.getCell(i, j);
			if (cell.getType() == 0) {
				isRegurgitating = true;
			}
			// Mange les bambous s'il y en a
			else {
				int stage = cell.getBambooStage();
				if (stage > 0) {
					cell.setBambooGauge(0);
					cell.setBambooStage(0);
					return eat(stage);
				}
			}

		} else if (input.isKeyDown(Input.KEY_Z)) {
			return move(0);
		} else if (input.isKeyDown(Input.KEY_E)) {
			return move(1);
		} else if (input.isKeyDown(Input.KEY_D)) {
			return move(2);
		} else if (input.isKeyDown(Input.KEY_X)) {
			return move(3);
		} else if (input.isKeyDown(Input.KEY_W)) {
			return move(4);
		} else if (input.isKeyDown(Input.KEY_Q)) {
			return move(5);
		}

		if (!isRegurgitating) {
			regurgitate(0); // Reset de la régurgitation si elle est terminée
		}

		return 0; // Aucune action donc timeout de 0
	}

	private int move(int direction) {
		switch (direction) {
		case 0:
			--this.nextI;
			this.flip = false;
			break;
		case 1:
			--this.nextI;
			++this.nextJ;
			this.flip = true;
			break;
		case 2:
			++this.nextJ;
			this.flip = true;
			break;
		case 3:
			++this.nextI;
			this.flip = true;
			break;
		case 4:
			++this.nextI;
			--this.nextJ;
			this.flip = false;
			break;
		case 5:
			--this.nextJ;
			this.flip = false;
			break;
		}

		Cell nextCell = grid.getCell(this.nextI, this.nextJ);
		if (nextCell == null || nextCell.getDinoSpeedCoefficient() == 0) { // La case n'est pas accessible
			this.nextI = this.i;
			this.nextJ = this.j;
			return 0;
		}
		int cooldown = (int) ((this.bambooCounter * this.countdownPerBamboo + grid.getCell(i, j).getDinoActionDuration()) / grid.getCell(i, j).getDinoSpeedCoefficient()) ;
		if (cooldown > 3000) cooldown = 3000;

		return cooldown;
	}

	private int eat(int stage) {
		this.bambooCounter += stage*1.5;
		return (int) 500*stage;
	}

	private void regurgitate(int delta) {
		if (delta > 0) {
			this.timeRegurgitating += delta;
			while (this.timeRegurgitating > this.timeRegurgiteBamboo) {
				this.timeRegurgitating -= this.timeRegurgiteBamboo;
				this.bambooCounter -= 1;
				this.score += 3000;
			}
		} else {
			this.timeRegurgitating = 0;
		}
	}

	public Point getPoint() {
		Point point = this.grid.getHexagonCenter(this.i, this.j);
		if (this.initialActionCountdown == 0) {
			return point;
		}
		Point nextPoint = this.grid.getHexagonCenter(this.nextI, this.nextJ);
		int x = (point.x * this.actionCountdown + nextPoint.x * (this.initialActionCountdown - this.actionCountdown)) / this.initialActionCountdown;
		int y = (point.y * this.actionCountdown + nextPoint.y * (this.initialActionCountdown - this.actionCountdown)) / this.initialActionCountdown;
		return new Point(x, y);
	}
}
