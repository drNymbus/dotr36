package game;

import java.util.ArrayList;
import java.util.Random;

import board.Input;
import board.Settings;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class Battlefield {
	private int width, height;
	private ArrayList<Castle> castles;
	private ArrayList<Soldier> soldiers;
	private Input input;

	public Battlefield(int nb_castles, Pane layer, Input in, int w, int h) {
		this.castles = new ArrayList<Castle>();
		this.soldiers = new ArrayList<Soldier>();
		this.width = w;
		this.height = h;
		this.input = in;

		Random rnd = new Random();
		int i = 0;
		// while(castles.size()<nb_castles) {
		while (i < nb_castles) {
			int x = rnd.nextInt(this.height - Settings.CASTLE_SIZE);
			int y = rnd.nextInt(this.width - Settings.CASTLE_SIZE);
			Castle c;
			boolean add = true;
			// ally castle
			if (i == 0)
				c = new Castle(i, 1, layer, x, y);
			// enemy castle
			else if (i == 1)
				c = new Castle(i, -1, layer, x, y);
			// neutral castle
			else
				c = new Castle(i, 0, layer, x, y);
			// collision detector
			for (Castle castle : castles) {
				if (castle.distance(c) < 100)
					add = false;
			}
			if (add) {
				this.castles.add(c);
				c.addToLayer();
				i++;
			}
		}
	}

	public ArrayList<Castle> getCastles() {
		return this.castles;
	}

	public ArrayList<Soldier> getSoldiers() {
		return this.soldiers;
	}

	public void update() {
		for (Castle c : this.castles) {
			c.update();
		}
	}

	public void processInput(Input input, long now) {
		if (input.isExit()) {
			Platform.exit();
			System.exit(0);
		}
	}
}
