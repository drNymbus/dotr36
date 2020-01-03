package game;

import board.*;
import javafx.scene.shape.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Soldier extends Sprite {
    private int id;
    private int owner;
    private int target;

    public Soldier(int id, int owner, int target, Pane layer, Color c, int x, int y) {
        // Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
        super(layer, c, x, y, 1);
        this.id = id;
        this.owner = owner;
        this.target = target;
    }

    public int getId() { return this.id; }
    public int getOwner() { return this.owner; }
    public int getTarget() { return this.target; }

    public void update(int target_x, int target_y) {
        int dx = 0, dy = 0;
        if (this.getX() != target_x)
            dx = (this.getX() - target_x > 0) ? -1 : 1;

        if (this.getY() != target_y)
            dx = (this.getY() - target_y > 0) ? -1 : 1;

        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }
}
