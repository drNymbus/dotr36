package game;

import java.util.ArrayList;
import java.util.Random;

import board.Settings;
import game.item.Castle;
import game.util.Production;

/**
 * Classe de gestion de l'ennemi.
 */
public class EnnemyIA {

	Battlefield bf;
	Random rand = new Random();

	public EnnemyIA(Battlefield b) {
		this.bf = b;
	}

	/**
	 * Choisi une action à réaliser, et quel chateau le réalise.
	 * Action: attaque, production d'une unité, levelup.
	 */
	public void update_Ennemy() {
		ArrayList<Castle> c = new ArrayList<Castle>();
		ArrayList<Castle> ce = new ArrayList<Castle>();

		for (Castle castle : bf.getCastles()) {
			if (castle.getOwner() == Settings.ENNEMY_ID) {
				c.add(castle);
			} else
				ce.add(castle);
		}
		int nb = rand.nextInt(c.size());
		int move = rand.nextInt(101);
		if (move < 25) {
			c.get(nb).addProd(Production.Piquier);
			System.out.println("piquier");
		} else if (move > 24 && move < 50) {
			c.get(nb).addProd(Production.Chevalier);
			System.out.println("chevalier");
		} else if (move > 49 && move < 75) {
			c.get(nb).addProd(Production.Onagre);
			System.out.println("onagre");
		} else if (move > 74 && move < 90) {
			int tmp = rand.nextInt(ce.size());
			c.get(nb).setTarget(ce.get(tmp).getId());
			c.get(nb).attack(ce.get(tmp).getId(), bf.getSoldiers(), rand.nextInt(1 + c.get(nb).getNbSoldiers()));
			System.out.println("attack");
		} else if (move > 89) {
			int tmp = rand.nextInt(c.size());
			c.get(tmp).levelup();
			System.out.println("levelup");
		}
	}
}
