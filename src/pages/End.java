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

public class End extends AppPage {

	private Image background;
	private Audio music;
	private Font font;
	private Color backgroundColor;
	private Color foregroundColor;
	private Color borderColor;
	private Font headingFont;
	private Color headingColor;

	public End(int ID) {
		super(ID);
	}

	public void init(GameContainer container, StateBasedGame game) {
		this.background = AppLoader.loadPicture("/images/bambooInvaders/end.png");
		this.music = AppLoader.loadAudio("/sounds/bambooInvaders/Le_bruit_dun_scorpion_qui_meurt.ogg");
		this.font = AppLoader.loadFont("/fonts/bambooInvaders/ubuntu.ttf", AppFont.BOLD, container.getHeight() / 24);
		this.backgroundColor = new Color(.8f, .8f, 1f);
		this.foregroundColor = new Color(.2f, .4f, 0f);
		this.borderColor = new Color(.2f, .2f, .2f);
		this.headingFont = AppLoader.loadFont("/fonts/bambooInvaders/garamond.ttf", AppFont.BOLD, container.getHeight() / 18);
		this.headingColor = Color.black;
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
		String text = "Le dino " + (world.getWinnerDino() + 1) + " gagna pour la raison suivante :";
		String reason = world.getWinReason();
		String score1 = "Score du dino 1 : " + world.getDinos()[0].getScore() / 1000;
		String score2 = "Score du dino 2 : " + world.getDinos()[1].getScore() / 1000;
		context.drawImage(this.background, 0, 0, container.getWidth(), container.getHeight(), 0, 0, this.background.getWidth(), this.background.getHeight());
		int headingWidth = container.getWidth() * 3 / 4;
		int headingHeight = this.headingFont.getHeight(null) * 5 / 2;
		int headingX = container.getWidth() / 2;
		int headingY = container.getHeight() / 3;
		int textX = headingX - headingWidth / 2;
		int textY = headingY - headingHeight * 9 / 20;
		int reasonX = headingX - headingWidth / 2;
		int reasonY = headingY - headingHeight / 20;
		context.setColor(this.headingColor);
		context.setFont(this.headingFont);
		context.drawString(text, textX, textY);
		context.drawString(reason, reasonX, reasonY);
		int centerX = container.getWidth() / 2;
		int centerY = container.getHeight() * 5 / 6;
		int padding = container.getHeight() / 24;
		int cornerRadius = container.getHeight() / 48;
		int lineWidth = container.getHeight() / 96;
		int scoreWidth = Math.max(this.font.getWidth(score1), this.font.getWidth(score2));
		int scoreHeight = this.font.getHeight(null) * 5 / 2;
		int score1X = centerX - scoreWidth / 2;
		int score1Y = centerY - scoreHeight * 9 / 20;
		int score2X = centerX - scoreWidth / 2;
		int score2Y = centerY + scoreHeight / 20;
		int boxWidth = scoreWidth + padding * 2;
		int boxHeight = scoreHeight + padding * 2;
		int boxX = centerX - boxWidth / 2;
		int boxY = centerY - boxHeight / 2;
		context.setColor(this.backgroundColor);
		context.fillRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius);
		context.setColor(this.foregroundColor);
		context.setFont(this.font);
		context.drawString(score1, score1X, score1Y);
		context.drawString(score2, score2X, score2Y);
		context.setColor(this.borderColor);
		context.setLineWidth(lineWidth);
		context.drawRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius);
		context.setLineWidth(1);
	}

}
