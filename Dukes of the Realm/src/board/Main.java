package board;

import game.Battlefield;
import game.Castle;
import game.TypeSoldier;
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
import javafx.scene.input.MouseButton;
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
	private Castle currentcastle = null;
	private ArrayList<HBox> text = new ArrayList<>();
	private boolean attack = false;
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
				if (!attack) {
					if (now % 5 == 0) { // slowing down the turn
						if (!bf.pause) {
							bf.processInput(input, now);
							bf.update();
							// update_display();
							update_CastlState();
							if (currentcastle != null)
								System.out.println(currentcastle.getId());
						} else
							bf.processInput(input, now);
					}
				}
			}
		};
		gameLoop.start();
	};

	public void displayInfo() {

		HBox infoOwner = new HBox();
		Text textOwner = new Text("Propriétaire:");
		infoOwner.getChildren().add(textOwner);
		text.add(infoOwner);
		infoOwner.relocate(670, 50);

		HBox infoTarget = new HBox();
		Text textTarget = new Text("Target:");
		infoTarget.getChildren().add(textTarget);
		text.add(infoTarget);
		infoTarget.relocate(670, 100);

		HBox infoLevel = new HBox();
		Text textLevel = new Text("Niveau du chateau:");
		infoLevel.getChildren().add(textLevel);
		infoLevel.relocate(670, 150);
		text.add(infoLevel);

		HBox infoTresor = new HBox();
		Text textTresor = new Text("Trésor:");
		infoTresor.getChildren().add(textTresor);
		infoTresor.relocate(670, 200);
		text.add(infoTresor);

		HBox infoSoldat = new HBox();
		Text textSoldat = new Text("Soldats:");
		infoSoldat.getChildren().add(textSoldat);
		infoSoldat.relocate(670, 250);
		text.add(infoSoldat);
		root.getChildren().addAll(text);

	}

	public void update_Display() {
		if (currentcastle == null)
			return;
		else {
			switch (currentcastle.getOwner()) {
			case -1:
				((Text) text.get(0).getChildren().get(0))
						.setText("Propriétaire:\n" + "Ennemy(" + currentcastle.getId() + ")");
				break;
			case 1:
				((Text) text.get(0).getChildren().get(0))
						.setText("Propriétaire:\n" + "Ally(" + currentcastle.getId() + ")");
				break;
			default:
				((Text) text.get(0).getChildren().get(0))
						.setText("Propriétaire:\n" + "Neutral(" + currentcastle.getId() + ")");
				break;
			}
			((Text) text.get(1).getChildren().get(0)).setText("Target:\n" + currentcastle.getTarget());
			((Text) text.get(2).getChildren().get(0)).setText("Niveau du chateau:\n" + currentcastle.getLevel());
			((Text) text.get(3).getChildren().get(0)).setText("Trésor:\n" + currentcastle.getTresor());
			((Text) text.get(4).getChildren().get(0)).setText("Soldats:\n" + currentcastle.getSoldiers());

		}

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
					MenuItem levelUp = new MenuItem("level up");
					produce_piquier.setOnAction(evt -> a.addProd(TypeSoldier.Piquier));
					produce_chevalier.setOnAction(evt -> a.addProd(TypeSoldier.Chevalier));
					produce_onagre.setOnAction(evt -> a.addProd(TypeSoldier.Onagre));
					levelUp.setOnAction(evt -> a.levelup());
					contextMenu.getItems().addAll(produce_piquier, produce_chevalier, produce_onagre, levelUp);
					contextMenu.show(a.getShape(), e.getScreenX(), e.getScreenY());
				});
			} else if (currentcastle != null && currentcastle.getOwner() == 1) {
				a.getShape().setOnContextMenuRequested(e -> {
					ContextMenu contextMenu = new ContextMenu();
					MenuItem setAttack = new MenuItem("attack");
					setAttack.setOnAction(evt -> currentcastle.setTarget(a.getId()));
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
		bf = new Battlefield(Settings.NB_CASTLES, playfieldLayer, input, 650, 800);
		// ally castle will always be 0 when loading
		playfieldLayer.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.MIDDLE) {
				((Text) text.get(0).getChildren().get(0)).setText("Propriétaire:\n");
				((Text) text.get(1).getChildren().get(0)).setText("Target:\n");
				((Text) text.get(2).getChildren().get(0)).setText("Niveau du chateau:\n");
				((Text) text.get(3).getChildren().get(0)).setText("Trésor:\n");
				((Text) text.get(4).getChildren().get(0)).setText("Soldats:\n");
				currentcastle = null;
				e.consume();
			}
		});
		for (int i = 0; i < Settings.NB_CASTLES; i++) {
			Castle a = bf.getCastles().get(i);
			a.getShape().setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.PRIMARY) {

					currentcastle = a;
					switch (a.getOwner()) {
					case -1:
						((Text) text.get(0).getChildren().get(0))
								.setText("Propriétaire:\n" + "Ennemy(" + a.getId() + ")");
						break;
					case 1:
						((Text) text.get(0).getChildren().get(0))
								.setText("Propriétaire:\n" + "Ally(" + a.getId() + ")");
						break;
					default:
						((Text) text.get(0).getChildren().get(0))
								.setText("Propriétaire:\n" + "Neutral(" + a.getId() + ")");
						break;
					}
					((Text) text.get(1).getChildren().get(0)).setText("Target:\n" + a.getTarget());
					((Text) text.get(2).getChildren().get(0)).setText("Niveau du chateau:\n" + a.getLevel());
					((Text) text.get(3).getChildren().get(0)).setText("Trésor:\n" + a.getTresor());
					((Text) text.get(4).getChildren().get(0)).setText("Soldats:\n" + a.getSoldiers());
					e.consume();
				}
			});
			if (a.getOwner() == 1) {
				a.getShape().setOnContextMenuRequested(e -> {
					ContextMenu contextMenu = new ContextMenu();
					MenuItem produce_piquier = new MenuItem("Produce (Piquier)");
					MenuItem produce_chevalier = new MenuItem("Produce (Chevalier)");
					MenuItem produce_onagre = new MenuItem("Produce (Onagre)");
					MenuItem levelUp = new MenuItem("level up");
					produce_piquier.setOnAction(evt -> a.addProd(TypeSoldier.Piquier));
					produce_chevalier.setOnAction(evt -> a.addProd(TypeSoldier.Chevalier));
					produce_onagre.setOnAction(evt -> a.addProd(TypeSoldier.Onagre));
					levelUp.setOnAction(evt -> a.levelup());
					contextMenu.getItems().addAll(produce_piquier, produce_chevalier, produce_onagre, levelUp);
					contextMenu.show(a.getShape(), e.getScreenX(), e.getScreenY());
				});
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
