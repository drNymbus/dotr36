package game.item;

import board.*;
import game.util.Production;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/**
 * Classe de gestion des soldats.
 */

public class Soldier extends Sprite {
	/** Numéro du soldat. */
	private int id;
	/** Propriétaire du soldat. */
	private int owner;
	/** Cible du soldat. */
	private int target;
	/** Type de soldat. */
	private Production type;
	/** Point de vie du soldat. */
	private int lp;
	/** Attaque du soldat. */
	private int atk;
	/** Vitesse du soldat. */
	private int spd;

	/**
	 * Créé un soldat.
	 * 
	 * @param id     Numéro du soldat.
	 * @param t      Type du soldat.
	 * @param owner  Propriétaire.
	 * @param target Cible.
	 * @param layer  Scène javafx.
	 * @param c      Couleur.
	 * @param x      Coordonnée en largeur.
	 * @param y      Coordonnée en hauteur.
	 */
	public Soldier(int id, Production t, int owner, int target, Pane layer, Color c, int x, int y) {
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

	/**
	 * Retourne le numéro du soldat.
	 * 
	 * @return Numéro associé au soldat.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Retourne le type du soldat.
	 * 
	 * @return Type( piquier/chevialier/onagre).
	 */
	public Production getType() {
		return this.type;
	}

	/**
	 * Retourne le propriétaire du soldat.
	 * 
	 * @return Propriétaire.(Settings.ALLY_ID/Settings.ENNEMY_ID)
	 */
	public int getOwner() {
		return this.owner;
	}

	/**
	 * Retourne la cible du soldat.
	 * 
	 * @return Cible.(Numéro du chateau)
	 */
	public int getTarget() {
		return this.target;
	}

	/**
	 * Défini la cible du soldat.
	 * 
	 * @param t Numéro du chateau à cibler.
	 */
	public void setTarget(int t) {
		this.target = t;
	}

	/**
	 * Retourne les poinds de vie du soldat.
	 * 
	 * @return Points de vie.
	 */
	public int getLP() {
		return this.lp;
	}

	/**
	 * Retourne les points d'attaque du soldat.
	 * 
	 * @return Points d'attaque.
	 */
	public int getATK() {
		return this.atk;
	}

	/**
	 * Retourne la vitesse du soldat.
	 * 
	 * @return Vitesse.
	 */
	public int getSPD() {
		return this.spd;
	}

	/**
	 * Copie le soldat pour en créer un nouveau.
	 * 
	 * @return Nouveau soldat.
	 */
	public Soldier copy() {
		Soldier n = new Soldier(this.id, this.type, this.owner, this.target, this.getLayer(), this.getColor(),
				this.getX(), this.getY());
		return n;
	}

	/**
	 * Met à jour la position du soldat sur le champ de bataille.
	 * 
	 * @param c     la cible du soldat.
	 * @param avoid La liste de Sprite à éviter.
	 */
	public void update(Castle c, ArrayList<Sprite> avoid) {
		int target_x = c.getX(), target_y = c.getY();
		switch (c.getDoor()) {
		case NORTH:
			target_y -= Settings.SIZE_CASTLE / 2;
			break;
		case EAST:
			target_x -= Settings.SIZE_CASTLE / 2;
			break;
		case SOUTH:
			target_y += Settings.SIZE_CASTLE / 2;
			break;
		case WEST:
			target_x = Settings.SIZE_CASTLE / 2;
			break;
		}
		int dx = 0, dy = 0;
		if (this.getX() != target_x)
			dx = (this.getX() - target_x > 0) ? -1 : 1;

		if (this.getY() != target_y)
			dy = (this.getY() - target_y > 0) ? -1 : 1;
		if (Math.abs(this.getY() - target_y) < this.spd) {
			this.setY(this.getY() + (dy));
		} else {
			this.setY(this.getY() + (dy * this.spd));
		}
		if (Math.abs(this.getX() - target_x) < this.spd) {
			this.setY(this.getY() + (dx));
		} else {
			this.setX(this.getX() + (dx * this.spd));
		}
		/*
		 * int dx = 0, dy = 0; int tmp = this.spd; boolean over = false; if (this.getX()
		 * != target_x) while (!over || tmp > 0) { dx = (this.getX() - target_x > 0) ?
		 * -1 : 1; Soldier cpy = this.copy(); cpy.setX(cpy.getX() + dx); for (int i=0; i
		 * < avoid.size(); i++) { if (avoid.get(i).isIn(this)) over = true; }
		 * 
		 * if (over) { this.setX(this.getX() + dx); tmp--; target_x = -1*(target_x); } }
		 * 
		 * tmp = this.spd; over = true; if (this.getY() != target_y) while (!over || tmp
		 * > 0) { dy = (this.getY() - target_y > 0) ? -1 : 1; Soldier cpy = this.copy();
		 * cpy.setY(cpy.getY() + dy); for (int i=0; i < avoid.size(); i++) { if
		 * (avoid.get(i).isIn(this)) over = true; }
		 * 
		 * if (!over) { this.setY(this.getY() + dy); tmp--; target_y = -1*(target_y); }
		 * }
		 */
	}

	/**
	 * Gestion de la défence. Perds des point de vie en fonction de l'attaque du
	 * soldat adverse.
	 * 
	 * @param s Soldat attaquant.
	 * @return true la défence est un succès, false sinon.
	 */
	public boolean defend(Soldier s) {
		this.lp -= s.getATK();
		if (this.lp < 1) {
			return false;
		}
		return true;
	}
}
