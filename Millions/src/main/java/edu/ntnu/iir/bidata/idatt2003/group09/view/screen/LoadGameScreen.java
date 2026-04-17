package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LoadGameScreen extends VBox {

  private static final String FONT_PATH = "/ThaleahFat.ttf";
  private static final double TITLE_FONT_SIZE = 32;
  private static final double BUTTON_FONT_SIZE = 26;

  public interface LoadGameHandler {
    void onLoadSelected(String fileName);
    void onBack();
  }

  public LoadGameScreen(List<String> saveFiles, LoadGameHandler handler) {
    getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());

    setSpacing(18);
    setAlignment(Pos.CENTER);
    setPadding(new Insets(40));
    setStyle("""
    -fx-background-image: url('/images/Millions_background.png');
    -fx-background-size: cover;
    -fx-background-position: center;
""");

    String fontFamily = loadFontFamily();

    VBox saveSlotsContainer = new VBox(10);
    saveSlotsContainer.setAlignment(Pos.CENTER);
    saveSlotsContainer.setPadding(new Insets(20));

    if (saveFiles == null || saveFiles.isEmpty()) {
      Label emptyLabel = new Label("No saved games found.");
      emptyLabel.setStyle("-fx-text-fill: white;");
      emptyLabel.setFont(Font.font(fontFamily, 22));
      saveSlotsContainer.getChildren().add(emptyLabel);
    } else {
      for (String fileName : saveFiles) {
        Button saveFileButton = createSaveFileButton(fileName, handler);
        UiSoundEffects.installClickSound(saveFileButton);
        UiSoundEffects.installHoverSound(saveFileButton);
        saveSlotsContainer.getChildren().add(saveFileButton);
        saveFileButton.getStyleClass().add("start-button");
        saveFileButton.setOnAction(e -> handler.onLoadSelected(fileName));
      }
    }

    Button backButton = new Button("Back");
    backButton.getStyleClass().add("start-button");
    backButton.setFont(Font.font(fontFamily, BUTTON_FONT_SIZE));
    backButton.setPrefWidth(450);
    backButton.setPrefHeight(55);
    backButton.setOnAction(e -> handler.onBack());
    UiSoundEffects.installHoverSound(backButton);
    UiSoundEffects.installClickSound(backButton);

    getChildren().addAll(saveSlotsContainer, backButton);
  }

  private String loadFontFamily() {
    try (InputStream fontStream = getClass().getResourceAsStream(FONT_PATH)) {
      if (fontStream == null) {
        return Font.getDefault().getFamily();
      }

      Font loadedFont = Font.loadFont(fontStream, TITLE_FONT_SIZE);
      if (loadedFont != null) {
        return loadedFont.getFamily();
      }
    } catch (IOException e) {
      return Font.getDefault().getFamily();
    }

    return Font.getDefault().getFamily();
  }

  public static String cleanName(String filename) {
      if (filename == null || filename.isEmpty()) {
          return "";
      }

      // Find the last dot for extension
      int lastDotIndex = filename.lastIndexOf('.');
      String nameWithoutExtension = (lastDotIndex != -1) ? 
          filename.substring(0, lastDotIndex) : filename;

      // Find the first dash after "savegame"
      int dashIndex = nameWithoutExtension.indexOf('-');
      if (dashIndex != -1) {
          return nameWithoutExtension.substring(dashIndex + 1);
      }

      return "";
  }

  private Button createSaveFileButton(String fileName, LoadGameHandler handler) {
    String displayName = cleanName(fileName);
    String netWorth = "";
    String weekInfo = "";
    String difficulty = "";

    Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(e -> {
        SaveManager.deleteSaveFile(fileName);
        handler.onLoadSelected(null); // Refresh the screen
    });
    deleteButton.getStyleClass().add("delete-button");
    UiSoundEffects.installHoverSound(deleteButton);
    UiSoundEffects.installClickSound(deleteButton);
    
    try {
        var state = SaveManager.load(fileName);
        if (state != null) {
            netWorth = String.format("$%.2f", state.getNetWorth());
            weekInfo = String.format("Week: %d", state.getWeek());
            difficulty = state.getDifficulty();
        }
    } catch (Exception e) {
        weekInfo = "Could not read save";
    }
    
    Button saveFileButton = new Button();

    VBox buttonContent = new VBox(8);
    buttonContent.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

    Label nameLabel = new Label(displayName + " (" + difficulty + ")");
    Label valueLabel = new Label(netWorth);
    Label weekLabel = new Label(weekInfo);

    nameLabel.setStyle("-fx-text-fill: white;");
    valueLabel.setStyle("-fx-text-fill: green;");
    weekLabel.setStyle("-fx-text-fill: white;");

    HBox titleBox = new HBox();
    titleBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(nameLabel, Priority.ALWAYS);
    nameLabel.setMaxWidth(Double.MAX_VALUE);
    weekLabel.setAlignment(Pos.CENTER_RIGHT);
    titleBox.getChildren().addAll(nameLabel, weekLabel);

    HBox bottomBox = new HBox();
    bottomBox.setAlignment(Pos.CENTER_LEFT);
    HBox.setHgrow(valueLabel, Priority.ALWAYS);
    valueLabel.setMaxWidth(Double.MAX_VALUE);
    deleteButton.setAlignment(Pos.CENTER_RIGHT);
    bottomBox.getChildren().addAll(valueLabel, deleteButton);


    buttonContent.getChildren().addAll(titleBox, bottomBox);

    saveFileButton.setGraphic(buttonContent);

    saveFileButton.setMinWidth(600);

    return saveFileButton;
  }

}

