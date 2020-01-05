package game;

import board.*;
import javafx.scene.shape.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Soldier extends Sprite {
    private int id;
    private int owner;
    private int target;

    private Production type;
    private int lp;
    private int atk;
    private int spd;

    public Soldier(int id, Production t, int owner, int target, Pane layer, Color c, int x, int y) {
        // Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
    	super(layer,
				(owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR),
				x, y, Settings.SIZE_SOLDIER);
		this.id = id;
        this.owner = owner;
        this.target = target;

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

    public void update(Castle c, ArrayList<Sprite> avoid) {
        int target_x = c.getX(), target_y = c.getY();
        switch (c.getDoor()) {
            case NORTH:
                target_y -= Settings.SIZE_CASTLE/2;
                break;
            case EAST:
                target_x += Settings.SIZE_CASTLE/2;
                break;
            case SOUTH:
                target_y += Settings.SIZE_CASTLE/2;
                break;
            case WEST:
                target_x -= Settings.SIZE_CASTLE/2;
                break;
        }

        int dx = 0, dy = 0;
        int tmp = this.spd; boolean over = false;
        if (this.getX() != target_x)
            while (!over || tmp > 0) {
                dx = (this.getX() - target_x > 0) ? -1 : 1;
                Soldier cpy = this.copy();
                cpy.setX(cpy.getX() + dx);
                for (int i=0; i < avoid.size(); i++) {
                    if (avoid.get(i).isIn(this)) over = true;
                }

                if (over) {
                    this.setX(this.getX() + dx);
                    tmp--;
                    target_x = -1*(target_x);
                }
            }

        tmp = this.spd; over = true;
        if (this.getY() != target_y)
            while (!over || tmp > 0) {
                dy = (this.getY() - target_y > 0) ? -1 : 1;
                Soldier cpy = this.copy();
                cpy.setY(cpy.getY() + dy);
                for (int i=0; i < avoid.size(); i++) {
                    if (avoid.get(i).isIn(this)) over = true;
                }

                if (!over) {
                    this.setY(this.getY() + dy);
                    tmp--;
                    target_y = -1*(target_y);
                }
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
