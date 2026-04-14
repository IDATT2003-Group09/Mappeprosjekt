package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
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
        Button saveFileButton = new Button(cleanName(fileName));
        saveFileButton.getStyleClass().add("start-button");
        saveFileButton.setFont(Font.font(fontFamily, BUTTON_FONT_SIZE));
        saveFileButton.setPrefWidth(450);
        saveFileButton.setPrefHeight(55);
        saveFileButton.setOnAction(e -> handler.onLoadSelected(fileName));
        UiSoundEffects.installHoverSound(saveFileButton);
        UiSoundEffects.installClickSound(saveFileButton);
        saveSlotsContainer.getChildren().add(saveFileButton);
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
}

