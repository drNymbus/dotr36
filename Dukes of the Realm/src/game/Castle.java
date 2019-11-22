package game;

import javafx.scene.shape.Rectangle;

public class Castle {
	private Rectangle r;
	private int posX;
	private int posY;
	public Castle(int X,int Y) {
		this.r=new Rectangle(20, 20);
		posX=X;
		posY=Y;
	}
}