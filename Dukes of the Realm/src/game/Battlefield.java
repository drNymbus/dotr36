package game;

import java.util.ArrayList;
import java.util.Random;

// import board.Input;
// import board.Settings;
import board.*;
import game.item.Castle;
import game.item.Soldier;
import game.item.Sprite;
import game.util.Direction;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class Battlefield {
	/** Largeur et Hauteur de la carte. */
	private int width, height;
	/** Liste des chateaux. */
	private ArrayList<Castle> castles;
	/** Liste des soldats présents sur la carte. */
	private ArrayList<Soldier> soldiers;
	/**
	 * Défini si le jeu est en pause ou non. True: le jeu est en pause. False: le
	 * jeu n'est pas en pause.
	 */
	public boolean pause = false;
	private Pane layer;
	private Input input;
	/** Défini si la partie est fini ou non. */
	public int gameover = Settings.NEUTRAL_ID;

	/**
	 * initialise la carte, fait apparaitre les chateaux pratiquement aléatoirement.
	 *
	 * @param nb_castles nombre de chateaux
	 * @param layer      la scène javafx
	 * @param in         la gestion des input
	 * @param w          la taille de la grille
	 * @param h          la hauteur de la grille
	 */

	public Battlefield(int nb_castles, Pane layer, Input in, int w, int h) {
		this.castles = new ArrayList<Castle>();
		this.soldiers = new ArrayList<Soldier>();

		this.width = w;
		this.height = h;

		this.input = in;
		this.layer=layer;

		Random rnd = new Random();
		Direction[] dirs = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

		int i = nb_castles; boolean base = true;
		while (i > 0 && i != 1) {
			if (base) {
				int x = rnd.nextInt(this.width - Settings.SIZE_CASTLE);
				int y = rnd.nextInt(this.height/2 - Settings.SIZE_CASTLE) + this.height/2;

				Castle c = new Castle(i, Settings.ALLY_ID, this.layer, x, y, dirs[rnd.nextInt(4)]);
				this.castles.add(c);
				c = new Castle(i-1, Settings.ENNEMY_ID, this.layer, (this.width - x), (this.height - y), dirs[rnd.nextInt(4)]);
				this.castles.add(c);

				base = false;
			} else {
				int x = this.width/2, y = this.height/2;
				boolean pos_ok = false;

				while (i > 0 && !pos_ok) {
					x = rnd.nextInt(this.width - Settings.SIZE_CASTLE);
					y = rnd.nextInt(this.height/2 - Settings.SIZE_CASTLE) + this.height/2;

					for (Castle c : this.castles)
						if (c.distance(x,y) > Settings.SIZE_CASTLE * 2) pos_ok = true;
				}

				Castle c = new Castle(i, Settings.NEUTRAL_ID, this.layer, x, y, dirs[rnd.nextInt(4)]);
				c.initNeutral();
				this.castles.add(c);
				c = new Castle(i-1, Settings.NEUTRAL_ID, this.layer, (this.width - x), (this.height - y), dirs[rnd.nextInt(4)]);
				c.initNeutral();
				this.castles.add(c);
			}

			i -= 2;
		}

		if (i == 1) {
			Castle c = new Castle(i, Settings.NEUTRAL_ID, this.layer, this.width/2, this.height/2, dirs[rnd.nextInt(4)]);
			c.initNeutral();
			this.castles.add(c);
		}
	}

	/**
	 * Retourne un chateau .
	 *
	 * @param id l'identité du chateau.
	 * @return le chateau , ou null si il n'a pas été trouvé.
	 */
	public Castle getCastle(int id) {
		for (int i = 0; i < this.castles.size(); i++) {
			Castle c = this.castles.get(i);
			if (c.getId() == id)
				return c;
		}
		return null;
	}

	/**
	 * retourne une liste de chateau.
	 *
	 * @param id l'identité du chateau
	 * @return la liste des chateaux
	 */
	public ArrayList<Castle> getCastles(int id) {
		ArrayList<Castle> res = new ArrayList<Castle>();
		for (int i=0; i < this.castles.size(); i++) {
			Castle c = this.castles.get(i);
			if (c.getId() != id) res.add(c);
		}
		return res;
	}
	public ArrayList<Castle> getCastles() { return this.getCastles(-1); }

	/**
	 * recherche un soldat spécifique
	 *
	 * @param id le numéro du soldat.
	 * @return le soldat si il est trouvé, sinon null
	 */
	public Soldier getSoldier(int id) {
		for (int i = 0; i < this.soldiers.size(); i++) {
			Soldier s = this.soldiers.get(i);
			if (s.getId() == id)
				return s;
		}
		return null;
	}

	/**
	 * renvoie la liste des soldats présents dans ce chateau.
	 *
	 * @return la liste des soldats présents dans ce chateau.
	 */
	public ArrayList<Soldier> getSoldiers() { return this.soldiers; }

	/**
	 * Met à jour la position des soldats, l'état de la partie ( gameover ou non ),
	 * raffraichi la production et le trésor des chateaux
	 */
	public void update() {
		int allycount=0;
		int enemycount=0;

		for (int i = 0; i < this.castles.size(); i++) {
			Castle c = this.castles.get(i);
			switch (c.getOwner()) {
				case Settings.ALLY_ID:
					allycount++;
					break;
				case Settings.ENNEMY_ID:
					enemycount++;
					break;
			}

			c.updateGold();
			c.updateProduction(this.layer);
		}

		if(allycount==0)
			gameover=Settings.ENNEMY_ID;
		if(enemycount==0)
			gameover=Settings.ALLY_ID;

		for (int i = 0; i < this.soldiers.size(); i++) {
			// System.out.println("NB_SOLDIERS:" + this.soldiers.size());
			Soldier s = this.soldiers.get(i);
			Castle c = this.getCastle(s.getTarget());
			// ArrayList<Sprite> avoid = new ArrayList<Sprite>(this.getCastles(s.getOwner()));
			s.update(c, this.getCastles());
			if (c.isSoldierIn(s)) {
				System.out.println("Soldier:" + s.getId() + " in Castle:" + c.getId());
				if(c.getOwner() == s.getOwner()) {
					c.addSoldier(s);
					this.soldiers.remove(i);
				} else {
					c.defend(this.soldiers, s);
				}
			}
		}
	}

	/**
	 * Gestion des entrées clavier ( pause, reprise du jeu et fermeture de la
	 * fenêtre ).
	 *
	 * @param input la classe input associé à la scène.
	 * @param now   la temps actuel associé à la scène.
	 */
	public void processInput(Input input, long now) {
		if (input.isExit()) {
			Platform.exit();
			System.exit(0);
		}
		else if(input.isPause())
			pause=true;
		else if(input.isResume())
			pause=false;

	}
}
