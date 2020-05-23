package pages;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppMenu;
import app.elements.MenuItem;

import games.bambooInvaders.Grid;
import games.bambooInvaders.World;

public class Choice extends AppMenu {

	public Choice(int ID) {
		super(ID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		super.initSize(container, game, 600, 400);
		super.init(container, game);
		this.setTitle("Bamboo Invaders");
		this.setSubtitle("Niveaux");
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		for (String[] grid: Grid.getGrids()) {
			menuItems.add(new MenuItem(grid[0]) {
				public void itemSelected() {
					((World) game.getState(3)).setGrid(new Grid(grid[0], grid[1]));
					game.enterState(3, new FadeOutTransition(), new FadeInTransition());
				}
			});
		}
		menuItems.add(new MenuItem("Regles") {
			public void itemSelected() {
				game.enterState(4, new FadeOutTransition(), new FadeInTransition());
			}
		});
		menuItems.add(new MenuItem("Retour") {
			public void itemSelected() {
				game.enterState(0, new FadeOutTransition(), new FadeInTransition());
			}
		});
		this.setMenu(menuItems);
		this.setHint("SELECT A LEVEL");
	}

}
