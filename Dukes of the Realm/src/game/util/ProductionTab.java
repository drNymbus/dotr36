package game.util;

import game.util.Production;


// import javafx.scene.shape.Rectangle;
import java.util.ArrayList;


/**
 * Classe de gestion de production.
 */
public class ProductionTab   {
	/** Liste du temps des productions. */
	private ArrayList<Integer> time;
	/** Liste des types de production. */
	private ArrayList<Production> type;

	public ProductionTab() {
		this.time = new ArrayList<Integer>();
		this.type = new ArrayList<Production>();
	}

	/**
	 * Ajoute la prodution t à la liste des types. Ajoute son temps correspondant à
	 * la liste des temps.
	 * 
	 * @param t Production à ajouter.
	 */
	public void add(Production t) {
		switch (t) {
		case Piquier:
			this.time.add(5);
			break;
		case Chevalier:
			this.time.add(20);
			break;
		case Onagre:
			this.time.add(50);
			break;
		}
		this.type.add(t);
	}

	/**
	 * Ajoute la production du levelup à la liste des types. ajoute le temps time à
	 * la liste des temps.
	 * 
	 * @param time Temps de la production.
	 */
	public void add(int time) {
		this.time.add(time);
		this.type.add(Production.Level);
	}

	/**
	 * Retourne la production actuelle.
	 * 
	 * @return Production en cours.
	 */
	public Production getProduction() {
		Production res = Production.None;
		int time = this.time.get(0);
		if (time == 0) {
			res = this.type.get(0);
			this.time.remove(0);
			this.type.remove(0);
		} else {
			time--;
			this.time.set(0, time);
		}
		return res;
	}

	/**
	 * Retourne le la taille de la liste time.
	 * 
	 * @return Taille de liste time.
	 */
	public int size() {
		return this.time.size();
	}

	/**
	 * Retourne la production correspondant à l'élément index.
	 * 
	 * @param index Position de la production recherchée.
	 * @return l'élément à la position index.
	 */
	public Production get(int index) {
		if (type.size() == 0)
			return null;
		return this.type.get(index);
	}

	/**
	 * Retourne le temps correspondant à l'élément index.
	 * 
	 * @param index Position du temps recherché.
	 * @return Temps restant.
	 */
	public int getProdRemaining(int index) {
		if (time.size() == 0)
			return 0;
		return this.time.get(index);

	}

	public void remove() {
		if (time.size() == 0)
			return;
		this.time.remove(0);
		this.type.remove(0);

	}

	/**
	 * Vide les listes time et type.
	 */
	public void reset() {
		this.time.clear();
		this.type.clear();
	}

	/**
	 * Retourne les listes type et time sous forme de string. "type,time |".
	 * 
	 * @return Message correspondant.
	 */
	public String toString() {
		String msg = "";
		for (int i = 0; i < this.type.size(); i++) {
			msg += this.type.get(i);
			msg += " , " + this.time.get(i);
			msg += " | ";
		}
		return msg;
	}



}
