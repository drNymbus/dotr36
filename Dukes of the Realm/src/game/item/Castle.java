package game.item;

import board.*;
import game.util.Direction;
import game.util.Production;
import game.util.ProductionTab;
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
	private int door_x, door_y;
	private int door_sx, door_sy;

	private int tresor;
	private int level;

	public Castle(int id, int owner, Pane layer, int x, int y, Direction d) {
		super(layer,
				(owner == Settings.ALLY_ID) ? Settings.ALLY_COLOR : ((owner == Settings.ENNEMY_ID) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR),
				x - Settings.SIZE_CASTLE/2, y - Settings.SIZE_CASTLE/2, Settings.SIZE_CASTLE);

		this.id = id;
		this.owner = owner;
		this.target = -1;
		this.nb_soldiers = 0;

		this.production = new ProductionTab();
		this.soldiers = new ArrayList<Soldier>();
		this.door = d;
		switch (this.door) {
			case NORTH:
				this.door_sx = Settings.SIZE_CASTLE/2;
				this.door_sy = Settings.SIZE_CASTLE/10;

				this.door_x = x - this.door_sx/2;
				this.door_y = y - Settings.SIZE_CASTLE/2;
				break;
			case EAST:
				this.door_sx = Settings.SIZE_CASTLE/10;
				this.door_sy = Settings.SIZE_CASTLE/2;

				this.door_x = x + Settings.SIZE_CASTLE/2 - this.door_sx;
				this.door_y = y - this.door_sy/2;
				break;
			case SOUTH:
				this.door_sx = Settings.SIZE_CASTLE/2;
				this.door_sy = Settings.SIZE_CASTLE/10;

				this.door_x = x - this.door_sx/2;
				this.door_y = y + Settings.SIZE_CASTLE/2 - this.door_sy;
				break;
			case WEST:
				this.door_sx = Settings.SIZE_CASTLE/10;
				this.door_sy = Settings.SIZE_CASTLE/2;

				this.door_x = x - Settings.SIZE_CASTLE/2;
				this.door_y = y - this.door_sy/2;
				break;
		}
		this.drawCastle();

		this.reserve = 0;
		Random rnd = new Random();
		if (this.owner == Settings.NEUTRAL_ID) {
			this.level=(1+rnd.nextInt(3));
			this.tresor=(rnd.nextInt(100));
		} else {
			this.level=1;
			this.tresor=0;
		}
	}

	public Direction getDoor() { return this.door; }
	public int getDoorX() { return this.door_x; }
	public int getDoorY() { return this.door_y; }

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
					tresor -= 1000;
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
		if (this.production.size() == 0)
			return;
		Production prod = this.production.getProduction();
		if (prod == Production.Level) {
			this.level++;
		} else if (prod != Production.None) {
			Soldier s = new Soldier(this.soldiers.size(), prod, this.owner, this.target, layer, this.getColor(), this.getX(), this.getY());
			this.soldiers.add(s);
		}
	}

	public void attack(int target, ArrayList<Soldier> sdrs, int n) {
		this.nb_soldiers = n;
		this.setTarget(target);
		int i = 0;
		while (i < this.nb_soldiers && this.soldiers.size() > 0) {
			Soldier s = this.soldiers.get(i);
			s.setX(this.door_x + this.door_sx/2); s.setY(this.door_y + this.door_sy/2);
			switch (this.door) {
				case NORTH:
					s.setY(s.getY() - s.getSize() - 2);
					break;
				case EAST:
					s.setX(s.getX() + s.getSize() + 2);
					break;
				case SOUTH:
					s.setY(s.getY() + s.getSize() + 2);
					break;
				case WEST:
					s.setX(s.getX() - s.getSize() - 2);
					break;
			}
			// s.addToLayer();
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
		if (this.soldiers.size() > 0) {
			Soldier dfd = this.soldiers.get(0);
			if (dfd.defend(atk)) {
				if (!atk.defend(dfd)) {
					atk.removeFromLayer();
					sdrs.remove(atk);
				}
			} else {
				dfd.removeFromLayer();
				this.soldiers.remove(0);
			}
		}

		if (this.soldiers.size() <= 0) {
			this.owner = atk.getOwner();
			this.setColor((owner == Settings.ALLY_ID) ? Settings.ALLY_COLOR : ((owner == Settings.ENNEMY_ID) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR));
			this.production.reset();
			while (this.soldiers.size() > 0) this.soldiers.remove(0);
		} else {
			Soldier dfd = this.soldiers.get(0);
			if (!atk.defend(dfd)) {
				atk.removeFromLayer();
				sdrs.remove(atk);
			}
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
        Rectangle rect_door = new Rectangle(this.door_x, this.door_y, this.door_sx, this.door_sy);
        rect_door.setFill(Color.BLACK);
        // this.addRectangle(rect_door);
        this.getLayer().getChildren().add(rect_door);
    }

	public double doorDistance(int x, int y) {
		double tmpX = Math.pow((x - (this.door_x + this.door_sx/2)), 2);
		double tmpY = Math.pow((y - (this.door_y + this.door_sy/2)), 2);
		return Math.sqrt(tmpX + tmpY);
	}

	public boolean isSoldierIn(Soldier s) {
		if (s.getX() - s.getSize()/2 > this.door_x + this.door_sx || s.getX() + s.getSize()/2 < this.door_x)
            return false;
        if (s.getY() - s.getSize()/2 > this.door_y + this.door_sy || s.getY() + s.getSize()/2 < this.door_y)
            return false;
        return true;
	}

}
