package game.util;
/**
 * enum util pour garder la traque de la production des chateaux, ainsi que le constructeur de Soldier().
 */
public enum Production {
    None ("None"),
    Piquier ("Piquier"),
    Chevalier ("Chevalier"),
    Onagre ("Onagre"),
    Level ("Level");

    private String name = "";
    Production(String name) { this.name = name; }

    public String toString() { return this.name; }
}
