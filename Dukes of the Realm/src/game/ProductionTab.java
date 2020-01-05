package game;

import board.*;
// import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class ProductionTab {
    private ArrayList<Integer> time;
    private ArrayList<Production> type;

    public ProductionTab() {
        this.time = new ArrayList<Integer>();
        this.type = new ArrayList<Production>();
    }

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

    public void add(int time) {
        this.time.add(time);
        this.type.add(Production.Level);
    }


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

    public int size() {
        return this.time.size();
    }

    public Production get(int index) {
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
