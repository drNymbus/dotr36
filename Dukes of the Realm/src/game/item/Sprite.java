package game.item;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Sprite {
    private Pane layer;
    private int x, y;
    private int size;
    private Rectangle base;
    private Color color;
    private ArrayList<Rectangle> rects;

    public Sprite(Pane layer, Color c, int x, int y, int size) {
        this.rects = new ArrayList<Rectangle>();
        this.base = new Rectangle(x, y, size, size);
        this.base.setFill(c);
        this.color = c;
        this.x=(int)base.getX()+(size/2);
        this.y=(int)base.getY()+(size/2);
        this.size = size;
        this.layer = layer;
        this.addToLayer();
    }

    public void addRectangle(Rectangle r) {
        this.rects.add(r);
    }

    // public void addToLayer() { this.layer.getChildren().add(this.rect); }
    public void addToLayer() {
        this.layer.getChildren().add(this.base);
        for (int i=0; i < this.rects.size(); i++) this.layer.getChildren().add(this.rects.get(i));
    }
    // public void removeFromLayer() { this.layer.getChildren().remove(this.rect); }
    public void removeFromLayer() {
        this.layer.getChildren().remove(this.base);
        for (int i=0; i < this.rects.size(); i++) this.layer.getChildren().remove(this.rects.get(i));
    }

    public Pane getLayer() { return this.layer; }

    public int getX() { return this.x; }
    public void setX(int x) {
        this.x = x; this.base.setX(x-this.size/2);
        for (int i=0; i < this.rects.size(); i++) {
            Rectangle r = this.rects.get(i);
            r.setX(r.getX() + (x - r.getX()));
        }
    }

    public int getY() { return this.y; }
    public void setY(int y) {
        this.y = y; this.base.setY(y-this.size/2);
        for (int i=0; i < this.rects.size(); i++) {
            Rectangle r = this.rects.get(i);
            r.setY(r.getY() + (y - r.getY()));
        }
    }

    public Rectangle getShape() { return this.base; }
    public int getSize() { return this.size; }

    public Color getColor() { return this.color; }
    public void setColor(Color c) {
        this.color = c;
        this.base.setFill(c);
    }

    public boolean isIn(Sprite s) {
        if (s.getX() - s.getSize()/2 > base.getX() + this.size || s.getX() + s.getSize()/2 < base.getX())
            return false;
        if (s.getY() - s.getSize()/2 > base.getY() + this.size || s.getY() + s.getSize()/2 < base.getY())
            return false;
        return true;
    }

    public boolean isIn(int sx, int sy, int ssize) {
        if (sx - ssize/2 > base.getX() + this.size || sx + ssize/2 < base.getX())
            return false;
        if (sy - ssize/2 > base.getY() + this.size || sy + ssize/2 < base.getY())
            return false;
        return true;
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
