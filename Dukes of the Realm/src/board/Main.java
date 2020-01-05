package board;

import game.Battlefield;
import game.item.Castle;
import game.EnnemyIA;
import game.util.Production;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu ;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Main extends Application {
	private Random rnd = new Random();
	private Text msg = new Text();

	private Pane playfieldLayer;
	private Scene scene;
	private AnimationTimer gameLoop;
	private Battlefield bf;
	private EnnemyIA ennemy;
	private Input input;
	private Castle currentcastle = null;
	private ArrayList<Text> text = new ArrayList<>();
	private VBox tmp;
	private boolean attack = false;
	private int ennemymove; //check when the AI plays
	private HBox endMsg;
	Group root;

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Group();
		scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
		scene.setFill(Color.DARKGOLDENROD);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setTitle("Dukes Of The realm");
		playfieldLayer = new Pane();
		root.getChildren().add(playfieldLayer);
		loadGame();
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (bf.gameover == 0) {
					if (now % 5 == 0) { // slowing down the turn
						update_CastlState();
						if (!bf.pause) {
							bf.processInput(input, now);
							bf.update();
							update_Display();
							if (ennemymove == 50) {
								ennemy.update_Ennemy();
								ennemymove = 0;
							}
							ennemymove++;
						} else
							bf.processInput(input, now);
					}
				} else {
					createEndMessage(bf.gameover);
				}
			}
		};
		gameLoop.start();
	};

	public void displayInfo() {
		tmp = new VBox();
		tmp.setSpacing(10);
		Text textOwner = new Text("Propriétaire:\n");
		text.add(textOwner);
		Text textTarget = new Text("Target:\n");
		text.add(textTarget);
		Text textLevel = new Text("Niveau du chateau\n");
		text.add(textLevel);
		Text textTresor = new Text("Trésor:\n");
		text.add(textTresor);
		Text textSoldat = new Text("Soldats:\n");
		text.add(textSoldat);
		tmp.getChildren().addAll(textOwner, textTarget, textLevel, textTresor, textSoldat);
		tmp.relocate(670, 50);
		root.getChildren().add(tmp);
	}

	public void update_Display() {
		if (currentcastle == null)
			return;
		else
			display_switch(currentcastle.getOwner());
	}

	public void update_CastlState() {
		for (int i = 0; i < Settings.NB_CASTLES; i++) {
			Castle a = bf.getCastles().get(i);
			if (a.getOwner() == 1) {
				a.getShape().setOnContextMenuRequested(e -> {
					ContextMenu contextMenu = new ContextMenu();
					MenuItem produce_piquier = new MenuItem("Produce (Piquier)");
					MenuItem produce_chevalier = new MenuItem("Produce (Chevalier)");
					MenuItem produce_onagre = new MenuItem("Produce (Onagre)");
					MenuItem levelUp = new MenuItem("LVL UP(" + (a.getLevel() + 1) + "," + (1000*a.getLevel()) + "G)");
					MenuItem stopAttack = new MenuItem("Stop Attack");
					produce_piquier.setOnAction(evt -> a.addProd(Production.Piquier));
					produce_chevalier.setOnAction(evt -> a.addProd(Production.Chevalier));
					produce_onagre.setOnAction(evt -> a.addProd(Production.Onagre));
					levelUp.setOnAction(evt -> a.levelup());
					stopAttack.setOnAction(evt -> a.stopAttack());
					contextMenu.getItems().addAll(produce_piquier, produce_chevalier, produce_onagre, levelUp, stopAttack);
					contextMenu.show(a.getShape(), e.getScreenX(), e.getScreenY());
				});
			} else if (currentcastle != null && currentcastle.getOwner() == 1) {

				a.getShape().setOnContextMenuRequested(e -> {
					ContextMenu contextMenu = new ContextMenu();
					Menu setAttack = new Menu("Attack");
					MenuItem atk1 = new MenuItem("1");
					MenuItem atk2 = new MenuItem("2");
					MenuItem atk3 = new MenuItem("3");
					MenuItem atk4 = new MenuItem("4");
					MenuItem atk5 = new MenuItem("5");
					atk1.setOnAction(evt -> currentcastle.attack(a.getId(), bf.getSoldiers(), 1));
					atk2.setOnAction(evt -> currentcastle.attack(a.getId(), bf.getSoldiers(), 2));
					atk3.setOnAction(evt -> currentcastle.attack(a.getId(), bf.getSoldiers(), 3));
					atk4.setOnAction(evt -> currentcastle.attack(a.getId(), bf.getSoldiers(), 4));
					atk5.setOnAction(evt -> currentcastle.attack(a.getId(), bf.getSoldiers(), 5));
					setAttack.getItems().addAll(atk1, atk2, atk3, atk4, atk5);
					contextMenu.getItems().addAll(setAttack);
					contextMenu.show(a.getShape(), e.getScreenX(), e.getScreenY());
				});
			}

		}

	}

	public void loadGame() {
		playfieldLayer.setPrefSize(650, 800);
		playfieldLayer.setLayoutX(0);
		playfieldLayer.setLayoutY(0);
		playfieldLayer.setStyle("-fx-background-color: PapayaWhip ;");
		input = new Input(scene);
		input.addListeners();
		displayInfo();
		// creating battlefield
		bf = new Battlefield(Settings.NB_CASTLES, playfieldLayer, input, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
		ennemy = new EnnemyIA(bf);
		ennemymove = 0;

		// middle click deselecting the current castle
		playfieldLayer.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.MIDDLE) {
				text.get(0).setText("Propriétaire:\n");
				text.get(1).setText("Target:\n");
				text.get(2).setText("Niveau du chateau:\n");
				text.get(3).setText("Trésor:\n");
				text.get(4).setText("Soldats:\n");
				currentcastle = null;
				e.consume();
			}
		});

		// right click selecting a castle and getting information
		for (int i = 0; i < Settings.NB_CASTLES; i++) {
			Castle a = bf.getCastles().get(i);
			a.getShape().setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.PRIMARY) {

					currentcastle = a;
					display_switch(a.getOwner());
					e.consume();
				}
			});
			// left click to make order
			if (a.getOwner() == 1) {
				a.getShape().setOnContextMenuRequested(e -> {
					ContextMenu contextMenu = new ContextMenu();
					MenuItem produce_piquier = new MenuItem("Produce (Piquier)");
					MenuItem produce_chevalier = new MenuItem("Produce (Chevalier)");
					MenuItem produce_onagre = new MenuItem("Produce (Onagre)");
					MenuItem levelUp = new MenuItem("LVL UP(" + (a.getLevel() + 1) + "," + (1000*a.getLevel()) + "G)");
					MenuItem stopAttack = new MenuItem("Stop Attack");
					produce_piquier.setOnAction(evt -> a.addProd(Production.Piquier));
					produce_chevalier.setOnAction(evt -> a.addProd(Production.Chevalier));
					produce_onagre.setOnAction(evt -> a.addProd(Production.Onagre));
					levelUp.setOnAction(evt -> a.levelup());
					stopAttack.setOnAction(evt -> a.stopAttack());
					contextMenu.getItems().addAll(produce_piquier, produce_chevalier, produce_onagre, levelUp, stopAttack);
					contextMenu.show(a.getShape(), e.getScreenX(), e.getScreenY());
				});
			}
		}
	}

	//
	public void display_switch(int i) {
		switch (i) {
		case -1:
			text.get(0).setText("Propriétaire:\n" + "Ennemy(" + currentcastle.getId() + ")");
			break;
		case 1:
			text.get(0).setText("Propriétaire:\n" + "Ally(" + currentcastle.getId() + ")");
			break;
		default:
			text.get(0).setText("Propriétaire:\n" + "Neutral(" + currentcastle.getId() + ")");
			break;
		}
		text.get(1).setText("Target:\n" + currentcastle.getTarget());
		text.get(2).setText("Niveau du chateau:\n" + currentcastle.getLevel());
		text.get(3).setText("Trésor:\n" + currentcastle.getTresor());
		text.get(4).setText("Soldats:\n" + currentcastle.getSoldiers());
	}

	public void createEndMessage(int winner) {
		if (winner == Settings.ALLY_ID) {
			endMsg = new HBox();
			Text msgWin = new Text("Victoire !");
			msgWin.setStyle("-fx-font-size:100px;");
			endMsg.getChildren().add(msgWin);
			endMsg.relocate(Settings.SCENE_WIDTH / 2, Settings.SCENE_HEIGHT / 2);
			endMsg.relocate((Settings.SCENE_WIDTH / 2) - 200, Settings.SCENE_HEIGHT / 2);
			root.getChildren().add(endMsg);
		} else {
			endMsg = new HBox();
			Text msgWin = new Text("Défaite");
			msgWin.setStyle("-fx-font-size:100px;");
			endMsg.getChildren().add(msgWin);
			endMsg.relocate((Settings.SCENE_WIDTH / 2) - 200, Settings.SCENE_HEIGHT / 2);
			root.getChildren().add(endMsg);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
