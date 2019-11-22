package board;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Sprite {
    private Pane layer;
    private int x, y;
    private Rectangle rect;

    public Sprite(Pane layer, Color c, int x, int y, int size) {
        this.rect = new Rectangle(x, y, size, size);
        this.rect.setFillColor(c);
        addToLayer();
    }

    public void addToLayer() {

    }

    public void removeFromLayer() {}

    public void setX(int x) {}
    public int getX() { return this.x; }
    public void setY(int x) {}
    public int getY() { return this.y; }
    public Rectangle getShape() {}
}
