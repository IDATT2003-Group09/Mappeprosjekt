package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import java.io.IOException;
import java.io.InputStream;

import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.Boss;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Skjerm for opprettelse av nytt spill. Håndterer inntasting av spilleravn, startkapital
 * og valg av exchange (sp500, random eller en egendefinert CSV-fil).
 */
public class CreateGameScreen extends StackPane {
	private Boss boss;

	private static final String FONT_PATH = "/ThaleahFat.ttf";
	private static final String EXIT_RED_PATH = "/images/util/exit/pixilart-frames/exitred.png";
	private static final String EXIT_GREEN_PATH = "/images/util/exit/pixilart-frames/exitgreen.png";
	private static final double BOSS_SIZE = 500;
	private static final double TITLE_FONT_SIZE = 32;
	private static final double BUTTON_FONT_SIZE = 26;
	private static final double OPTION_BUTTON_FONT_SIZE = 24;
	private static final double EXIT_BUTTON_SIZE = 50;

	public interface CreateGameHandler {
		void onCreateGame(String playerName, String experienceLevel, String exchangeChoice, String startingMoney);
		boolean doesSaveFileExist(String playerName);
		void onBack();
	}

	public CreateGameScreen(CreateGameHandler handler) {
		this(handler, null);
	}

	public CreateGameScreen(CreateGameHandler handler, String bossMessage) {
		getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());

		String imageUrl = getClass().getResource("/images/office.png").toExternalForm();
		setStyle("""
			-fx-background-image: url('%s');
			-fx-background-size: cover;
			-fx-background-position: center;
		""".formatted(imageUrl));

		String fontFamily = loadFontFamily();

		VBox contentBox = new VBox(16);
		contentBox.setAlignment(Pos.CENTER);
		contentBox.setFillWidth(false);

		TextField fileNameField = new TextField();
		fileNameField.setPromptText("");
		fileNameField.setMaxWidth(250);
		fileNameField.setPrefHeight(38);
		fileNameField.setFont(Font.font(fontFamily, 22));
		fileNameField.setAlignment(Pos.CENTER);
		fileNameField.setStyle("""
			-fx-background-color: transparent;
			-fx-background-insets: 0;
			-fx-background-radius: 0;
			-fx-border-color: transparent;
			-fx-border-width: 0;
			-fx-text-fill: #111111;
			-fx-highlight-fill: #ffd447;
			-fx-highlight-text-fill: #111111;
		""");

		TextField moneyField = new TextField();
		moneyField.setPromptText("Starting money (e.g. 100000)");
		moneyField.setMaxWidth(250);
		moneyField.setPrefHeight(38);
		moneyField.setFont(Font.font(fontFamily, 22));
		moneyField.setAlignment(Pos.CENTER);
		moneyField.setStyle("""
			-fx-background-color: transparent;
			-fx-background-insets: 0;
			-fx-background-radius: 0;
			-fx-border-color: transparent;
			-fx-border-width: 0;
			-fx-text-fill: #111111;
			-fx-highlight-fill: #ffd447;
			-fx-highlight-text-fill: #111111;
		""");

		StackPane inputBubble = new StackPane(fileNameField);
		inputBubble.setPadding(new Insets(12, 16, 12, 16));
		inputBubble.setMinWidth(300);
		inputBubble.setMaxWidth(380);
		inputBubble.setStyle("""
			-fx-background-color: #f5f5f5;
			-fx-border-color: #111111;
			-fx-border-width: 3px;
			-fx-background-radius: 0;
			-fx-border-radius: 0;
		""");
		inputBubble.setVisible(false);
		inputBubble.setManaged(false);

		StackPane moneyBubble = new StackPane(moneyField);
		moneyBubble.setPadding(new Insets(12, 16, 12, 16));
		moneyBubble.setMinWidth(300);
		moneyBubble.setMaxWidth(380);
		moneyBubble.setStyle("""
			-fx-background-color: #f5f5f5;
			-fx-border-color: #111111;
			-fx-border-width: 3px;
			-fx-background-radius: 0;
			-fx-border-radius: 0;
		""");
		moneyBubble.setVisible(false);
		moneyBubble.setManaged(false);

		Button confirmNameButton = new Button("Start");
		confirmNameButton.getStyleClass().add("start-button");
		confirmNameButton.setFont(Font.font(fontFamily, BUTTON_FONT_SIZE));
		confirmNameButton.setPrefWidth(380);
		confirmNameButton.setPrefHeight(55);
		UiSoundEffects.installHoverSound(confirmNameButton);
		UiSoundEffects.installClickSound(confirmNameButton);

		fileNameField.setEditable(false);

		boss = new Boss("What do you mean all our employees quit...", BOSS_SIZE);
		// User can immediately enter their name and confirm
		fileNameField.setEditable(true);
		inputBubble.setVisible(true);
		inputBubble.setManaged(true);
		if (bossMessage != null && !bossMessage.isBlank()) {
			boss.updateTalkingBubble(bossMessage);
		} else {
			boss.updateTalkingBubble("Hey there! What's your name?");
		}


		confirmNameButton.setOnAction(ev -> {
			String playerName = fileNameField.getText() == null ? "" : fileNameField.getText().trim();

			if (playerName.isBlank()) {
				boss.updateTalkingBubble("Hey, I am talking to you! Enter your name.");
				fileNameField.requestFocus();
				return;
			}
			if (playerName.length() > 20) {
				boss.updateTalkingBubble("I am not remembering all that! Get a shorter name.");
				fileNameField.requestFocus();
				return;
			}
			if (handler.doesSaveFileExist(playerName)) {
				boss.updateTalkingBubble("I have already met someone with that name! Choose another one.");
				fileNameField.requestFocus();
				return;
			}

			// Name is valid - ask for starting money
			fileNameField.setEditable(false);
			inputBubble.setVisible(false);
			inputBubble.setManaged(false);
			moneyField.setText("");
			moneyField.setEditable(true);
			moneyBubble.setVisible(true);
			moneyBubble.setManaged(true);
			contentBox.getChildren().setAll(moneyBubble, confirmNameButton);
			boss.updateTalkingBubble("How much money do you want to start with?");

			confirmNameButton.setOnAction(ev2 -> {
				String moneyText = moneyField.getText() == null ? "" : moneyField.getText().trim();
				if (moneyText.isBlank()) {
					boss.updateTalkingBubble("Enter a starting amount!");
					moneyField.requestFocus();
					return;
				}
				try {
					double money = Double.parseDouble(moneyText);
					if (money < 1000 || money > 100000000) {
						boss.updateTalkingBubble("Pick an amount between 1,000 and 100,000,000.");
						moneyField.requestFocus();
						return;
					}
				} catch (NumberFormatException ex) {
					boss.updateTalkingBubble("That's not a valid number!");
					moneyField.requestFocus();
					return;
				}

				// Show experience options
				Button tutorialButton = createOptionButton("Tutorial", fontFamily, () ->
						handler.onCreateGame(playerName, "Tutorial", "sp500", moneyText));
				Button easyButton = createOptionButton("Easy", fontFamily, () ->
						showExchangeOptions(contentBox, boss, fontFamily, handler, playerName, "Easy", moneyText));
				Button mediumButton = createOptionButton("Medium", fontFamily, () ->
						showExchangeOptions(contentBox, boss, fontFamily, handler, playerName, "Medium", moneyText));
				Button hardButton = createOptionButton("Hard", fontFamily, () ->
						showExchangeOptions(contentBox, boss, fontFamily, handler, playerName, "Hard", moneyText));

				contentBox.getChildren().setAll(tutorialButton, easyButton, mediumButton, hardButton);
				boss.updateTalkingBubble("Are you any good at this? If you are, you don't mind a higher commission, right?");

				moneyField.setEditable(false);
				moneyBubble.setVisible(false);
				moneyBubble.setManaged(false);
			});
		});

		// Handle Enter key press
		fileNameField.setOnAction(ev -> confirmNameButton.getOnAction().handle(null));
		moneyField.setOnAction(ev -> confirmNameButton.getOnAction().handle(null));

		ImageView exitRedImage = createExitImageView(EXIT_RED_PATH);
		ImageView exitGreenImage = createExitImageView(EXIT_GREEN_PATH);
		exitGreenImage.setVisible(false);

		StackPane backButton = new StackPane(exitRedImage, exitGreenImage);
		backButton.setPickOnBounds(false);
		backButton.setMinSize(EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE);
		backButton.setPrefSize(EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE);
		backButton.setMaxSize(EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE);
		backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
		backButton.setOnMouseEntered(e -> {
			exitRedImage.setVisible(false);
			exitGreenImage.setVisible(true);
		});
		backButton.setOnMouseExited(e -> {
			exitGreenImage.setVisible(false);
			exitRedImage.setVisible(true);
		});
		backButton.setOnMouseClicked(e -> handler.onBack());
		UiSoundEffects.installHoverSound(backButton);
		UiSoundEffects.installClickSound(backButton);

		contentBox.getChildren().addAll(inputBubble, confirmNameButton);
		getChildren().add(contentBox);
		StackPane.setAlignment(contentBox, Pos.CENTER);
		StackPane.setMargin(contentBox, new Insets(90, 0, 0, 0));

		getChildren().add(backButton);
		StackPane.setAlignment(backButton, Pos.TOP_LEFT);
		StackPane.setMargin(backButton, new Insets(20, 0, 0, 20));

		getChildren().add(boss);
		StackPane.setAlignment(boss, Pos.BOTTOM_LEFT);
		StackPane.setMargin(boss, new Insets(120, 0, 0, -70));
	}

	private ImageView createExitImageView(String path) {
		InputStream imageStream = getClass().getResourceAsStream(path);
		if (imageStream == null) {
			return new ImageView();
		}

		Image image = new Image(imageStream, EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE, true, false);
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(false);
		imageView.setCache(false);
		return imageView;
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

	private Button createOptionButton(String text, String fontFamily, Runnable action) {
		Button button = new Button(text);
		button.getStyleClass().add("start-button");
		button.setFont(Font.font(fontFamily, OPTION_BUTTON_FONT_SIZE));
		button.setPrefWidth(380);
		button.setPrefHeight(50);
		button.setOnAction(e -> action.run());
		UiSoundEffects.installHoverSound(button);
		UiSoundEffects.installClickSound(button);
		return button;
	}

	private void showExchangeOptions(
			VBox contentBox,
			Boss boss,
			String fontFamily,
			CreateGameHandler handler,
			String playerName,
			String experienceLevel,
			String startingMoney
	) {
		Button spButton = createOptionButton("sp500", fontFamily, () ->
				handler.onCreateGame(playerName, experienceLevel, "sp500", startingMoney));
		Button randomButton = createOptionButton("Random", fontFamily, () ->
				handler.onCreateGame(playerName, experienceLevel, "random", startingMoney));

		Button customButton = createOptionButton("Custom", fontFamily, () -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select CSV File");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
			File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
			if (selectedFile != null) {
				try {
					if (validateCSVStructure(selectedFile)) {
						handler.onCreateGame(playerName, experienceLevel, "custom:" + selectedFile.getAbsolutePath(), startingMoney);
					} else {
						boss.updateTalkingBubble("CSV is invalid! Please select a file matching the template.");
					}
				} catch (Exception e) {
					boss.updateTalkingBubble("Error reading CSV. Please try again.");
				}
			} else {
				boss.updateTalkingBubble("No file selected. Please choose a CSV file.");
			}
		});

		contentBox.getChildren().setAll(spButton, randomButton, customButton);
		boss.updateTalkingBubble("Which exchange do you play?");
	}

	   /**
		* Validates that the CSV file matches the structure of sp500Standard.csv (header and column count).
		*/
	   private boolean validateCSVStructure(File csvFile) {
		   // Path to the reference CSV in resources
		   String referencePath = "/csv/input/sp500Standard.csv";
		   try (
			   BufferedReader userReader = new BufferedReader(new FileReader(csvFile));
			   InputStream refStream = getClass().getResourceAsStream(referencePath);
			   BufferedReader refReader = refStream != null ? new BufferedReader(new InputStreamReader(refStream)) : null
		   ) {
			   if (refReader == null) return false;
			   String refHeader = refReader.readLine();
			   String userHeader = userReader.readLine();
			   if (refHeader == null || userHeader == null) return false;
			   // Compare headers (ignoring whitespace)
			   if (!refHeader.replaceAll("\\s+", "").equalsIgnoreCase(userHeader.replaceAll("\\s+", ""))) {
				   return false;
			   }
			   // Optionally, check column count for first data row
			   String refFirstData = refReader.readLine();
			   String userFirstData = userReader.readLine();
			   if (refFirstData != null && userFirstData != null) {
				   int refCols = refFirstData.split(",").length;
				   int userCols = userFirstData.split(",").length;
				   if (refCols != userCols) return false;
			   }
			   return true;
		   } catch (Exception e) {
			   return false;
		   }
	   }
}