package game;

import board.*;
import javafx.scene.shape.*;
import javafx.scene.layout.Pane;

public class Soldier extends Sprite {
    private int id;
    private int owner;
    private int target;

    public Soldier(int id, int owner, int target, Pane layer, int x, int y) {
        Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
		super(layer, c, x, y, 1);
        this.id = id;
        this.owner = owner;
        this.target = target;
    }
}
