package game;

public enum TypeSoldier {
    None ("None"),
    Piquier ("Piquier"),
    Chevalier ("Chevalier"),
    Onagre ("Onagre");

    private String name = "";
    TypeSoldier(String name) { this.name = name; }

    public String toString() { return this.name; }
}
