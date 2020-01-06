package game.item;

import board.*;
import game.util.Production;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Soldier extends Sprite {
    private int id;
    private int owner;
    private int target;

    private int dx, dy, stuck;

    private Production type;
    private int lp;
    private int atk;
    private int spd;

    public Soldier(int id, Production t, int owner, int target, Pane layer, Color c, int x, int y) {
        // Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
    	super(layer,
				(owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR),
				x - Settings.SIZE_SOLDIER/2, y - Settings.SIZE_SOLDIER/2, Settings.SIZE_SOLDIER);
		this.id = id;
        this.owner = owner;
        this.target = target;

        this.dx = 0; this.dy = 0;
        this.stuck = 0;

        this.type = t;
        switch (t) {
            case Piquier:
                this.lp = 1;
                this.atk = 1;
                this.spd = 2;
                break;
            case Chevalier:
                this.lp = 3;
                this.spd = 5;
                this.atk = 6;
                break;
            case Onagre:
                this.lp = 5;
                this.atk = 10;
                this.spd = 1;
                break;
        }
    }

    public int getId() { return this.id; }
    public Production getType() { return this.type; }
    public int getOwner() { return this.owner; }
    public int getTarget() { return this.target; }
    public void setTarget(int t) { this.target = t; }
    public int getLP() { return this.lp; }
    public int getATK() { return this.atk; }
    public int getSPD() { return this.spd; }

    public Soldier copy() {
        Soldier n = new Soldier(this.id, this.type, this.owner, this.target, this.getLayer(), this.getColor(), this.getX(), this.getY());
        return n;
    }

    public void update(Castle c_target, ArrayList<Castle> avoid) {
        int old_x = this.getX(), old_y = this.getY();

        double dist = c_target.doorDistance(this.getX(), this.getY());
        double dist_p = c_target.doorDistance(this.getX() + this.spd, this.getY());
        double dist_m = c_target.doorDistance(this.getX() - this.spd, this.getY());
        if (dist_p > dist_m) {
            if (dist > dist_m) {
                this.dx = -1;
            } else {
                this.dx = 0;
            }
        } else if (dist > dist_p){
            this.dx = 1;
        } else {
            this.dx = 0;
        }

        if (this.dx != 0) {
            for (int i=0; i < avoid.size(); i++) {
                if (avoid.get(i).isIn(this.getX()+(dx*this.spd), this.getY(), this.getSize()))
                    this.dx = 0;
            }
        }

        dist = c_target.doorDistance(this.getX(), this.getY());
        dist_p = c_target.doorDistance(this.getX(), this.getY() + this.spd);
        dist_m = c_target.doorDistance(this.getX(), this.getY() - this.spd);
        if (dist_p > dist_m) {
            if (dist > dist_m) {
                this.dy = -1;
            } else {
                this.dy = 0;
            }
        } else if (dist > dist_p) {
            this.dy = 1;
        } else {
            this.dy = 0;
        }

        if (this.dy != 0) {
            for (int i=0; i < avoid.size(); i++) {
                if (avoid.get(i).isIn(this.getX(), this.getY() + (dy*this.spd), this.getSize()))
                    this.dy = 0;
            }
        }

        this.setX(this.getX() + (this.dx*this.spd));
        this.setY(this.getY() + (this.dy*this.spd));

        if (old_x == this.getX() && old_y == this.getY()) {
            this.stuck ++;
            if (c_target.doorDistance(this.getX() + this.spd, this.getY()) > c_target.doorDistance(this.getX() - this.spd, this.getY())) {
                this.setX(this.getX() - this.spd);
            } else {
                this.setX(this.getX() + this.spd);
            }

            if (c_target.doorDistance(this.getX(), this.getY() + this.spd) > c_target.doorDistance(this.getX(), this.getY() - this.spd)) {
                this.setY(this.getY() - this.spd);
            } else {
                this.setY(this.getY() + this.spd);
            }
        } else {
            this.stuck = 0;
        }

    }

    public boolean defend(Soldier s) {
        this.lp -= s.getATK();
        if (this.lp < 1) {
            return false;
        }
        return true;
    }
}
