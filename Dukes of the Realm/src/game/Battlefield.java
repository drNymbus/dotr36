package game;

import game.*;

import java.util.ArrayList;
import java.util.Random;

import board.Input;
import board.Settings;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class Battlefield {
	private int width, height;

	private ArrayList<Castle> castles;
	private int nbCastles;

	private ArrayList<Soldier> soldiers;
	private int nbSoldiers;

	public boolean pause=false;
	private Pane layer;
	private Input input;
	public int gameover=0;
	public Battlefield(int nb_castles, Pane layer, Input in, int w, int h) {
		this.castles = new ArrayList<Castle>();
		this.soldiers = new ArrayList<Soldier>();

		this.width = w;
		this.height = h;

		this.input = in;
		this.layer=layer;

		Random rnd = new Random();
		int i = 0;
		while (i < nb_castles) {
			int owner = (i == 0) ? Settings.ALLY_ID : ((i == 1) ? Settings.ENNEMY_ID : Settings.NEUTRAL_ID);

			int x = rnd.nextInt(this.width - Settings.CASTLE_SIZE);
			int y = rnd.nextInt(this.height - Settings.CASTLE_SIZE);

			Direction[] dirs = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
			Direction d = dirs[rnd.nextInt(4)];

			boolean pos_ok = false;
			while (i > 0 && !pos_ok) {
				x = rnd.nextInt(this.width - Settings.CASTLE_SIZE);
				y = rnd.nextInt(this.height - Settings.CASTLE_SIZE);

				if (i == 1) {
					if (this.castles.get(0).distance(x,y) < 500)
						pos_ok = true;
				} else {
					for (Castle c : this.castles) {
						if (c.distance(x,y) > 100) pos_ok = true;
					}
				}

			}

			Castle c = new Castle(i, owner, this.layer, x, y, d);
			this.castles.add(c);
			i++;
		}
	}

	public Castle getCastle(int id) {
		for (int i = 0; i < this.castles.size(); i++) {
			Castle c = this.castles.get(i);
			if (c.getId() == id)
				return c;
		}
		return null;
	}

	public ArrayList<Castle> getCastles() {
		return this.castles;
	}

	public Soldier getSoldier(int id) {
		for (int i = 0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			if (s.getId() == id)
				return s;
		}
		return null;
	}

	public ArrayList<Soldier> getSoldiers(int owner) {
		ArrayList<Soldier> res = new ArrayList<Soldier>();
		for (int i = 0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			if (s.getOwner() == owner)
				res.add(s);
		}
		return res;
	}

	public void update() {
		int allycount=0;
		int enemycount=0;
		for (int i = 0; i < this.castles.size(); i++) {
			Castle c = this.castles.get(i);
			switch (c.getOwner()) {
			case Settings.ALLY_ID:
				allycount++;
				break;
			case Settings.ENNEMY_ID:
				enemycount++;
			default:
				break;
			}
			
			c.updateGold();
			c.updateProduction(this.layer);
			if(c.getTarget()!=-1)
				c.attack(this.soldiers);
		}
		if(allycount==0)
			gameover=Settings.ENNEMY_ID;
		if(enemycount==0)
			gameover=Settings.ALLY_ID;
		
		//System.out.println(this.soldiers.size());
		for (int i = 0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			Castle c = this.getCastle(s.getTarget());
			s.update(c.getX(), c.getY());
			if (c.isIn(s)) {
				if(c.getOwner()==s.getOwner())
					c.addSoldier();
				c.defend(s);
				this.soldiers.remove(i);
			}
		}
	}

	public void processInput(Input input, long now) {
		if (input.isExit()) {
			Platform.exit();
			System.exit(0);
		}
		else if(input.isPause())
			pause=true;
		else if(input.isResume())
			pause=false;

	}
}
