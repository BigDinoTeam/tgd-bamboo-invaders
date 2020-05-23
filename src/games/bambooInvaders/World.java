package games.bambooInvaders;

import java.awt.Point;

import app.AppLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class World extends BasicGameState {

	private static int goalScore = 150; // Score to reach to win
	private int ID;
	private int state;
	private Grid grid;
	private Dino firstDino;
	private Dino lastDino;
	private Dino[] dinos; // 1 ou 2 dinos
	private Audio worldMusic;
	private float worldMusicPosition;

	public World(int ID) {
		this.ID = ID;
		this.state = 0;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au chargement du programme */
		this.worldMusic = AppLoader.loadAudio("/sounds/bambooInvaders/Tidal_Wave.ogg");
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à l'apparition de la page */
		if (this.state == 0) {
			this.play(container, game);
		} else if (this.state == 2) {
			this.resume(container, game);
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à la disparition de la page */
		if (this.state == 1) {
			this.pause(container, game);
		} else if (this.state == 3) {
			this.stop(container, game);
			this.state = 0; // TODO: remove
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			this.setState(1);
			game.enterState(2, new FadeOutTransition(), new FadeInTransition());
		}
		this.grid.update(container, game, delta);

		for (Dino dino: dinos) {
			dino.update(container, game, delta);
		}

		// Check winner/looser :
		//TODO : loop through Dino check if a nest was bamboozled
		//TODO : loop through Dino to check score
		for (Dino dino: dinos) {
			if(dino.getNest().getBambooStage() == 2){ // Si bambou adulte dans le nid du Dino
				this.setState(3);
				if (dinos.length == 1){
					game.enterState(5 , new FadeOutTransition (), new FadeInTransition ()); // Death page (if only one Dino)
				} else{
					game.enterState(6 , new FadeOutTransition (), new FadeInTransition ()); // Win page with the other Dino as winner
					//TODO : mettre l'autre Dino en winner
				}
			}
		}

		for (Dino dino: dinos) { // Check win by score
			if(dino.getScore() / 1000 >= this.goalScore){
				// TODO : launch win page with this dino as the winner. Reason : score
				this.setState(3);
				game.enterState(6 , new FadeOutTransition (), new FadeInTransition ());
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		int width = container.getWidth();
		int height = container.getHeight();
		context.setClip(0, 0, width / 2, height);
		Point firstPoint = this.firstDino.getPoint();
		this.grid.render(container, game, context, firstPoint.x + width / 4, firstPoint.y);
		this.firstDino.render(container, game, context);
		context.setClip(width / 2, 0, width / 2, height);
		Point lastPoint = this.lastDino.getPoint();
		this.grid.render(container, game, context, lastPoint.x - width / 4, lastPoint.y);
		this.lastDino.render(container, game, context);
		context.setClip(0, 0, width, height);
	}

	public void play(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au début du jeu */
		this.firstDino = new Dino(grid, false);
		this.lastDino = new Dino(grid, true);
		if (!this.worldMusic.isPlaying()) {
			this.worldMusic.playAsMusic(1, 0.8f, true);
		}
		this.dinos = new Dino[]{this.firstDino, this.lastDino}; // TODO : gérer le cas d'un Dino en solo
	}

	public void pause(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la mise en pause du jeu */
		if (this.worldMusic.isPlaying()) {
			this.worldMusicPosition = this.worldMusic.getPosition();
			this.worldMusic.stop();
		}
	}

	public void resume(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la reprise du jeu */
		if (!this.worldMusic.isPlaying()) {
			this.worldMusic.playAsMusic(1, 0.8f, true);
			this.worldMusic.setPosition(worldMusicPosition);
		}
	}

	public void stop(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois à la fin du jeu */
		if (this.worldMusic.isPlaying()) {
			this.worldMusic.stop();
		}
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

}
