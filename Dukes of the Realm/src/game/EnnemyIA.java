package game;

import java.util.ArrayList;
import java.util.Random;

import board.Settings;

public class EnnemyIA {

	Battlefield bf;
	Random rand = new Random();
	public EnnemyIA(Battlefield b) {
		this.bf = b;
	}

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
		if (move < 30) {
			c.get(nb).addProd(TypeSoldier.Piquier);
			System.out.println("piquier");
		} else if (move > 29 && move < 60) {
			c.get(nb).addProd(TypeSoldier.Chevalier);
			System.out.println("chevalier");
		} else if (move > 59 && move < 90) {
			c.get(nb).addProd(TypeSoldier.Onagre);
			System.out.println("onagre");
		} else if (move > 89) {
			c.get(nb).setTarget(ce.get(rand.nextInt(ce.size())).getId());
			System.out.println("attack");
		}
	}
}