package game.item;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
/**
 * Classe de gestion des Sprite.
 *
 */
public class Sprite {
	private Pane layer;
	/** Coordonnée actuelle du Sprite. */
	private int x, y;
	/** Taille du Sprite. */
	private int size;
	/** Rectangle associé au Sprite. */
	private Rectangle base;
	/** Couleur du Sprite. */
	private Color color;
	/** Liste des rectangles ( Porte et Base ) */
	private ArrayList<Rectangle> rects;

	/**
	 * Créé un sprite.
	 *
	 * @param layer la scène javafx.
	 * @param c     la couleur du Sprite.
	 * @param x     la position sur la largeur.
	 * @param y     la position sur la hauteur.
	 * @param size  la taille du Sprite.
	 */
	public Sprite(Pane layer, Color c, int x, int y, int size) {
		this.rects = new ArrayList<Rectangle>();
		this.base = new Rectangle(x, y, size, size);
		this.base.setFill(c);
		this.color = c;
		this.x = (int) base.getX() + (size / 2);
		this.y = (int) base.getY() + (size / 2);
		this.size = size;
		this.layer = layer;
		this.addToLayer();
	}

	/**
	 * Ajoute un rectangle à la liste.
	 *
	 * @param r un rectangle.
	 */
	public void addRectangle(Rectangle r) {
		this.rects.add(r);
	}

	/**
	 * Ajoute les rectangles à la scène javafx.
	 */
	public void addToLayer() {
		this.layer.getChildren().add(this.base);
		for (int i = 0; i < this.rects.size(); i++)
			this.layer.getChildren().add(this.rects.get(i));
	}

	/**
	 * Retire les rectangles de la scène javafx.
	 */
	public void removeFromLayer() {
		this.layer.getChildren().remove(this.base);
		for (int i = 0; i < this.rects.size(); i++)
			this.layer.getChildren().remove(this.rects.get(i));
	}

	/**
	 * Retourne la scène javafx.
	 *
	 * @return la scène( Pane ).
	 */
	public Pane getLayer() {
		return this.layer;
	}

	/**
	 * Retourne la coordonnée du Sprite (largeur).
	 *
	 * @return la coordonnée(largeur).
	 */
	public int getX() {
		return this.x;
	}


	/**
	 * Retourne la coordonnée du Sprite (hauteur).
	 *
	 * @return la coordonnée(hauteur).
	 */
	public int getY() {
		return this.y;
	}

    /**
     * Retourne la couleur du Sprite.
     * @return la couleur.
     */
    public Color getColor() { return this.color; }

    /**
     * Repositionne la variable couleur du Sprite par la couleur c donnee.
     * @param c la couleur.
     */
    public void setColor(Color c) {
        this.color = c;
        this.base.setFill(c);
    }

    /**
	 * Défini la coordonnée du Sprite(largeur).
	 *
	 * @param x Coordonnée valide sur la grille.
	 */
    public void setX(int x) {
        this.x = x;
        this.base.setX(y - this.size / 2);
        for (int i = 0; i < this.rects.size(); i++) {
			Rectangle r = this.rects.get(i);
			r.setX(r.getX() + (y - r.getX()));
        }
    }

	/**
	 * Défini la coordonnée du Sprite(hauteur).
	 *
	 * @param y Coordonnée valide sur la grille.
	 */
	public void setY(int y) {
		this.y = y;
		this.base.setY(y - this.size / 2);
		for (int i = 0; i < this.rects.size(); i++) {
			Rectangle r = this.rects.get(i);
			r.setY(r.getY() + (y - r.getY()));
		}
	}
/**
 * Retourne le rectangle correspondant à la base.
 * @return La base.
 */
	public Rectangle getShape() {
		return this.base;
	}
/**
 * Retourne la taille du Sprite.
 * @return la taille.
 */
	public int getSize() {
		return this.size;
	}

/**
 * Vérifie si  ce Sprite  entre en collision avec le Sprite s.
 * @param s un Sprite.
 * @return True si il rentre en collision, false sinon.
 */
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
/**
 * Retourne la distance entre ce sprite et le Sprite s.
 * @param s un Sprite.
 * @return la distance entre les deux Sprites.
 */
	public double distance(Sprite s) {
		double tmpX = Math.pow((s.getX() - this.getX()), 2);
		double tmpY = Math.pow((s.getY() - this.getY()), 2);
		return Math.sqrt(tmpX + tmpY);
	}
/**
 * Retourne la distance entre ce sprite et une position.
 * @param x La coordonnée de la largeur.
 * @param y La coorodnnée de la hauteur.
 * @return
 */
	public double distance(int x, int y) {
		double tmpX = Math.pow((x - this.getX()), 2);
		double tmpY = Math.pow((y - this.getY()), 2);
		return Math.sqrt(tmpX + tmpY);
	}

}
