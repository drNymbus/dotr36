package game;

import board.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
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
	private int nb_soldiers;
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
		this.nb_soldiers = 0;

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

	public Direction getDoor() { return this.door; }

	// getter setter
	public int getTresor() { return tresor; }
	public void setTresor(int tresor) { this.tresor = tresor; }

	public int getLevel() { return level; }
	public void setLevel(int level) { this.level = level; }

	public int getId() { return this.id; }

	public void setOwner(int o) { this.owner = o; }
	public int getOwner() { return this.owner; }

	public void setTarget(int t) {
		this.target = t;
		for (int i=0; i < this.soldiers.size(); i++)
			this.soldiers.get(i).setTarget(t);
	}

	public int getTarget() { return this.target; }

	public int getNbSoldiers() { return this.soldiers.size(); }
	public String getSoldiers() {
		String msg = "";
		for (int i=0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			msg += " - " + s.getType() + "(" + s.getLP() + ")\n";
		}
		return msg;
	}

	public ProductionTab getProduction() {
		return this.production;
	}

	public void addSoldier(Soldier s) {
		s.removeFromLayer();
		this.soldiers.add(0, s);
	}
	// public ArrayList<Soldier> getSoldiers() { return this.soldiers; }

	public void addProd(Production t) {
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
			this.production.add(500 * this.level);
		}
	}

	public void updateProduction(Pane layer) {
		int x = this.getX() + Settings.SIZE_CASTLE / 2;
		int y = this.getY() + Settings.SIZE_CASTLE / 2;
		switch (this.door) {
			case NORTH:
				y -= Settings.SIZE_SOLDIER / 2;
				break;
			case EAST:
				x += Settings.SIZE_CASTLE / 2;
				break;
			case SOUTH:
				y += Settings.SIZE_SOLDIER / 2;
				break;
			case WEST:
				x -= Settings.SIZE_CASTLE / 2;
				break;
		}

		if (this.production.size() == 0)
			return;
		Production prod = this.production.getProduction();
		if (prod == Production.Level) {
			this.level++;
		} else if (prod != Production.None) {
			Soldier s = new Soldier(this.soldiers.size(), prod, this.owner, this.target, layer, this.getColor(), this.getX(), this.getY());
			s.removeFromLayer();
			this.soldiers.add(s);
		}
	}

	public void attack(int target, ArrayList<Soldier> sdrs, int n) {
		this.nb_soldiers = n;
		this.setTarget(target);
		int i = 0;
		while (i < this.nb_soldiers && this.soldiers.size() > 0) {
			Soldier s = this.soldiers.get(0);
			switch (this.door) {
				case NORTH:
					s.setY(s.getY() - s.getSize()/2);
					break;
				case EAST:
					s.setX(s.getX() + s.getSize()/2);
					break;
				case SOUTH:
					s.setY(s.getY() + s.getSize()/2);
					break;
				case WEST:
					s.setX(s.getX() - s.getSize()/2);
					break;
			}
			s.addToLayer();
			sdrs.add(s);
			this.soldiers.remove(0);
			i++;
		}
	}
	public void attack(ArrayList<Soldier> sdrs) { this.attack(this.target, sdrs, this.nb_soldiers); }
	public void stopAttack() {
		this.target = -1;
		for (int i=0; i < this.soldiers.size(); i++) this.soldiers.get(i).setTarget(-1);
		this.nb_soldiers = 0;
	}

	public void defend(ArrayList<Soldier> sdrs, Soldier atk) {
		Soldier dfd = this.soldiers.get(0);
		if (dfd.defend(atk)) {
			if (!atk.defend(dfd))
				atk.removeFromLayer();
				sdrs.remove(atk);
		} else {
			dfd.removeFromLayer();
			this.soldiers.remove(0);
		}

		if (this.soldiers.size() <= 0) {
			this.owner = atk.getOwner();
			this.getShape().setFill((owner == Settings.ALLY_ID) ? Settings.ALLY_COLOR : ((owner == Settings.ENNEMY_ID) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR));
			this.production.reset();
			while (this.soldiers.size() > 0) this.soldiers.remove(0);
		}
	}

	public void updateGold() {
		if (owner == 0)
			tresor += level;
		else
			tresor += (10 * level);
	}

	//
	//
	//
	//
	//
	//
	//

	public void drawCastle() {
        int x = 0, y = 0, sx = 0, sy = 0;
        switch (this.door) {
            case NORTH:
                x = this.getX() - this.getSize()/4; sx = this.getSize()/4*3;
                y = this.getY() - this.getSize()/2; sy = this.getSize()/10;
				break;
			case EAST:
                x = this.getX() - this.getSize()/2; sx = this.getSize()/10;
                y = this.getY() - this.getSize()/4; sy = this.getSize()/4*3;
				break;
			case SOUTH:
                x = this.getX() - this.getSize()/4; sx = this.getSize()/4*3;
                y = this.getY() + this.getSize()/2 - this.getSize()/10; sy = this.getSize()/10;
				break;
			case WEST:
                x = this.getX() + this.getSize()/2 - this.getSize()/10; sx = this.getSize()/10;
                y = this.getY() - this.getSize()/4; sy = this.getSize()/4*3;
				break;
        }
        Rectangle rect_door = new Rectangle(x, y, sx, sy);
        rect_door.setFill(Color.BLACK);
        this.addRectangle(rect_door);
		this.addToLayer();
    }

	public boolean isSoldierIn(Soldier s) {
		boolean in = false;
		switch (this.door) {
			case NORTH:
				if (s.getY() > this.getY() && s.getY() < this.getY() + this.getSize()/10)
					if (s.getX() > this.getX() + this.getSize()/4 && s.getX() < this.getX() + this.getSize()/4*3)
						in = true;
				break;
			case EAST:
				if (s.getX() > this.getX() && s.getY() < this.getX() + this.getSize()/10)
					if (s.getY() > this.getY() + this.getSize()/4 && s.getY() < this.getY() + this.getSize()/4*3)
						in = true;
				break;
			case SOUTH:
				if (s.getY() > this.getY() + this.getSize()/10*9 && s.getY() < this.getY() + this.getSize())
					if (s.getX() > this.getX() + this.getSize()/4 && s.getX() < this.getX() + this.getSize()/4*3)
						in = true;
				break;
			case WEST:
				if (s.getX() > this.getX() + this.getSize()/10*9 && s.getY() < this.getX() + this.getSize())
					if (s.getY() > this.getY() + this.getSize()/4 && s.getY() < this.getY() + this.getSize()/4*3)
						in = true;
				break;
		}
		return in;
	}

}
