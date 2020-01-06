package game.item;

import board.*;
import game.util.Direction;
import game.util.Production;
import game.util.ProductionTab;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.Pane;

/**
 * Classe de gestion des chateaux.
 *
 */
public class Castle extends Sprite   {
	/** Numéro du chateau. */
	private int id;
	/** Propriétaire du chateau. */
	private int owner;
	/** Liste de production. */
	private ProductionTab production;
	/** Liste des soldats présents. */
	private ArrayList<Soldier> soldiers;
	/** Cible actuelle.-1 correspond à aucunne cible. */
	private int target;
	/** Nombre de soldat. */
	private int nb_soldiers;
	/** Direction de la porte. */
	private Direction door;
	/** Quantité du trésor. Permet la production d'unité. */
	private int tresor;
	/** Niveau du chateau. */
	private int level;

	/**
	 * Créé un chateau
	 * 
	 * @param id    le buméro du chateau.
	 * @param owner le propriétaire.
	 * @param layer la scène javafx.
	 * @param x     la position sur la largeur.
	 * @param y     la position sur la hauteur.
	 * @param d     la direction de la porte.
	 */
	public Castle(int id, int owner, Pane layer, int x, int y, Direction d) {
		super(layer,
				(owner == 1) ? Settings.ALLY_COLOR : ((owner == -1) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR),
				x, y, Settings.SIZE_CASTLE);
		this.id = id;
		this.owner = owner;
		this.target = -1;
		this.nb_soldiers = 0;
		this.production = new ProductionTab();
		this.soldiers = new ArrayList<Soldier>();
		this.door = d;
		drawCastle();
		Random rnd = new Random();
		if (owner == 0) {
			this.level = (1 + rnd.nextInt(3));
			this.tresor = (rnd.nextInt(100));
		} else {
			this.level = 1;
			this.tresor = 0;
		}
	}

	/**
	 * Créé l'armée des chateaux neutre.
	 */
	public void initNeutral() {
		Random r = new Random();

		int armySyze = 7;
		for (int i = 0; i < armySyze; i++) {
			int tmp = r.nextInt(3);
			switch (tmp) {
			case 0:
				Soldier s = new Soldier(this.soldiers.size(), Production.Chevalier, this.owner, this.target,
						this.getLayer(), this.getColor(), this.getX(), this.getY());
				s.removeFromLayer();
				this.soldiers.add(s);
				break;
			case 1:
				Soldier s1 = new Soldier(this.soldiers.size(), Production.Piquier, this.owner, this.target,
						this.getLayer(), this.getColor(), this.getX(), this.getY());
				s1.removeFromLayer();
				this.soldiers.add(s1);
				break;
			case 2:
				Soldier s2 = new Soldier(this.soldiers.size(), Production.Onagre, this.owner, this.target,
						this.getLayer(), this.getColor(), this.getX(), this.getY());
				s2.removeFromLayer();
				this.soldiers.add(s2);
				break;
			}
		}
	}

	/**
	 * Retourne la direction de la porte.
	 * 
	 * @return la direction de la porte, soit nord, soit est, soit ouest, soit sud.
	 */
	public Direction getDoor() {
		return this.door;
	}

	/**
	 * Renvoie le trésor d'un chateau.
	 * 
	 * @return la valeur de tresor.(int)
	 */
	public int getTresor() {
		return tresor;
	}

	/**
	 * change la valeur du trésor d'un chateau.
	 * 
	 * @param tresor la valeur du trésor à mettre.
	 */
	public void setTresor(int tresor) {
		this.tresor = tresor;
	}

	/**
	 * Renvoie le niveau d'un chateau
	 * 
	 * @return le niveau du chateau.(int)
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Change le niveau d'un chateau
	 * 
	 * @param level le nouveau niveau du chateau.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Renvoie le numéro du chateau
	 * 
	 * @return Le numéro associé au chateau, défini lors de la création du
	 *         Battlefield.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Défini le propriétaire du chateau.
	 * 
	 * @param o le nouveau propriétaire.
	 *          Settings.NEUTRAL_ID/Settings.ALLY_ID/Settings.ENNEMY_ID.
	 */
	public void setOwner(int o) {
		this.owner = o;
	}

	/**
	 * Renvoie le propriétaire actuel du chateau
	 * 
	 * @return soit ettings.NEUTRAL_ID soit Settings.ALLY_ID soit
	 *         Settings.ENNEMY_ID.
	 */
	public int getOwner() {
		return this.owner;
	}

	/**
	 * Défini la cible d'un chateau.
	 * 
	 * @param t l'id d'un chateau.
	 */
	public void setTarget(int t) {
		this.target = t;
		for (int i = 0; i < this.soldiers.size(); i++)
			this.soldiers.get(i).setTarget(t);
	}

	/**
	 * Renvoie la cible actuelle d'un chateau.
	 * 
	 * @return la cible.
	 */
	public int getTarget() {
		return this.target;
	}

	/**
	 * Renvoie le nombre de soldat présent dans un chateau.
	 * 
	 * @return le nombre de soldat.
	 */
	public int getNbSoldiers() {
		return this.soldiers.size();
	}

	/**
	 * Liste des soldats présents dans le chateau sous forme de texte.
	 * 
	 * @return le message correspondant à la liste des soldats présents.
	 */
	public String getSoldiers() {
		String msg = "";
		for (int i = 0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			msg += " - " + s.getType() + "(" + s.getLP() + ")\n";
		}
		return msg;
	}

	/**
	 * Renvoie les productions du chateau.
	 * 
	 * @return les productions du chateau.
	 */
	public ProductionTab getProduction() {
		return this.production;
	}

	/**
	 * Ajoute un soldat à laliste des soldats du chateau
	 * 
	 * @param s un soldat.
	 */
	public void addSoldier(Soldier s) {
		s.removeFromLayer();
		this.soldiers.add(0, s);
	}

	/**
	 * Gestion de la production des soldats du chateau.
	 * 
	 * @param t la production à ajouter
	 */
	public void addProd(Production t) {
		boolean add = false;
		switch (t) {
		case Piquier:
			if (this.tresor > 100) {
				add = true;
				tresor -= 100;
			}
			break;
		case Chevalier:
			if (this.tresor > 500) {
				add = true;
				tresor -= 500;
			}
			break;

		case Onagre:
			if (this.tresor > 1000) {
				add = true;
				tresor -= 100;
			}
			break;
		}
		if (add)
			this.production.add(t);
	}

	/**
	 * Gestio de la montée en niveau du chateau
	 */
	public void levelup() {
		if (tresor > (1000 * this.level)) {
			tresor -= (1000 * this.level);
			this.production.add(100 + (50 * this.level));
		}
	}

	/**
	 * Met à jour la production
	 * 
	 * @param layer la scène javafx.
	 */
	public void updateProduction(Pane layer) {
		int x = this.getX() + Settings.SIZE_CASTLE / 2;
		int y = this.getY() + Settings.SIZE_CASTLE / 2;
		switch (this.door) {
		case NORTH:
			y -= Settings.SIZE_SOLDIER / 2;
			break;
		case EAST:
			x += Settings.SIZE_CASTLE / 2;
			break;
		case SOUTH:
			y += Settings.SIZE_SOLDIER / 2;
			break;
		case WEST:
			x -= Settings.SIZE_CASTLE / 2;
			break;
		}

		if (this.production.size() == 0)
			return;
		Production prod = this.production.getProduction();
		if (prod == Production.Level) {
			this.level++;
		} else if (prod != Production.None) {
			Soldier s = new Soldier(this.soldiers.size(), prod, this.owner, this.target, layer, this.getColor(),
					this.getX(), this.getY());
			s.removeFromLayer();
			this.soldiers.add(s);
		}
	}
/**
 * Arrête la production actuelle du château.
 */
	public void stopProduction() {
		if (this.production.size() == 0)
			return;
		int time = production.getProdRemaining(0);
		this.tresor += time * 20;
		production.remove();
	}

	/**
	 * Gestion de l'attaque.
	 * 
	 * @param target l'id de la cible (chateau).
	 * @param sdrs   la liste de soldat.
	 * @param n      le nombre de soldat à envoyer
	 */
	public void attack(int target, ArrayList<Soldier> sdrs, int n) {
		this.nb_soldiers = n;
		this.setTarget(target);
		int i = 0;
		while (i < this.nb_soldiers && this.soldiers.size() > 0) {
			Soldier s = this.soldiers.get(0);
			switch (this.door) {
			case NORTH:
				s.setY(s.getY() - s.getSize() / 2);
				break;
			case EAST:
				s.setX(s.getX() + s.getSize() / 2);
				break;
			case SOUTH:
				s.setY(s.getY() + s.getSize() / 2);
				break;
			case WEST:
				s.setX(s.getX() - s.getSize() / 2);
				break;
			}
			s.addToLayer();
			sdrs.add(s);
			this.soldiers.remove(0);
			i++;
		}
	}

	/**
	 * Gestion de l'attaque
	 * 
	 * @param sdrs la liste de soldat.
	 */
	public void attack(ArrayList<Soldier> sdrs) {
		this.attack(this.target, sdrs, this.nb_soldiers);
	}

	/**
	 * Gestion de l'arrêt de l'attaque.
	 */
	public void stopAttack() {
		this.target = -1;
		for (int i = 0; i < this.soldiers.size(); i++)
			this.soldiers.get(i).setTarget(-1);
		this.nb_soldiers = 0;
	}

	/**
	 * Gestion de la défence du chateau.
	 * 
	 * @param sdrs la liste de soldat présent sur le champ de bataille.
	 * @param atk  le soldat attaquant.
	 */
	public void defend(ArrayList<Soldier> sdrs, Soldier atk) {
		if (this.soldiers.size() <= 0) {
			this.owner = atk.getOwner();
			this.getShape().setFill((owner == Settings.ALLY_ID) ? Settings.ALLY_COLOR
					: ((owner == Settings.ENNEMY_ID) ? Settings.ENNEMY_COLOR : Settings.NEUTRAL_COLOR));
			this.production.reset();
			atk.removeFromLayer();
			return;
		}
		Soldier dfd = this.soldiers.get(0);
		if (dfd.defend(atk)) {
			if (!atk.defend(dfd))
				atk.removeFromLayer();
			sdrs.remove(atk);
		} else {
			dfd.removeFromLayer();
			this.soldiers.remove(0);
		}

	}

	/**
	 * Raffraichi le trésor du chateau.
	 */
	public void updateGold() {
		if (owner == 0)
			tresor += level;
		else
			tresor += (10 * level);
	}

	/**
	 * Ajoute une porte au chateau.
	 */
	public void drawCastle() {
		int x = 0, y = 0, sx = 0, sy = 0;
		switch (this.door) {
		case NORTH:
			x = this.getX() - this.getSize() / 4;
			sx = this.getSize() / 2;
			y = this.getY() - this.getSize() / 2;
			sy = this.getSize() / 10;
			break;
		case EAST:
			x = this.getX() - this.getSize() / 2;
			sx = this.getSize() / 10;
			y = this.getY() - this.getSize() / 4;
			sy = this.getSize() / 2;
			break;
		case SOUTH:
			x = this.getX() - this.getSize() / 4;
			sx = this.getSize() / 2;
			y = this.getY() + this.getSize() / 2 - this.getSize() / 10;
			sy = this.getSize() / 10;
			break;
		case WEST:
			x = this.getX() + this.getSize() / 2 - this.getSize() / 10;
			sx = this.getSize() / 10;
			y = this.getY() - this.getSize() / 4;
			sy = this.getSize() / 2;
			break;
		}
		Rectangle rect_door = new Rectangle(x, y, sx, sy);
		rect_door.setFill(Color.BLACK);
		this.addRectangle(rect_door);
		this.getLayer().getChildren().add(rect_door);
	}

	/**
	 * Vérifie si le soldat entre par la porte.
	 * 
	 * @param s un soldat
	 * @return True si il est entre en collision false sinon.
	 */
	public boolean isSoldierIn(Soldier s) {
		boolean in = false;
		switch (this.door) {
		case NORTH:
			if (s.getY() > this.getY() && s.getY() < this.getY() + this.getSize() / 10)
				if (s.getX() > this.getX() + this.getSize() / 4 && s.getX() < this.getX() + this.getSize() / 4 * 3)
					in = true;
			break;
		case EAST:
			if (s.getX() > this.getX() && s.getY() < this.getX() + this.getSize() / 10)
				if (s.getY() > this.getY() + this.getSize() / 4 && s.getY() < this.getY() + this.getSize() / 4 * 3)
					in = true;
			break;
		case SOUTH:
			if (s.getY() > this.getY() + this.getSize() / 10 * 9 && s.getY() < this.getY() + this.getSize())
				if (s.getX() > this.getX() + this.getSize() / 4 && s.getX() < this.getX() + this.getSize() / 4 * 3)
					in = true;
			break;
		case WEST:
			if (s.getX() > this.getX() + this.getSize() / 10 * 9 && s.getY() < this.getX() + this.getSize())
				if (s.getY() > this.getY() + this.getSize() / 4 && s.getY() < this.getY() + this.getSize() / 4 * 3)
					in = true;
			break;
		}
		return in;
	}

}
