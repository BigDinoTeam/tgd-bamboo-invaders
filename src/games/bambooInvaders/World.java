package games.bambooInvaders;

import app.AppLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.awt.*;


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

	private int winnerDino;
	private String winReason; // Whether the winner win by score or by surviving longer than its opponent


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
		for (int i = 0; i < dinos.length; i++) { // Check if a nest was bamboozled
			Dino dino = dinos[i];
			if(dino.getNest().getBambooStage() == 2){ // Si bambou adulte dans le nid du Dino
				this.setState(3);
				if (dinos.length == 1){
					game.enterState(5 , new FadeOutTransition (), new FadeInTransition ()); // Death page (if only one Dino)
				} else{
					this.winnerDino = 1 - i; // the other dino is the winner
					this.winReason = "L'autre nid a été envahis par des bambous."; // Other nest bamboozled
					game.enterState(7 , new FadeOutTransition (), new FadeInTransition ()); // WinMulti page with the other Dino as winner
				}
			}
		}


		for (int i = 0; i < dinos.length; i++) { // Check win by score
			Dino dino = dinos[i];
			if(dino.getScore() / 1000 >= this.goalScore){
				this.setState(3);
				if (dinos.length == 1){
					game.enterState(6 , new FadeOutTransition (), new FadeInTransition ()); // Win page (if only one Dino)
				} else{
					this.winnerDino = i; // this dino is the winner
					this.winReason = "Score maximal atteint !";
					game.enterState(7 , new FadeOutTransition (), new FadeInTransition ()); // WinMulti page
				}
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
