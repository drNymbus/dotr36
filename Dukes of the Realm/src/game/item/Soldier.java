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

    /* Direction du soldat */
    private int dx, dy;

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
        // Color c = (owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR);
    	super(layer,
				(owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR),
				x - Settings.SIZE_SOLDIER/2, y - Settings.SIZE_SOLDIER/2, Settings.SIZE_SOLDIER);
		this.id = id;
        this.owner = owner;
        this.target = target;

        this.dx = 0; this.dy = 0;

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
    public int getId() { return this.id; }

    /**
	 * Retourne le type du soldat.
	 *
	 * @return Type( piquier/chevialier/onagre).
	 */
    public Production getType() { return this.type; }

    /**
	 * Retourne le propriétaire du soldat.
	 *
	 * @return Propriétaire.(Settings.ALLY_ID/Settings.ENNEMY_ID)
	 */
    public int getOwner() { return this.owner; }

    /**
	 * Retourne la cible du soldat.
	 *
	 * @return Cible.(Numéro du chateau)
	 */
    public int getTarget() { return this.target; }

    /**
	 * Défini la cible du soldat.
	 *
	 * @param t Numéro du chateau à cibler.
	 */
    public void setTarget(int t) { this.target = t; }

    /**
	 * Retourne les poinds de vie du soldat.
	 *
	 * @return Points de vie.
	 */
    public int getLP() { return this.lp; }

    /**
	 * Retourne les points d'attaque du soldat.
	 *
	 * @return Points d'attaque.
	 */
    public int getATK() { return this.atk; }

    /**
	 * Retourne la vitesse du soldat.
	 *
	 * @return Vitesse.
	 */
    public int getSPD() { return this.spd; }

    /**
	 * Copie le soldat pour en créer un nouveau.
	 *
	 * @return Nouveau soldat.
	 */
    public Soldier copy() {
        Soldier n = new Soldier(this.id, this.type, this.owner, this.target, this.getLayer(), this.getColor(), this.getX(), this.getY());
        return n;
    }

    /**
	 * Met à jour la position du soldat sur le champ de bataille.
	 *
	 * @param c     la cible du soldat.
	 * @param avoid La liste de Sprite à éviter.
	 */
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
        }

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
