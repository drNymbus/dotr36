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

	private ArrayList<Integer> production;
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
		this.production = new ArrayList<Integer>();
		this.door = d;
		this.reserve = 1;
		Random rnd = new Random();
		if(owner==0)
		{
			this.level=(1+rnd.nextInt(3));
			this.tresor=(rnd.nextInt(100));
		}else
			this.level=1;
			this.tresor=0;
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
	}

	public int getTarget() {
		return this.target;
	}

	public int getNbSoldiers() {
		return this.reserve;
	}

	public int getProduction() {
		return this.production.size();
	}
	public void addSoldier() {
		this.reserve ++;
	}

	// public ArrayList<Soldier> getSoldiers() { return this.soldiers; }

	public void addProd() {
		if (tresor > 100) {
			this.production.add(5);
			tresor -= 100;
		}
	}

	public void levelup() {
		if (tresor > (1000 * this.level)) {
			tresor -= (1000 * this.level);
			this.production.add(100 + 50 * this.level);
		}
	}

	public void updateProduction() {
		if (this.production.size() == 0)
			return;
		int prod = this.production.get(0);
		prod--;
		if (prod == 0) {
			this.reserve++;
			this.production.remove(0);
		}
		else
			this.production.set(0, prod);
	}

	public double distance(Castle c) {
		double tmpX = Math.pow((c.getX() - this.getX()), 2);
		double tmpY = Math.pow((c.getY() - this.getY()), 2);
		return Math.sqrt(tmpX + tmpY);
	}

	private Soldier createSoldier(int id, Pane layer) {
		Soldier s = new Soldier(id, this.owner, this.target, layer, this.getColor(), this.getX(), this.getY());
		s.addToLayer();
		// switch pour la position de la door;
		return s;
	}

	public int attack(ArrayList<Soldier> soldiers, Pane layer) {
		int i = 3, count = 0;
		while (i > 0 && this.reserve > 0) {
			this.reserve--;
			i--;
			soldiers.add(createSoldier(soldiers.size(), layer));
			count++;
		}
		this.target=-1;
		return count;
	}

	public void defend(Soldier s) {
		s.removeFromLayer();
		this.reserve--;
		if (this.reserve <= 0) {
			this.owner = s.getOwner();
			this.getShape().setFill((owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR));
			
		}
		while (this.production.size() > 0)
			this.production.remove(0);
	}

	public void updateGold() {
		if (owner == 0)
			tresor = tresor + (level);
		else
			tresor = tresor + (10 * level);
	}

}
