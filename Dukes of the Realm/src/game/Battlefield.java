package game;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.layout.Pane;

public class Battlefield {
    private int width, height;
    private ArrayList<Castle> castles;
    private ArrayList<Soldier> soldiers;

    public Battlefield(int nb_castles, Pane layer, int w, int h) {
    	this.castles = new ArrayList<Castle>();
    	this.soldiers = new ArrayList<Soldier>();
    	this.width = w; this.height = h;

        Random rnd = new Random();
        for (int i=0; i < nb_castles; i++) {
            int x = rnd.nextInt(this.width);
            int y = rnd.nextInt(this.width);
            Castle c = new Castle(i, layer, x, y);
            this.castles.add(c);
        }
    }

    public ArrayList<Castle> getCastles() { return this.castles; }
    public ArrayList<Soldier> getSoldiers() { return this.soldiers; }

    public void update() {
        for (Castle c: this.castles) {
            c.update();
        }
    }

}
