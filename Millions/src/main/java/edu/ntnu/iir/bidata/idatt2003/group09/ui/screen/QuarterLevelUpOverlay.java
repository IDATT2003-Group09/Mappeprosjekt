package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.Boss;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
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
import javafx.scene.text.Font;
import javafx.util.Duration;

public class QuarterLevelUpOverlay extends StackPane {

    private static final String FONT_PATH = "/ThaleahFat.ttf";
    private static final double FONT_SIZE = 26;
    private static final double BOSS_SIZE = 360;
    private static final List<String> BOSS_QUOTES = List.of(
        "Compounding rewards the patient long before it flatters the lucky.",
        "A good quarter is proof that discipline can outperform drama.",
        "Momentum is rented weekly. Judgment is what lets you keep it.",
        "The market respects calm hands and very selective panic."
    );

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);


}
