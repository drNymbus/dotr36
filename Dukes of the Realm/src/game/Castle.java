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
	private ArrayList<Soldier> soldiers;
	private int target;
	private Direction door;

	public Castle(int id, int owner, Pane layer, int x, int y, Direction d) {
		super(layer, (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR), x, y, 4);
		// Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
		this.id = id;
		this.owner = owner;
		this.target = -1;
		this.reserve = 0;
		this.production = new ArrayList<Integer>();
		this.door = d;
		// this.soldiers = new ArrayList<Soldier>();
	}

	public int getId() { return this.id; }

	public void setOwner(int o) { this.owner = o; }
	public int getOwner() { return this.owner; }

	public void setTarget(int t) { this.target = t; }
	public int getTarget() { return this.target; }

	public int getNbSoldiers() { return this.reserve; }
	public int getProduction() { return this.production.size(); }
	// public ArrayList<Soldier> getSoldiers() { return this.soldiers; }

	public void addProd() { this.production.add(5); }
	public void updateProduction() {
		int prod = this.production.get(0);
		prod--;
		if (prod == 0) {
			this.reserve++;
			this.production.remove(0);
		}
	}

	private Soldier createSoldier(int id, Pane layer) {
		if (this.target == -1) {return null;}
		Soldier s = new Soldier(id, this.owner, this.target, layer, this.getColor(), this.getX(), this.getY());
		//switch pour la position de la door;
		return s;
	}

	public int attack(ArrayList<Soldier> soldiers, Pane layer) {
		int i = 3, count = 0;
		while (i > 0 || this.reserve > 0) {
			this.reserve--;
			i--;

			soldiers.add(createSoldier(soldiers.size(), layer));
			count++;
		}
		return count;
	}

	public void defend(Soldier s) {
		this.reserve--;
		if (this.reserve == 0) this.owner = s.getOwner();

		while (this.production.size() > 0) this.production.remove(0);
	}

}
