package board;

import game.Battlefield;
import game.Castle;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.shape.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Main extends Application {
	private Random rnd = new Random();
	private Text msg = new Text();

	private Pane playfieldLayer;
	private Scene scene;
	private AnimationTimer gameLoop;
	private Battlefield bf;
	private Input input;
	private ArrayList<HBox> text = new ArrayList<>(); 
	Group root;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Group();
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
		scene.setFill(Color.DARKGOLDENROD);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		playfieldLayer = new Pane();
		root.getChildren().add(playfieldLayer);
		loadGame();
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				bf.processInput(input, now);
			
			}
		};
		gameLoop.start();
	};

	
	public void displayInfo() {
		
		HBox infoOwner = new HBox();
		Text textOwner= new Text("Propriétaire:");
		infoOwner.getChildren().add(textOwner);
		text.add(infoOwner);
		infoOwner.relocate(50, 750);	
		
		HBox infoLevel = new HBox();
		Text textLevel= new Text("Niveau du chateau:");
		infoLevel.getChildren().add(textLevel);
		infoLevel.relocate(250, 750);	
		text.add(infoLevel);
		
		HBox infoTresor = new HBox();
		Text textTresor= new Text("Trésor:");
		infoTresor.getChildren().add(textTresor);
		infoTresor.relocate(450, 750);	
		text.add(infoTresor);
		
		HBox infoSoldat = new HBox();
		Text textSoldat= new Text("Soldat:");
		infoSoldat.getChildren().add(textSoldat);
		infoSoldat.relocate(650, 750);	
		text.add(infoSoldat);
		root.getChildren().addAll(text);
	
	}
	public void loadGame() {
		playfieldLayer.setPrefSize(700, 700);
		playfieldLayer.setLayoutX(50);
		playfieldLayer.setLayoutY(50);
		playfieldLayer.setStyle("-fx-background-color: PapayaWhip ;");
		input = new Input(scene);
		input.addListeners();
		displayInfo();
		// creating battlefield
		bf = new Battlefield(Settings.NB_CASTLES, playfieldLayer, input, 700, 700);
		// ally castle will always be 0 when loading
		Castle a = bf.getCastles().get(0);
		a.getShape().setOnMousePressed(e -> {
			((Text) text.get(0).getChildren().get(0)).setText("Propriétaire:\n"+a.getOwner());
			((Text) text.get(3).getChildren().get(0)).setText("Soldat:\n"+a.getSoldiers());
			
			e.consume();

		});
		a.getShape().setOnContextMenuRequested(e -> {
			ContextMenu contextMenu = new ContextMenu();
			MenuItem produce = new MenuItem("Produce");
			MenuItem attack = new MenuItem("Attack");
			// produce.setOnAction(evt -> System.out.println(ally.getid()));
			contextMenu.getItems().addAll(produce, attack);
			contextMenu.show(a.getShape(), e.getScreenX(), e.getScreenY());
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
