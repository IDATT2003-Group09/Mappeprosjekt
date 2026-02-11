package edu.ntnu.iir.bidata.idatt2003.group09;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {

    VBox root = new VBox();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  static void main(String[] args) {
    launch(args);
  }
}
