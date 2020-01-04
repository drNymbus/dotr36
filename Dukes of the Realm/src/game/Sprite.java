package game;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Sprite {
    private Pane layer;
    private int x, y;
    private Rectangle rect;
    private Color color;

    public Sprite(Pane layer, Color c, int x, int y, int size) {
        this.rect = new Rectangle(x, y, size, size);
        this.rect.setFill(c);
        this.color = c;
        this.x=x;
        this.y=y;
        this.layer = layer;
        this.addToLayer();
    }

    public void addToLayer() { this.layer.getChildren().add(rect); }

    public void removeFromLayer() { this.layer.getChildren().remove(rect); }

    public void setX(int x) { this.x = x; this.rect.setX(x); }
    public int getX() { return this.x; }
    public void setY(int x) { this.y = x; this.rect.setY(x); }
    public int getY() { return this.y; }

    public Rectangle getShape() { return this.rect; }
    public int getSize() { return (int) this.rect.getWidth(); }

    public Color getColor() { return this.color; }

    public boolean isIn(Sprite s) {
        int x = s.getX(), y = s.getY();
        if (x >= this.x && x <= this.x + this.getSize()) {
            if (y >= this.y && y <= this.y + this.getSize()) return true;
        }
        return false;
    }

    public double distance(Sprite s) {
		double tmpX = Math.pow((s.getX() - this.getX()), 2);
		double tmpY = Math.pow((s.getY() - this.getY()), 2);
		return Math.sqrt(tmpX + tmpY);
	}

    public double distance(int x, int y) {
		double tmpX = Math.pow((x - this.getX()), 2);
		double tmpY = Math.pow((y - this.getY()), 2);
		return Math.sqrt(tmpX + tmpY);
	}

}
