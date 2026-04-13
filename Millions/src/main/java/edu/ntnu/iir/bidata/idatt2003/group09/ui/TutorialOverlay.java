package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class TutorialOverlay {

  private static final String FONT_PATH = "/ThaleahFat.ttf";
  private static final double TITLE_FONT_SIZE = 32;
  private static final double BOSS_SIZE = 430;

  private static final double NEWSPAPER_TAB_X = 180;
  private static final double TRADE_TAB_X = 20;
  private static final double PORTFOLIO_TAB_X = 90;
  private static final double HISTORY_TAB_X = 230;
  private static final double TAB_Y = 10;
  private static final double TAB_H = 55;

  private static final double STOCK_LIST_X = 20;
  private static final double STOCK_LIST_Y = 380;
  private static final double STOCK_LIST_W = 520;
  private static final double STOCK_LIST_H = 300;

  private static final double BUY_BUTTON_X = 220;
  private static final double BUY_BUTTON_Y = 275;
  private static final double BUY_BUTTON_W = 85;
  private static final double BUY_BUTTON_H = 45;

  private static final double SELL_BUTTON_X = 312;
  private static final double SELL_BUTTON_Y = 275;
  private static final double SELL_BUTTON_W = 85;
  private static final double SELL_BUTTON_H = 45;

  private static final double NEXT_WEEK_BUTTON_X = 400;
  private static final double NEXT_WEEK_BUTTON_Y = 275;
  private static final double NEXT_WEEK_BUTTON_W = 130;
  private static final double NEXT_WEEK_BUTTON_H = 45;

  private final AnchorPane layer;
  private final Pane dimLayer;
  private final Rectangle fullDim;
  private final Rectangle topDim;
  private final Rectangle leftDim;
  private final Rectangle rightDim;
  private final Rectangle bottomDim;
  private final Rectangle spotlightBorder;
  private final Boss boss;
  private int tutorialStep;
  private boolean spotlightActive;
  private double spotlightX;
  private double spotlightY;
  private double spotlightWidth;
  private double spotlightHeight;

  public TutorialOverlay() {
    this.boss = new Boss("Welcome! Select a stock on the left to begin.", loadFontFamily(), BOSS_SIZE);
    this.boss.setTalkingLoops(1);
    this.boss.getImageView().setScaleX(-1);
    this.boss.getChatBubble().setTranslateX(-185);
    this.boss.setPickOnBounds(false);

    this.layer = new AnchorPane();
    this.layer.setPickOnBounds(false);
    this.layer.setMouseTransparent(false);
    this.layer.setVisible(false);

    this.dimLayer = new Pane();
    this.dimLayer.setPickOnBounds(false);
    this.dimLayer.setMouseTransparent(false);

    this.fullDim = new Rectangle();
    this.fullDim.setFill(Color.rgb(0, 0, 0, 0.72));

    this.topDim = createDimRect();
    this.leftDim = createDimRect();
    this.rightDim = createDimRect();
    this.bottomDim = createDimRect();

    this.spotlightBorder = new Rectangle();
    this.spotlightBorder.setFill(Color.TRANSPARENT);
    this.spotlightBorder.setStroke(Color.web("#ffd447"));
    this.spotlightBorder.setStrokeWidth(4);
    this.spotlightBorder.setMouseTransparent(true);

    this.dimLayer.getChildren().addAll(fullDim, topDim, leftDim, rightDim, bottomDim, spotlightBorder);
    this.dimLayer.prefWidthProperty().bind(this.layer.widthProperty());
    this.dimLayer.prefHeightProperty().bind(this.layer.heightProperty());
    this.layer.widthProperty().addListener((obs, oldWidth, newWidth) -> refreshDimming());
    this.layer.heightProperty().addListener((obs, oldHeight, newHeight) -> refreshDimming());

    this.layer.getChildren().addAll(this.dimLayer, this.boss);
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
    clearSpotlight();
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
    if (!isActive() || tutorialStep != 2) {
      return;
    }

    tutorialStep = 3;
    spotlight(NEWSPAPER_TAB_X, TAB_Y, 160, TAB_H);
    boss.updateTalkingBubble("Look at all these news.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onReadyToBuy() {
    if (!isActive() || tutorialStep != 4) {
      return;
    }

    tutorialStep = 5;
    spotlight(STOCK_LIST_X, STOCK_LIST_Y, STOCK_LIST_W, STOCK_LIST_H);
    boss.updateTalkingBubble("WOW! Look at those news this makes it obvious what you should buy. Go buy something.");
  }

  public void onStockSelected() {
    if (!isActive() || tutorialStep != 5) {
      return;
    }

    tutorialStep = 6;
    spotlight(BUY_BUTTON_X, BUY_BUTTON_Y, BUY_BUTTON_W, BUY_BUTTON_H);
    boss.updateTalkingBubble("Select the stock that you know will make me money.");
  }

  public void onBuySuccess() {
    if (!isActive() || tutorialStep != 6) {
      return;
    }

    tutorialStep = 7;
    spotlight(HISTORY_TAB_X, TAB_Y, 190, TAB_H);
    boss.updateTalkingBubble("If you ever want to know more about your purchases check your transaction history.");
  }

  public void onTransactionHistoryViewed() {
    if (!isActive() || tutorialStep != 7) {
      return;
    }

    tutorialStep = 8;
    spotlight(HISTORY_TAB_X, TAB_Y, 190, TAB_H);
    boss.updateTalkingBubble("If you ever want to know more about your purchases check your transaction history.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onPortfolioViewed() {
    if (!isActive() || tutorialStep != 9) {
      return;
    }

    tutorialStep = 10;
    spotlight(TRADE_TAB_X, TAB_Y, 120, TAB_H);
    boss.updateTalkingBubble("Good! Now go to the trade screen and advance to the next week.");
  }

  public void onTradeScreenViewed() {
    if (!isActive() || tutorialStep != 10) {
      return;
    }

    tutorialStep = 11;
    spotlight(NEXT_WEEK_BUTTON_X, NEXT_WEEK_BUTTON_Y, NEXT_WEEK_BUTTON_W, NEXT_WEEK_BUTTON_H);
    boss.updateTalkingBubble("Good! Now go to the trade screen and advance to the next week.");
  }

  public void onNextWeek() {
    if (!isActive() || tutorialStep != 11) {
      return;
    }

    tutorialStep = 12;
    spotlight(SELL_BUTTON_X, SELL_BUTTON_Y, SELL_BUTTON_W, SELL_BUTTON_H);
    boss.updateTalkingBubble("What!? You didn't earn nearly enough money for me.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onSellSuccess() {
    if (!isActive() || tutorialStep != 13) {
      return;
    }

    tutorialStep = 14;
    clearSpotlight();
    boss.updateTalkingBubble("You better start earning more money before the next Q or you are out of here!");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
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
      spotlight(NEWSPAPER_TAB_X, TAB_Y, 140, TAB_H);
      boss.updateTalkingBubble("You can start by reading the newspaper.");
    } else if (tutorialStep == 3) {
      tutorialStep = 4;
      spotlight(TRADE_TAB_X, TAB_Y, 120, TAB_H);
      boss.updateTalkingBubble("WOW! Look at those news this makes it obvious what you should buy. Go buy something.");
    } else if (tutorialStep == 8) {
      tutorialStep = 9;
      spotlight(PORTFOLIO_TAB_X, TAB_Y, 140, TAB_H);
      boss.updateTalkingBubble("Or look at your portfolio to see what you own.");
    } else if (tutorialStep == 12) {
      tutorialStep = 13;
      spotlight(SELL_BUTTON_X, SELL_BUTTON_Y, SELL_BUTTON_W, SELL_BUTTON_H);
      boss.updateTalkingBubble("Sell that stock.");
    } else if (tutorialStep == 14) {
      stopTutorial();
    }
  }

  private Rectangle createDimRect() {
    Rectangle rectangle = new Rectangle();
    rectangle.setFill(Color.rgb(0, 0, 0, 0.72));
    rectangle.setMouseTransparent(false);
    return rectangle;
  }

  private void spotlight(double x, double y, double width, double height) {
    spotlightActive = true;
    spotlightX = x;
    spotlightY = y;
    spotlightWidth = width;
    spotlightHeight = height;
    refreshDimming();
  }

  private void clearSpotlight() {
    spotlightActive = false;
    refreshDimming();
  }

  private void refreshDimming() {
    double layerWidth = Math.max(0, layer.getWidth());
    double layerHeight = Math.max(0, layer.getHeight());

    if (!spotlightActive) {
      fullDim.setVisible(true);
      fullDim.setX(0);
      fullDim.setY(0);
      fullDim.setWidth(layerWidth);
      fullDim.setHeight(layerHeight);

      topDim.setVisible(false);
      leftDim.setVisible(false);
      rightDim.setVisible(false);
      bottomDim.setVisible(false);
      spotlightBorder.setVisible(false);
      return;
    }

    double clampedX = Math.max(0, spotlightX);
    double clampedY = Math.max(0, spotlightY);
    double clampedW = Math.max(0, Math.min(spotlightWidth, layerWidth - clampedX));
    double clampedH = Math.max(0, Math.min(spotlightHeight, layerHeight - clampedY));

    fullDim.setVisible(false);
    topDim.setVisible(true);
    leftDim.setVisible(true);
    rightDim.setVisible(true);
    bottomDim.setVisible(true);
    spotlightBorder.setVisible(true);

    topDim.setX(0);
    topDim.setY(0);
    topDim.setWidth(layerWidth);
    topDim.setHeight(clampedY);

    leftDim.setX(0);
    leftDim.setY(clampedY);
    leftDim.setWidth(clampedX);
    leftDim.setHeight(clampedH);

    rightDim.setX(clampedX + clampedW);
    rightDim.setY(clampedY);
    rightDim.setWidth(Math.max(0, layerWidth - (clampedX + clampedW)));
    rightDim.setHeight(clampedH);

    bottomDim.setX(0);
    bottomDim.setY(clampedY + clampedH);
    bottomDim.setWidth(layerWidth);
    bottomDim.setHeight(Math.max(0, layerHeight - (clampedY + clampedH)));

    spotlightBorder.setX(clampedX);
    spotlightBorder.setY(clampedY);
    spotlightBorder.setWidth(clampedW);
    spotlightBorder.setHeight(clampedH);
  }
}