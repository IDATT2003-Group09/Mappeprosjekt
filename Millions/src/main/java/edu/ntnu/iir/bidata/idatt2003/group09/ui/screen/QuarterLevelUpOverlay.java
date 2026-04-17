package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class QuarterLevelUpOverlay extends StackPane {

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public QuarterLevelUpOverlay(
            int completedQuarter,
            int unlockedQuarter,
            BigDecimal clearedNetWorth,
            BigDecimal clearedTarget,
            BigDecimal nextTarget,
            Runnable onContinue
    ) {
        getStyleClass().add("quarter-level-overlay");
        setPickOnBounds(true);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Region backdrop = new Region();
        backdrop.getStyleClass().add("quarter-level-backdrop");
        backdrop.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Region aura = new Region();
        aura.getStyleClass().add("quarter-level-aura");
        aura.setMouseTransparent(true);

        VBox card = new VBox(18);
        card.getStyleClass().add("quarter-level-card");
        card.setMaxWidth(720);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(30, 36, 28, 36));

        Label eyebrow = new Label("Quarter " + completedQuarter + " cleared");
        eyebrow.getStyleClass().add("quarter-level-eyebrow");

        Label title = new Label("Welcome to Q" + unlockedQuarter);
        title.getStyleClass().add("quarter-level-title");

        Label subtitle = new Label(
                "You hit the number, survived the deadline, and bought yourself another quarter to impress the board."
        );
        subtitle.getStyleClass().add("quarter-level-subtitle");
        subtitle.setWrapText(true);

        HBox rewardChips = new HBox(
                10,
                createChip("TARGET MET"),
                createChip("QUARTER CLEARED"),
                createChip("Q" + unlockedQuarter + " UNLOCKED")
        );
        rewardChips.setAlignment(Pos.CENTER_LEFT);

        GridPane metricGrid = new GridPane();
        metricGrid.setHgap(14);
        metricGrid.setVgap(14);
        metricGrid.getStyleClass().add("quarter-level-metrics");

        metricGrid.getColumnConstraints().addAll(
                createMetricColumn(),
                createMetricColumn(),
                createMetricColumn()
        );

        metricGrid.add(createMetricCard("Net Worth", currencyFormat.format(clearedNetWorth), false), 0, 0);
        metricGrid.add(createMetricCard("Quarter Goal", currencyFormat.format(clearedTarget), false), 1, 0);
        metricGrid.add(createMetricCard("Next Goal", currencyFormat.format(nextTarget), true), 2, 0);

        Button continueButton = new Button("Back to trading");
        continueButton.getStyleClass().add("quarter-level-button");
        UiSoundEffects.installHoverSound(continueButton);
        UiSoundEffects.installClickSound(continueButton);
        continueButton.setOnAction(event -> onContinue.run());

        card.getChildren().addAll(eyebrow, title, subtitle, rewardChips, metricGrid, continueButton);

        getChildren().addAll(backdrop, aura, card);
        StackPane.setAlignment(card, Pos.CENTER);
        StackPane.setAlignment(aura, Pos.CENTER);

        playEntrance(card, aura, rewardChips);
        Platform.runLater(continueButton::requestFocus);
    }


}
