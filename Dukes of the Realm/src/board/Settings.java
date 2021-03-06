package board;

import javafx.scene.paint.Color;
/**
 * Classe de paramètre du jeu.
 */
public class Settings {

	public static final double SCENE_WIDTH = 800;
    public static final double SCENE_HEIGHT = 800;

	public static final int GAME_WIDTH = 650;
	public static final int GAME_HEIGHT = 800;

	public static final int NB_CASTLES = 5;

	public static final int SIZE_CASTLE= 40;
	public static final int SIZE_SOLDIER= 10;

	public static final int NEUTRAL_ID = 0;
	public static final int ALLY_ID = 1;
	public static final int ENNEMY_ID = -1;

	public static final Color NEUTRAL_COLOR = Color.GREY;
	public static final Color ALLY_COLOR = Color.BLUE;
	public static final Color ENNEMY_COLOR = Color.RED;

}
