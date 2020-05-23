package pages;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppFont;
import app.AppLoader;
import app.AppPage;

import games.bambooInvaders.World;

public class Life extends AppPage {

	private Image background;
	private Audio music;
	private Font font;
	private Color backgroundColor;
	private Color foregroundColor;
	private Color borderColor;

	public Life(int ID) {
		super(ID);
	}

	public void init(GameContainer container, StateBasedGame game) {
		this.background = AppLoader.loadPicture("/images/bambooInvaders/life.png");
		this.music = AppLoader.loadAudio("/sounds/bambooInvaders/Le_bruit_dun_scorpion_qui_meurt.ogg");
		this.font = AppLoader.loadFont("/fonts/bambooInvaders/ubuntu.ttf", AppFont.BOLD, container.getHeight() / 24);
		this.backgroundColor = new Color(.8f, .8f, 1f);
		this.foregroundColor = new Color(.2f, .4f, 0f);
		this.borderColor = new Color(.2f, .2f, .2f);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		if (!this.music.isPlaying()) {
			this.music.playAsMusic(1, .4f, false);
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
		World world = (World) game.getState(3);
		String score = "Score : " + (world.getDinos()[0].getScore() / 1000);
		context.drawImage(this.background, 0, 0, container.getWidth(), container.getHeight(), 0, 0, this.background.getWidth(), this.background.getHeight());
		int centerX = container.getWidth() / 2;
		int centerY = container.getHeight() * 5 / 6;
		int padding = container.getHeight() / 24;
		int cornerRadius = container.getHeight() / 48;
		int lineWidth = container.getHeight() / 96;
		int scoreWidth = this.font.getWidth(score);
		int scoreHeight = this.font.getHeight(null) * 5 / 4;
		int scoreX = centerX - scoreWidth / 2;
		int scoreY = centerY - scoreHeight * 2 / 5;
		int boxWidth = scoreWidth + padding * 2;
		int boxHeight = scoreHeight + padding * 2;
		int boxX = centerX - boxWidth / 2;
		int boxY = centerY - boxHeight / 2;
		context.setColor(this.backgroundColor);
		context.fillRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius);
		context.setColor(this.foregroundColor);
		context.setFont(this.font);
		context.drawString(score, scoreX, scoreY);
		context.setColor(this.borderColor);
		context.setLineWidth(lineWidth);
		context.drawRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius);
		context.setLineWidth(1);
	}

}
