package pages;

import app.AppLoader;
import app.AppPage;
import games.bambooInvaders.World;
import org.newdawn.slick.*;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Death extends AppPage {

	private Image background;
	private Audio music;
	private Font font;

	public Death(int ID) {
		super(ID);
	}

	public void init(GameContainer container, StateBasedGame game) {
		this.background = AppLoader.loadPicture("/images/bambooInvaders/end.png");
		this.music = AppLoader.loadAudio("/sounds/bambooInvaders/Le_bruit_dun_scorpion_qui_meurt.ogg");
		this.font = AppLoader.loadFont("/fonts/Garamond.ttf",0, 40);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		if (!this.music.isPlaying()) {
			this.music.playAsMusic(1, .4f, true);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_ESCAPE) || input.isKeyDown(Input.KEY_ENTER)) {
			game.enterState(1, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		context.drawImage(this.background, 0, 0, container.getWidth(), container.getHeight(), 0, 0, this.background.getWidth(), this.background.getHeight());

		World world = (World) game.getState(3);
		context.setFont(this.font);
		context.drawString("Score : " + (world.getDinos()[0].getScore() / 1000), 750, 975);
	}

}