package game;

import board.*;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import javafx.scene.layout.Pane;

public class Castle extends Sprite{
	private int id;
	private int owner;
	private int reserve;
	private ArrayList<Integer> production;
	private int target;
	private Direction door;

	public Castle(int id, int owner, Pane layer, int x, int y) {
		super(layer, (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR), x, y, 4);
		// Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
		this.id = id;
		this.owner = owner;
		this.target = -1;
		this.reserve = 0;
		this.production = new ArrayList<Integer>();
	}

	public int getId() { return this.id; }

	public void setOwner(int o) { this.owner = o; }
	public int getOwner() { return this.owner; }

	public void setTarget(int t) { this.target = t; }
	public int getTarget() { return this.target; }

	public int getSoldiers() { return this.reserve; }
	public int getProduction() { return this.production.size(); }

	public void update() {
		this.updateProduction();
	}

	public void addProd() { this.production.add(5); }
	public void updateProduction() {
		int prod = this.production.get(0);
		prod--;
		if (prod == 0) {
			this.reserve++;
			this.production.remove(0);
		}
	}

}
