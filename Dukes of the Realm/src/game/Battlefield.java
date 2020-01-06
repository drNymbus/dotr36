package game;

import java.util.ArrayList;
import java.util.Random;

// import board.Input;
// import board.Settings;
import board.*;
import game.item.Castle;
import game.item.Soldier;
import game.item.Sprite;
import game.util.Direction;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class Battlefield {
	private int width, height;
	private ArrayList<Castle> castles;
	private ArrayList<Soldier> soldiers;

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
		Direction[] dirs = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

		int i = nb_castles; boolean base = true;
		while (i > 0 && i != 1) {
			if (base) {
				int x = rnd.nextInt(this.width - Settings.SIZE_CASTLE);
				int y = rnd.nextInt(this.height/2 - Settings.SIZE_CASTLE) + this.height/2;

				Castle c = new Castle(i, Settings.ALLY_ID, this.layer, x, y, dirs[rnd.nextInt(4)]);
				this.castles.add(c);
				c = new Castle(i-1, Settings.ENNEMY_ID, this.layer, (this.width - x), (this.height - y), dirs[rnd.nextInt(4)]);
				this.castles.add(c);

				base = false;
			} else {
				int x = this.width/2, y = this.height/2;
				boolean pos_ok = false;

				while (i > 0 && !pos_ok) {
					x = rnd.nextInt(this.width - Settings.SIZE_CASTLE);
					y = rnd.nextInt(this.height/2 - Settings.SIZE_CASTLE) + this.height/2;

					for (Castle c : this.castles)
						if (c.distance(x,y) > Settings.SIZE_CASTLE * 2) pos_ok = true;
				}

				Castle c = new Castle(i, Settings.NEUTRAL_ID, this.layer, x, y, dirs[rnd.nextInt(4)]);
				this.castles.add(c);
				c = new Castle(i-1, Settings.NEUTRAL_ID, this.layer, (this.width - x), (this.height - y), dirs[rnd.nextInt(4)]);
				this.castles.add(c);
			}

			i -= 2;
		}

		if (i == 1) {
			Castle c = new Castle(i, Settings.NEUTRAL_ID, this.layer, this.width/2, this.height/2, dirs[rnd.nextInt(4)]);
			this.castles.add(c);
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

	public ArrayList<Castle> getCastles(int id) {
		ArrayList<Castle> res = new ArrayList<Castle>();
		for (int i=0; i < this.castles.size(); i++) {
			Castle c = this.castles.get(i);
			if (c.getId() != id) res.add(c);
		}
		return res;
	}
	public ArrayList<Castle> getCastles() { return this.getCastles(-1); }

	public Soldier getSoldier(int id) {
		for (int i = 0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			if (s.getId() == id)
				return s;
		}
		return null;
	}

	public ArrayList<Soldier> getSoldiers() { return this.soldiers; }

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
					break;
			}

			c.updateGold();
			c.updateProduction(this.layer);
		}

		if(allycount==0)
			gameover=Settings.ENNEMY_ID;
		if(enemycount==0)
			gameover=Settings.ALLY_ID;

		for (int i = 0; i < this.soldiers.size(); i++) {
			// System.out.println("NB_SOLDIERS:" + this.soldiers.size());
			Soldier s = this.soldiers.get(i);
			Castle c = this.getCastle(s.getTarget());
			// ArrayList<Sprite> avoid = new ArrayList<Sprite>(this.getCastles(s.getOwner()));
			s.update(c, this.getCastles());
			if (c.isSoldierIn(s)) {
				System.out.println("Soldier:" + s.getId() + " in Castle:" + c.getId());
				if(c.getOwner() == s.getOwner()) {
					c.addSoldier(s);
					this.soldiers.remove(i);
				}
				c.defend(this.soldiers, s);
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
