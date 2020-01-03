package game;

import board.*;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.Pane;

public class Castle extends Sprite {
	private int id;
	private int owner;
	private int reserve;

	private ProductionTab production;
	private ArrayList<Soldier> soldiers;
	private int target;
	private Direction door;

	private int tresor;
	private int level;

	public Castle(int id, int owner, Pane layer, int x, int y, Direction d) {
		super(layer,
				(owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR),
				x, y, Settings.SIZE_CASTLE);
		// Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ?
		// Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
		this.id = id;
		this.owner = owner;
		this.target = -1;

		this.production = new ProductionTab();
		this.soldiers = new ArrayList<Soldier>();
		this.door = d;
		this.reserve = 0;
		Random rnd = new Random();
		if(owner==0)
		{
			this.level=(1+rnd.nextInt(3));
			this.tresor=(rnd.nextInt(100));
		}else{
			this.level=1;
			this.tresor=0;
		}
	}

	// getter setter
	public int getTresor() {
		return tresor;
	}

	public void setTresor(int tresor) {
		this.tresor = tresor;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getId() {
		return this.id;
	}

	public void setOwner(int o) {
		this.owner = o;
	}

	public int getOwner() {
		return this.owner;
	}

	public void setTarget(int t) {
		this.target = t;
		for (int i=0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			s.setTarget(t);
		}
	}

	public int getTarget() {
		return this.target;
	}

	public int getNbSoldiers() {
		return this.reserve;
	}

	public String getSoldiers() {
		String msg = "";
		for (int i=0; i < this.soldiers.size(); i++) {
			msg += this.soldiers.get(i).getType();
			msg += "|";
		}
		return msg;
	}

	public ProductionTab getProduction() {
		return this.production;
	}

	public void addSoldier() {
		this.reserve ++;
	}

	// public ArrayList<Soldier> getSoldiers() { return this.soldiers; }

	public void addProd(TypeSoldier t) {
		boolean add = false;
		switch (t) {
			case Piquier:
				if (this.tresor > 100) {
					add = true;
					tresor -= 100;
				}
				break;
			case Chevalier:
				if (this.tresor > 500) {
					add = true;
					tresor -= 500;
				}
				break;

			case Onagre:
				if (this.tresor > 1000) {
					add = true;
					tresor -= 100;
				}
				break;
		}
		this.production.add(t);
	}

	public void levelup() {
		if (tresor > (1000 * this.level)) {
			tresor -= (1000 * this.level);
			this.production.add(100 + 50 * this.level);
		}
	}

	public void updateProduction(Pane layer) {
		if (this.production.size() == 0)
			return;
		ArrayList<TypeSoldier> prod = this.production.getProduction();
		for (int i=0; i < prod.size(); i++) {
			Soldier s = new Soldier(this.soldiers.size(), prod.get(i), this.owner, this.target, layer, this.getColor(), this.getX(), this.getY());
			this.soldiers.add(s);
			this.reserve++;
		}
	}

	// private Soldier createSoldier(int id, Pane layer) {
	// 	Soldier s = new Soldier(id, this.owner, this.target, layer, this.getColor(), this.getX(), this.getY());
	// 	s.addToLayer();
	// 	// switch pour la position de la door;
	// 	return s;
	// }

	public int attack(ArrayList<Soldier> sdrs) {
		int i = 3, count = 0;
		while (i > 0 && this.reserve > 0) {
			Soldier s = this.soldiers.get(0);
			sdrs.add(s);
			this.soldiers.remove(0);
			this.reserve--;
			count++;
		}
		// this.target=-1;
		return count;
	}

	public void defend(Soldier s) {
		s.removeFromLayer();
		this.reserve--;
		if (this.reserve <= 0) {
			this.owner = s.getOwner();
			this.getShape().setFill((owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR));

		}
		this.production.reset();
	}

	public void updateGold() {
		if (owner == 0)
			tresor += level;
		else
			tresor += (10 * level);
	}

}
