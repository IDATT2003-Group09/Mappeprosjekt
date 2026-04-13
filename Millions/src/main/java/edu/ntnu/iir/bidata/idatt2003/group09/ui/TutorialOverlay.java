package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class TutorialOverlay {

  private static final String FONT_PATH = "/ThaleahFat.ttf";
  private static final double TITLE_FONT_SIZE = 32;
  private static final double BOSS_SIZE = 430;

  private final AnchorPane layer;
  private final Boss boss;
  private int tutorialStep;

  public TutorialOverlay() {
    this.boss = new Boss("Welcome! Select a stock on the left to begin.", loadFontFamily(), BOSS_SIZE);
    this.boss.setTalkingLoops(1);
    this.boss.getImageView().setScaleX(-1);
    this.boss.getChatBubble().setTranslateX(-185);
    this.boss.setMouseTransparent(true);

    this.layer = new AnchorPane();
    this.layer.setPickOnBounds(false);
    this.layer.setMouseTransparent(true);
    this.layer.setVisible(false);

    this.layer.getChildren().add(this.boss);
    AnchorPane.setBottomAnchor(this.boss, 0.0);
    AnchorPane.setRightAnchor(this.boss, -40.0);

    this.tutorialStep = 0;
  }

  public AnchorPane getLayer() {
    return layer;
  }

  public void startTutorial() {
    tutorialStep = 0;
    layer.setVisible(true);
    boss.updateTalkingBubble("Welcome! Select a stock on the left to begin.");
  }

  public void stopTutorial() {
    tutorialStep = 0;
    layer.setVisible(false);
  }

  public boolean isActive() {
    return layer.isVisible();
  }

  public void onStockSelected() {
    if (!isActive() || tutorialStep != 0) {
      return;
    }

    tutorialStep = 1;
    boss.updateTalkingBubble("Great! Now click Buy to purchase one share.");
  }

  public void onBuySuccess() {
    if (!isActive() || tutorialStep > 2) {
      return;
    }

    tutorialStep = 2;
    boss.updateTalkingBubble("Nice buy. Click Next Week to advance the market.");
  }

  public void onNextWeek() {
    if (!isActive() || tutorialStep != 2) {
      return;
    }

    tutorialStep = 3;
    boss.updateTalkingBubble("Good! Now click Sell to close one position.");
  }

  public void onSellSuccess() {
    if (!isActive() || tutorialStep < 3) {
      return;
    }

    tutorialStep = 4;
    stopTutorial();
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
}