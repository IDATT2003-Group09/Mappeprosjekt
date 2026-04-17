package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Game.GameProgress;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameOverScreen extends StackPane {

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public GameOverScreen(GameController controller, Runnable onBack) {
        GameProgress progress = controller.getProgress();
        BigDecimal netWorth = controller.getNetWorth();
        BigDecimal target = progress.getCurrentTarget();
        BigDecimal shortfall = calculateShortfall(netWorth, target);
        int quarter = progress.getCheckpointLevel();
        int week = progress.getCurrentWeek();
        getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/styling/gameoverscreen.css").toExternalForm());
        getStyleClass().add("game-over-screen");
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Region backdrop = new Region();
        backdrop.getStyleClass().add("game-over-backdrop");
        backdrop.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Region dimLayer = new Region();
        dimLayer.getStyleClass().add("game-over-dim");
        dimLayer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox card = new VBox(18);
        card.getStyleClass().add("game-over-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(720);
        card.setPadding(new Insets(30, 36, 28, 36));

        Label eyebrow = new Label("Quarter " + quarter + " deadline missed");
        eyebrow.getStyleClass().add("game-over-eyebrow");

        Label title = new Label("Game Over");
        title.getStyleClass().add("game-over-title");

        Label subtitle = new Label(buildSubtitle(controller, quarter, week, netWorth, target));
        subtitle.getStyleClass().add("game-over-subtitle");
        subtitle.setWrapText(true);

        HBox chips = new HBox(
            10,
            createChip("MISSED TARGET"),
            createChip(controller.getPlayer().getDifficulty().toUpperCase(Locale.ROOT)),
            createChip("WEEK " + week)
        );
        chips.setAlignment(Pos.CENTER_LEFT);

        GridPane metricGrid = new GridPane();
        metricGrid.getStyleClass().add("game-over-metrics");
        metricGrid.setHgap(14);
        metricGrid.setVgap(14);
        metricGrid.getColumnConstraints().addAll(createMetricColumn(), createMetricColumn(), createMetricColumn());
        metricGrid.add(createMetricCard("Net Worth", currencyFormat.format(netWorth), false), 0, 0);
        metricGrid.add(createMetricCard("Quarter Goal", currencyFormat.format(target), false), 1, 0);
        metricGrid.add(createMetricCard("Shortfall", currencyFormat.format(shortfall), true), 2, 0);

        Button backButton = new Button("Back to menu");
        backButton.getStyleClass().add("start-button");
        backButton.getStyleClass().add("game-over-button");
        backButton.setPrefWidth(320);
        backButton.setPrefHeight(58);
        backButton.setOnAction(event -> onBack.run());
        UiSoundEffects.installHoverSound(backButton);
        UiSoundEffects.installClickSound(backButton);

        card.getChildren().addAll(eyebrow, title, subtitle, chips, metricGrid, backButton);

        getChildren().addAll(backdrop, dimLayer, card);
        StackPane.setAlignment(card, Pos.CENTER);
        StackPane.setMargin(card, new Insets(0, 0, 40, 0));

        Platform.runLater(backButton::requestFocus);
    }

    private VBox createMetricCard(String labelText, String valueText, boolean accent) {
        VBox metricCard = new VBox(6);
        metricCard.getStyleClass().add("game-over-metric-card");
        if (accent) {
            metricCard.getStyleClass().add("game-over-metric-card-accent");
        }

        Label label = new Label(labelText);
        label.getStyleClass().add("game-over-metric-label");

        Label value = new Label(valueText);
        value.getStyleClass().add("game-over-metric-value");

        metricCard.getChildren().addAll(label, value);
        return metricCard;
    }

    private Label createChip(String text) {
        Label chip = new Label(text);
        chip.getStyleClass().add("game-over-chip");
        return chip;
    }

    private ColumnConstraints createMetricColumn() {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(33.33);
        column.setHgrow(Priority.ALWAYS);
        return column;
    }

    private BigDecimal calculateShortfall(BigDecimal netWorth, BigDecimal target) {
        if (target == null || netWorth == null) {
            return BigDecimal.ZERO;
        }
        if (target.compareTo(netWorth) <= 0) {
            return BigDecimal.ZERO;
        }
        return target.subtract(netWorth);
    }

    private String buildSubtitle(
            GameController controller,
            int quarter,
            int week,
            BigDecimal netWorth,
            BigDecimal target
    ) {
        return "%s, the board wanted %s by the end of quarter %d. You reached %s at week %d, so the office just ran out of patience."
                .formatted(
                        controller.getPlayer().getName(),
                        currencyFormat.format(target),
                        quarter,
                        currencyFormat.format(netWorth),
                        week
                );
    }
}
