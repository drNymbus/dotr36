package game;

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
