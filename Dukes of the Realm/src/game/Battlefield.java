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

	public Battlefield(int nb_castles, Pane layer, Input in, int w, int h) {
		this.castles = new ArrayList<Castle>();
		this.soldiers = new ArrayList<Soldier>();
		this.width = w;
		this.height = h;
		this.input = in;
		this.layer=layer;
		Random rnd = new Random();
		int i = 0;
		// while(castles.size()<nb_castles) {
		while (i < nb_castles) {
			int x = rnd.nextInt(this.width - Settings.CASTLE_SIZE);
			int y = rnd.nextInt(this.height - Settings.CASTLE_SIZE);
			Castle c;
			boolean add = true;
			// ally castle
			if (i == 0) {
				c = new Castle(i, 1, layer, x, y, Direction.NORTH);
			}
			// enemy castle
			else if (i == 1) {
				c = new Castle(i, -1, layer, x, y, Direction.NORTH);
			}
			// neutral castle 
			else {
				c = new Castle(i, 0, layer, x, y, Direction.NORTH);
			}
			// collision detector
			for (Castle castle : castles) {
				if (castle.distance(c) < 100)
					add = false;
			}
			if (i == 1) {
				if (c.distance(castles.get(0)) < 500)
					add = false;
			}
			if (add) {
				this.castles.add(c);
				c.addToLayer();
				i++;
			}
		}
	}

	/*
	 * public Battlefield(int nb_castles, Pane layer, Input in, int w, int h) {
	 * this.castles = new ArrayList<Castle>(); this.nbCastles = nb_castles;
	 * this.soldiers = new ArrayList<Soldier>(); // array accessible by id then get
	 * the list of castle id id; // this.nbSoldiers = 0; this.width = w; this.height
	 * = h;
	 * 
	 * Random rnd = new Random(); for (int i=0; i < nb_castles; i++) { int x =
	 * rnd.nextInt(this.width); int y = rnd.nextInt(this.width); Castle c = new
	 * Castle(i, 0, layer, x, y, Direction.NORTH); this.castles.add(c); } }
	 */

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
		for (int i = 0; i < this.castles.size(); i++) {
			Castle c = this.castles.get(i);
			c.updateGold();
			c.updateProduction();	
			if(c.getTarget()!=-1)
				c.attack(this.soldiers, this.layer);	
		}
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
