package games.bambooInvaders;

import app.AppFont;
import app.AppLoader;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

public class Dino {

	private Image dino;
	private Image dino_down;
	private Image gui;
	private AppFont bambooFont;
	private Audio eat;
	private Audio splash;
	private Audio regurgitate;

	private int bambooCounter;
	private int initialActionCountdown;
	private int actionCountdown;
	private Grid grid;
	private Cell nest;
	private int i;
	private int j;
	private int nextI;
	private int nextJ;
	private boolean reversed;
	private boolean flipped;
	private int score;
	private boolean isRegurgitating;
	private int timeRegurgitating;
	private boolean inAction;

	private final int timeRegurgiteBamboo = 50; // 50 ms
	private final int countdownPerBamboo = 50; // 50 ms

	public Dino(Grid grid, boolean reversed) {
		this.score = 0;
		this.grid = grid;
		int[] ij = grid.findNestPlace(reversed);
		this.nest = grid.getCell(ij[0], ij[1]);
		this.i = ij[0];
		this.j = ij[1];
		this.nextI = ij[0];
		this.nextJ = ij[1];
		this.reversed = reversed;
		this.flipped = !reversed;
		this.initialActionCountdown = 0;
		this.actionCountdown = 0;
		this.bambooCounter = 0;
		this.timeRegurgitating = 0;
		this.isRegurgitating = false;
		this.inAction = false;
		this.dino = AppLoader.loadPicture("/images/bambooInvaders/dino.png");
		this.dino_down = AppLoader.loadPicture("/images/bambooInvaders/dino_down.png");
		this.gui = AppLoader.loadPicture("/images/bambooInvaders/GUI.png");
		this.eat = AppLoader.loadAudio("/sounds/bambooInvaders/mange.ogg");
		this.regurgitate = AppLoader.loadAudio("/sounds/bambooInvaders/regurgite.ogg");
		this.splash = AppLoader.loadAudio("/sounds/bambooInvaders/splash.ogg");
		this.bambooFont = AppLoader.loadFont(null, AppFont.BOLD, 42);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		this.score += delta;

		this.actionCountdown -= delta;
		if (this.actionCountdown <= 0) {
			this.inAction = false;
			this.i = this.nextI;
			this.j = this.nextJ;
			this.actionCountdown = this.initialActionCountdown = checkInput(container,delta);
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context, int scrollX, int scrollY, boolean guiDisplayed) {
		/* Méthode exécutée environ 60 fois par seconde */
		context.drawImage(
			this.inAction ? dino_down : dino,
			-Cell.getWidth() / 3 - scrollX,
			-Cell.getHeight() / 3 - scrollY,
			Cell.getWidth() / 3 - scrollX,
			Cell.getHeight() / 3 - scrollY,
			this.flipped ? dino.getWidth() : 0,
			0,
			this.flipped ? 0 : dino.getWidth(),
			dino.getHeight()
		);
		if (!guiDisplayed) {
			return;
		}
		int guiX = this.reversed ? container.getWidth() - gui.getWidth() : 0;
		int guiY = container.getHeight() - gui.getHeight();
		context.drawImage(
			gui,
			guiX,
			guiY,
			0,
			0,
			gui.getWidth(),
			gui.getHeight()
		);
		context.setFont(bambooFont);
		context.setColor(new Color(0x5c913b));
		context.drawString(""+this.bambooCounter, guiX + 175, guiY + 6);
		context.resetFont();
		context.setColor(new Color(0x565656));
		context.drawString("Score : "+this.score/1000, guiX + 125, guiY + 72);
	}

	private int checkInput(GameContainer container, int delta) {
		Input input = container.getInput();

		if (this.isRegurgitating  && this.bambooCounter > 0) {
			regurgitate(delta);
		}
		this.isRegurgitating = false;

		if (input.isKeyDown(this.reversed ? Input.KEY_L : Input.KEY_S)) {
			// Regurgite si dans un nid
			Cell cell = grid.getCell(i, j);
			if (cell == this.nest && this.bambooCounter > 0) {
				isRegurgitating = true;
				inAction = true;
				if (this.timeRegurgitating == 0) {
					this.regurgitate.playAsSoundEffect(1, 1f, false);
				}
			}
			// Mange les bambous s'il y en a
			else if (cell.getType() != 0){
				int stage = cell.getBambooStage();
				if (stage > 0) {
					this.eat.playAsSoundEffect(1, 1f, false);
					inAction = true;
					cell.setBambooGauge(0);
					cell.setBambooStage(0);
					return eat(stage);
				}
			}

		} else if (input.isKeyDown(this.reversed ? Input.KEY_O : Input.KEY_Z)) {
			return move(0);
		} else if (input.isKeyDown(this.reversed ? Input.KEY_P : Input.KEY_E)) {
			return move(1);
		} else if (input.isKeyDown(this.reversed ? Input.KEY_M : Input.KEY_D)) {
			return move(2);
		} else if (input.isKeyDown(this.reversed ? Input.KEY_COLON: Input.KEY_X)) {
			return move(3);
		} else if (input.isKeyDown(this.reversed ? Input.KEY_SEMICOLON: Input.KEY_W)) {
			return move(4);
		} else if (input.isKeyDown(this.reversed ? Input.KEY_K : Input.KEY_Q)) {
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
			this.flipped = false;
			break;
		case 1:
			--this.nextI;
			++this.nextJ;
			this.flipped = true;
			break;
		case 2:
			++this.nextJ;
			this.flipped = true;
			break;
		case 3:
			++this.nextI;
			this.flipped = true;
			break;
		case 4:
			++this.nextI;
			--this.nextJ;
			this.flipped = false;
			break;
		case 5:
			--this.nextJ;
			this.flipped = false;
			break;
		}

		Cell nextCell = grid.getCell(this.nextI, this.nextJ);
		if (nextCell == null || nextCell.getDinoSpeedCoefficient() == 0) { // La case n'est pas accessible
			this.nextI = this.i;
			this.nextJ = this.j;
			return 0;
		}
		if (nextCell.getType() == 3) {
			this.splash.playAsSoundEffect(1, 1f, false);
		}
		int cooldown = (int) (this.bambooCounter * this.countdownPerBamboo + grid.getCell(i, j).getDinoActionDuration() / grid.getCell(i, j).getDinoSpeedCoefficient()) ;
		if (cooldown > 4000) cooldown = 4000;

		return cooldown;
	}

	private int eat(int stage) {
		this.bambooCounter += stage*1.5;
		return (int) 250*stage;
	}

	private void regurgitate(int delta) {
		if (delta > 0) {
			this.timeRegurgitating += delta;
			while (this.timeRegurgitating > this.timeRegurgiteBamboo && this.bambooCounter > 0) {
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

	public int getScore() {
		return this.score;
	}

	public Cell getNest() {
		return this.nest;
	}

}
