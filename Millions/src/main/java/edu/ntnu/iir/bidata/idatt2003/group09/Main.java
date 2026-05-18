package edu.ntnu.iir.bidata.idatt2003.group09;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.NavigationController;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main entry point for the Millions stock trading game application.
 */
public class Main extends Application {

    private static final double DESIGN_WIDTH = 1100;
    private static final double DESIGN_HEIGHT = 700;

    private StackPane root;
    private BorderPane contentRoot;
    private TutorialOverlay tutorialOverlay;
    private NavigationController navigationController;

    /**
     * Starts the JavaFX application. Initializes the exchange, player, and trade screen, and sets up the main stage.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Millions - A Stock Trading Game");

        UiSoundEffects.startBackgroundMusic();
        initializeRootLayout();
        navigationController = new NavigationController(contentRoot, tutorialOverlay);
        navigationController.showStartScreen();

        Scene scene = createScaledScene();
        configureStage(primaryStage, scene);
    }

    private void initializeRootLayout() {
        contentRoot = new BorderPane();
        contentRoot.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentRoot.setMinSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentRoot.setMaxSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        tutorialOverlay = new TutorialOverlay();

        root = new StackPane(contentRoot, tutorialOverlay.getLayer());
        root.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        root.setMinSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        root.setMaxSize(DESIGN_WIDTH, DESIGN_HEIGHT);
    }

    private Scene createScaledScene() {
        StackPane viewport = new StackPane(root);
        viewport.setStyle("-fx-background-color: #202020;");

        Scene scene = new Scene(viewport, DESIGN_WIDTH, DESIGN_HEIGHT);

        root.scaleXProperty().bind(
            Bindings.createDoubleBinding(
                () -> Math.min(scene.getWidth() / DESIGN_WIDTH, scene.getHeight() / DESIGN_HEIGHT),
                scene.widthProperty(),
                scene.heightProperty()
            )
        );
        root.scaleYProperty().bind(root.scaleXProperty());
        return scene;
    }

    private void configureStage(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.getIcons().add(new Image(
            getClass().getResourceAsStream("/images/boss/boss-frames/pixil-frame-0.png")
        ));
        primaryStage.show();
    }

    @Override
    public void stop() {
        UiSoundEffects.stopBackgroundMusic();
    }

    /**
     * write mvn javafx:run to run this method that starts the application
     *
     * @param args
     */
    public static void main(String[] args) {
        
        launch(args);
    }
}