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
    tutorialStep = 1;
    boss.updateTalkingBubble("Congratulations rookie, you get to work for me. Start earning me money.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void stopTutorial() {
    tutorialStep = 0;
    layer.setVisible(false);
  }

  public boolean isActive() {
    return layer.isVisible();
  }

  public void onNewspaperViewed() {
    if (!isActive() || tutorialStep != 1) {
      return;
    }

    tutorialStep = 2;
    boss.updateTalkingBubble("You can start by reading the newspaper.");
  }

  public void onReadyToBuy() {
    if (!isActive() || tutorialStep != 2) {
      return;
    }

    tutorialStep = 3;
    boss.updateTalkingBubble("WOW! Look at those news this makes it obvious what you should buy. Go buy something.");
  }

  public void onStockSelected() {
    if (!isActive() || tutorialStep != 3) {
      return;
    }

    tutorialStep = 4;
    boss.updateTalkingBubble("Select the stock that you know will make me money.");
  }

  public void onBuySuccess() {
    if (!isActive() || tutorialStep != 4) {
      return;
    }

    tutorialStep = 5;
    boss.updateTalkingBubble("Now buy it.");
  }

  public void onPortfolioViewed() {
    if (!isActive() || tutorialStep != 5) {
      return;
    }

    tutorialStep = 6;
    boss.updateTalkingBubble("If you ever want to know more about your purchases check your transaction history.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onHistoryViewed() {
    if (!isActive() || tutorialStep != 6) {
      return;
    }

    tutorialStep = 7;
    boss.updateTalkingBubble("Or look at your portfolio to see what you own.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onNextWeek() {
    if (!isActive() || tutorialStep != 7) {
      return;
    }

    tutorialStep = 8;
    boss.updateTalkingBubble("Now advance to the next week.");
    
    new Thread(() -> {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException ignored) {}
      
      if (layer.isVisible() && tutorialStep == 8) {
        tutorialStep = 9;
        boss.updateTalkingBubble("What!? You didn't earn nearly enough money for me.");
      }
    }).start();
  }

  public void onSellSuccess() {
    if (!isActive() || tutorialStep != 9) {
      return;
    }

    tutorialStep = 10;
    boss.updateTalkingBubble("Sell that stock.");
    
    new Thread(() -> {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException ignored) {}
      
      if (layer.isVisible() && tutorialStep == 10) {
        tutorialStep = 11;
        boss.updateTalkingBubble("You better start earning more money before the next Q or you are out of here!");
        
        try {
          Thread.sleep(3000);
        } catch (InterruptedException ignored) {}
        stopTutorial();
      }
    }).start();
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

  private void onContinuePressed() {
    if (!isActive()) {
      return;
    }

    if (tutorialStep == 1) {
      tutorialStep = 2;
      boss.updateTalkingBubble("You can start by reading the newspaper.");
    } else if (tutorialStep == 6) {
      tutorialStep = 8;
      onNextWeek();
    } else if (tutorialStep == 7) {
      tutorialStep = 8;
      onNextWeek();
    }
  }
}