import app.AppLoader;
import games.bambooInvaders.Cell;
import games.bambooInvaders.Grid;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import javax.swing.*;
import java.awt.*;

public final class Main {

	public static final void main(String[] arguments) throws SlickException {
		String title = "Bamboo Invaders";
		int width = 1280;
		int height = 720;
		boolean fullscreen = false;
		String request = "Voulez-vous jouer en plein écran ?";
		String[] options = new String[] {
			"Oui",
			"Non"
		};
		JFrame frame = new JFrame();
		frame.setIconImage(AppLoader.loadIcon("/images/icon.png").getImage());
		int returnValue = JOptionPane.showOptionDialog(
			frame,
			request,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[0]
		);
		frame.dispose();
		if (returnValue == -1) {
			return;
		}
		if (returnValue == 0) {
			DisplayMode display = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
			width = display.getWidth();
			height = display.getHeight();
			fullscreen = true;
		}
		Cell.load("/data/bambooInvaders/cells.json");
		Grid.load("/data/bambooInvaders/grids.json");
		StateBasedGame game = new StateBasedGame(title) {

			@Override
			public void initStatesList(GameContainer container) {
				this.addState(new pages.Welcome(0));
				this.addState(new pages.Choice(1));
				this.addState(new pages.Pause(2));
				this.addState(new games.bambooInvaders.World(3));
				this.addState(new pages.Rules(4));
				this.addState(new pages.Death(5));
				this.addState(new pages.Life(6));
				this.addState(new pages.End(7));
			}

		};
		AppGameContainer container = new AppGameContainer(game, width, height, fullscreen);
		container.setTargetFrameRate(60);
		container.setVSync(true);
		container.setShowFPS(false);
		container.setIcon(AppLoader.resolve("/images/icon.png"));
		container.start();
	}

}
