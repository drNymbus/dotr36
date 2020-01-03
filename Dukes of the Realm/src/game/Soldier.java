package game;

import board.*;
import javafx.scene.shape.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Soldier extends Sprite {
    private int id;
    private int owner;
    private int target;

    private TypeSoldier type;
    private int lp;
    private int atk;
    private int spd;



    public Soldier(int id, TypeSoldier t, int owner, int target, Pane layer, Color c, int x, int y) {
        // Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
    	super(layer,
				(owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR),
				x, y, 4);
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
    public TypeSoldier getType() { return this.type; }
    public int getOwner() { return this.owner; }
    public int getTarget() { return this.target; }
    public void setTarget(int t) { this.target = t; }

    public void update(int target_x, int target_y) {
        int dx = 0, dy = 0;
        if (this.getX() != target_x)
            dx = (this.getX() - target_x > 0) ? -1 : 1;

        if (this.getY() != target_y)
            dy = (this.getY() - target_y > 0) ? -1 : 1;

        this.setX(this.getX() + (dx * this.spd));
        this.setY(this.getY() + (dy * this.spd));
    }
}
