package game;

import board.*;
// import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class ProductionTab {
    private ArrayList<Integer> time;
    private ArrayList<TypeSoldier> type;

    public ProductionTab() {
        this.time = new ArrayList<Integer>();
        this.type = new ArrayList<TypeSoldier>();
    }

    public void add(TypeSoldier t) {
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

    public void add(int time) {
        this.time.add(time);
        this.type.add(TypeSoldier.None);
    }


    public ArrayList<TypeSoldier> getProduction() {
        ArrayList<TypeSoldier> res = new ArrayList<TypeSoldier>();
        ArrayList<Integer> rm = new ArrayList<Integer>();
        for (int i=0; i < this.time.size(); i++) {
            int time = this.time.get(i);
            if (time == 0) {
                res.add(this.type.get(i));
                rm.add(i);
            }
            time--;
            this.time.set(i, time);
        }

        for (int i=0; i < rm.size(); i++) {
            int i_rm = rm.get(i);
            this.time.remove(i_rm);
            this.type.remove(i_rm);
        }

        return res;
    }

    public int size() {
        return this.time.size();
    }

    public TypeSoldier get(int index) {
        return this.type.get(index);
    }

    public void reset() {
        while (this.time.size() > 0) this.time.remove(0);
        while (this.type.size() > 0) this.type.remove(0);
    }

    public String toString() {
        String msg = "";
        for (int i=0; i < this.type.size(); i++) {
            msg += this.type.get(i);
            msg += " , " + this.time.get(i);
            msg += " | ";
        }
        return msg;
    }

}
