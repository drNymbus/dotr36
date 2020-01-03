package game;
import game.*;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.layout.Pane;

public class Battlefield {
    private int width, height;
    private ArrayList<Castle> castles;
    private int nbCastles;
    private ArrayList<Soldier> soldiers;
    private int nbSoldiers;

    private Pane layer;

    public Battlefield(int nb_castles, Pane layer, int w, int h) {
    	this.castles = new ArrayList<Castle>();
        this.nbCastles = nb_castles;
    	this.soldiers = new ArrayList<Soldier>(); // array accessible by id then get the list of castle id id;
        // this.nbSoldiers = 0;
    	this.width = w; this.height = h;

        Random rnd = new Random();
        for (int i=0; i < nb_castles; i++) {
            int x = rnd.nextInt(this.width);
            int y = rnd.nextInt(this.width);
            Castle c = new Castle(i, 0, layer, x, y, Direction.NORTH);
            this.castles.add(c);
        }
    }

    public Castle getCastle(int id) {
        for (int i=0; i < this.castles.size(); i++) {
            Castle c = this.castles.get(i);
            if (c.getId() == id) return c;
        }
        return null;
    }
    public ArrayList<Castle> getCastles() { return this.castles; }

    public Soldier getSoldier(int id) {
        for (int i=0; i < this.soldiers.size(); i++) {
            Soldier s = this.soldiers.get(i);
            if (s.getId() == id) return s;
        }
        return null;
    }

    public ArrayList<Soldier> getSoldiers(int owner) {
        ArrayList<Soldier> res = new ArrayList<Soldier>();
        for (int i=0; i < this.soldiers.size(); i++) {
            Soldier s = this.soldiers.get(i);
            if (s.getOwner() == owner) res.add(s);
        }
        return res;
    }

    public void attack(int id) {
        Castle c = this.getCastle(id);
        int nb_soldiers = c.attack(this.soldiers, this.layer);
    }

    public void update() {
        for (int i=0; i < this.castles.size(); i++) {
            Castle c = this.castles.get(i);
            this.attack(c.getId());
        }

        for (int i=0; i < this.soldiers.size(); i++) {
            Soldier s = this.soldiers.get(i);
            Castle c = this.getCastle(s.getTarget());
            s.update(c.getX(), c.getY());

            if (c.isIn(s)) {
                c.defend(s);
                this.soldiers.remove(i);
            }

        }
    }


}
