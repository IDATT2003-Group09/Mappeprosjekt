package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class StartScreen {

  public StartScreen() {

  private VBox newLoadSettingsBox(){
    Button newGameButton = new Button("New Game");
    Button loadGameButton = new Button("Load Game");
    Button settingsButton = new Button("Settings");
    Button exitButton = new Button("Exit");
    VBox box = new VBox(10, newGameButton, loadGameButton, settingsButton, exitButton);
    box.setAlignment(Pos.CENTER);
    return box;
  }

  private VBox loadGame() {
    Button save1Button = new Button("Save 1");
    Button save2Button = new Button("Save 2");
    Button save3Button = new Button("Save 3");
    Button backButton = new Button("Back");
    VBox box = new VBox(10, save1Button, save2Button, save3Button, backButton);
    box.setAlignment(Pos.CENTER);
    return box;
  }
}
