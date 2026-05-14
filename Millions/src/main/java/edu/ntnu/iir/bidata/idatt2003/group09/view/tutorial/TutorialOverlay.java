package edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial;

import java.io.IOException;
import java.io.InputStream;

import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.Boss;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class TutorialOverlay {

  private static final String FONT_PATH = "/ThaleahFat.ttf";
  private static final double TITLE_FONT_SIZE = 32;
  private static final double BOSS_SIZE = 430;

  private static final double NEWSPAPER_TAB_X = 200;
  private static final double NEWSPAPER_TAB_W = 150;
  private static final double TRADE_TAB_X = 14;
  private static final double TRADE_TAB_W = 90;
  private static final double PORTFOLIO_TAB_X = 90;
  private static final double PORTFOLIO_TAB_W = 125;
  private static final double HISTORY_TAB_X = 350;
  private static final double HISTORY_TAB_W = 220;
  private static final double TAB_Y = 0;
  private static final double TAB_H = 46;

  private static final double STOCK_LIST_X = 16;
  private static final double STOCK_LIST_Y = 250;
  private static final double STOCK_LIST_W = 524;
  private static final double STOCK_LIST_H = 448;

  // Search bar (sits in the filter bar row above the stock list)
  private static final double SEARCH_BAR_X = 16;
  private static final double SEARCH_BAR_Y = 196;
  private static final double SEARCH_BAR_W = 350;
  private static final double SEARCH_BAR_H = 36;

  // Sector button container (row below the filter bar)
  private static final double SECTOR_BUTTONS_X = 16;
  private static final double SECTOR_BUTTONS_Y = 204;
  private static final double SECTOR_BUTTONS_W = 900;
  private static final double SECTOR_BUTTONS_H = 40;

  // Max Buy button
  private static final double MAX_BUY_BUTTON_X = 786;
  private static final double MAX_BUY_BUTTON_Y = 650;
  private static final double MAX_BUY_BUTTON_W = 90;
  private static final double MAX_BUY_BUTTON_H = 36;

  private static final double BUY_BUTTON_X = 840;
  private static final double BUY_BUTTON_Y = 650;
  private static final double BUY_BUTTON_W = 70;
  private static final double BUY_BUTTON_H = 36;

  private static final double SELL_BUTTON_X = 890;
  private static final double SELL_BUTTON_Y = 650;
  private static final double SELL_BUTTON_W = 90;
  private static final double SELL_BUTTON_H = 46;

  private static final double NEXT_WEEK_BUTTON_X = 980;
  private static final double NEXT_WEEK_BUTTON_Y = 55;
  private static final double NEXT_WEEK_BUTTON_W = 132;
  private static final double NEXT_WEEK_BUTTON_H = 46;

  private static final double NEWSPAPER_VIEW_X = 18;
  private static final double NEWSPAPER_VIEW_Y = 76;
  private static final double NEWSPAPER_VIEW_W = 1055;
  private static final double NEWSPAPER_VIEW_H = 604;
  private static final double TAB_CONTENT_X = 0;
  private static final double TAB_CONTENT_Y = 40;
  private static final double TAB_CONTENT_W = 1100;
  private static final double TAB_CONTENT_H = 684;

  // Step numbers — keeping them as named constants makes future changes easier
  private static final int STEP_START               = 1;
  private static final int STEP_NEWSPAPER_TAB       = 2;
  private static final int STEP_NEWSPAPER_VIEW      = 3;
  private static final int STEP_TRADE_TAB           = 4;
  private static final int STEP_STOCK_LIST          = 5;
  // --- new steps ---
  private static final int STEP_SEARCH_BAR          = 6;
  private static final int STEP_SECTOR_BUTTONS      = 7;
  private static final int STEP_MAX_BUY             = 8;
  // --- shifted steps ---
  private static final int STEP_BUY_BUTTON          = 9;
  private static final int STEP_BUY_CONFIRM         = 10;
  private static final int STEP_BUY_SUCCESS         = 11;
  private static final int STEP_HISTORY_TAB         = 12;
  private static final int STEP_TRANSACTION_HISTORY = 13;
  private static final int STEP_PORTFOLIO_TAB       = 14;
  private static final int STEP_PORTFOLIO_VIEW      = 15;
  private static final int STEP_TRADE_TAB_2         = 16;
  private static final int STEP_TRADE_TAB_2_VIEWED  = 17;
  private static final int STEP_SELL_BUTTON         = 18;
  private static final int STEP_SELL_CONFIRM        = 19;
  private static final int STEP_SELL_SUCCESS_VIEW   = 20;
  private static final int STEP_SELL_SUCCESS        = 21;
  private static final int STEP_END                 = 22;

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
    this.boss = new Boss("Welcome! Select a stock on the left to begin.", BOSS_SIZE);
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
    tutorialStep = STEP_START;
    clearSpotlight();
    boss.updateTalkingBubble("Congratulations rookie, you get to work for me. Start earning me money.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void stopTutorial() {
    tutorialStep = 0;
    layer.setVisible(false);
  }

  public int getCurrentStep() {
    return tutorialStep;
  }

  public boolean isAtConfirmationStep() {
    return tutorialStep == STEP_BUY_CONFIRM || tutorialStep == STEP_SELL_CONFIRM;
  }

  public boolean isActive() {
    return layer.isVisible();
  }

  public void onNewspaperViewed() {
    if (!isActive() || tutorialStep != STEP_NEWSPAPER_TAB) {
      return;
    }

    tutorialStep = STEP_NEWSPAPER_VIEW;
    spotlight(NEWSPAPER_VIEW_X, NEWSPAPER_VIEW_Y, NEWSPAPER_VIEW_W, NEWSPAPER_VIEW_H);
    boss.updateTalkingBubble("Take your time reading the news, it might give you some hints on what to buy.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onTradeScreenViewed() {
    if (!isActive() || tutorialStep != STEP_TRADE_TAB && tutorialStep != STEP_TRADE_TAB_2) {
      return;
    }

    if (tutorialStep == STEP_TRADE_TAB) {
      tutorialStep = STEP_STOCK_LIST;
      spotlight(STOCK_LIST_X, STOCK_LIST_Y, STOCK_LIST_W, STOCK_LIST_H);
      boss.updateTalkingBubble("Now select a stock that you think will make me money.");
    } else {
      // STEP_TRADE_TAB_2 — returning to trade screen before sell
      tutorialStep = STEP_TRADE_TAB_2_VIEWED;
      spotlight(NEXT_WEEK_BUTTON_X, NEXT_WEEK_BUTTON_Y, NEXT_WEEK_BUTTON_W, NEXT_WEEK_BUTTON_H);
      boss.updateTalkingBubble("Great! Now advance to the next week.");
    }
  }

  public void onStockSelected() {
    if (!isActive() || tutorialStep != STEP_STOCK_LIST) {
      return;
    }

    // Advance to search bar introduction — user does not need to interact with it,
    // just show the spotlight and let them continue.
    tutorialStep = STEP_SEARCH_BAR;
    spotlight(SEARCH_BAR_X, SEARCH_BAR_Y, SEARCH_BAR_W, SEARCH_BAR_H);
    boss.updateTalkingBubble("You can search for stocks by name or ticker symbol up here.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onBuyButtonClicked() {
    if (!isActive() || tutorialStep != STEP_BUY_BUTTON) {
      return;
    }

    boss.visibleBoss(true);
    tutorialStep = STEP_BUY_CONFIRM;
    clearSpotlight();
    boss.updateTalkingBubble("Review your purchase in the overview and click confirm to complete the transaction.");
    boss.getChatBubble().addContinueButton(() -> onConfirmOverviewSeen(false));
  }

  public void onSellButtonClicked() {
    if (!isActive() || tutorialStep != STEP_SELL_BUTTON) {
      return;
    }

    tutorialStep = STEP_SELL_CONFIRM;
    clearSpotlight();
    boss.visibleBoss(true);
    boss.updateTalkingBubble("Review your sale in the overview and click confirm to complete the transaction.");
    boss.getChatBubble().addContinueButton(() -> onConfirmOverviewSeen(true));
  }

  private void onConfirmOverviewSeen(boolean isSellStep) {
    if (!isActive()) {
      return;
    }

    if (isSellStep && tutorialStep == STEP_SELL_CONFIRM) {
      tutorialStep = STEP_SELL_SUCCESS_VIEW;
      spotlight(TAB_CONTENT_X, TAB_CONTENT_Y, TAB_CONTENT_W, TAB_CONTENT_H);
      boss.visibleChatBubble(false);
    } else if (!isSellStep && tutorialStep == STEP_BUY_CONFIRM) {
      tutorialStep = STEP_BUY_SUCCESS;
      spotlight(TAB_CONTENT_X, TAB_CONTENT_Y, TAB_CONTENT_W, TAB_CONTENT_H);
      boss.visibleChatBubble(false);
    }
  }

  public void onBuySuccess() {
    if (!isActive() || tutorialStep != STEP_BUY_SUCCESS) {
      return;
    }

    boss.visibleBoss(true);
    boss.visibleChatBubble(spotlightActive);

    tutorialStep = STEP_HISTORY_TAB;
    spotlight(HISTORY_TAB_X, TAB_Y, HISTORY_TAB_W, TAB_H);
    boss.updateTalkingBubble("If you want more information about your stocks, go to the transaction history.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onTransactionHistoryViewed() {
    if (!isActive() || tutorialStep != STEP_HISTORY_TAB) {
      return;
    }

    tutorialStep = STEP_TRANSACTION_HISTORY;
    spotlight(TAB_CONTENT_X, TAB_CONTENT_Y, TAB_CONTENT_W, TAB_CONTENT_H);
    boss.updateTalkingBubble("Look through your transaction history.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onNextWeek() {
    if (!isActive() || tutorialStep != STEP_TRADE_TAB_2_VIEWED) {
      return;
    }

    tutorialStep = STEP_SELL_BUTTON - 1; // step 17 — boss rants, then continue leads to sell
    clearSpotlight();
    boss.updateTalkingBubble("What!? You didn't earn nearly enough money for me.");
    boss.getChatBubble().addContinueButton(this::onContinuePressed);
  }

  public void onSellSuccess() {
    if (!isActive() || tutorialStep != STEP_SELL_SUCCESS_VIEW) {
      return;
    }

    boss.visibleBoss(true);
    boss.visibleChatBubble(true);
    tutorialStep = STEP_SELL_SUCCESS;
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

    if (tutorialStep == STEP_START) {
      tutorialStep = STEP_NEWSPAPER_TAB;
      spotlight(NEWSPAPER_TAB_X, TAB_Y, NEWSPAPER_TAB_W, TAB_H);
      boss.updateTalkingBubble("You can start by reading the newspaper.");

    } else if (tutorialStep == STEP_NEWSPAPER_VIEW) {
      tutorialStep = STEP_TRADE_TAB;
      spotlight(TRADE_TAB_X, TAB_Y, TRADE_TAB_W, TAB_H);
      boss.updateTalkingBubble("With all that news you should have an idea of what to buy. Go to the trade screen and buy something.");

    // --- new steps ---
    } else if (tutorialStep == STEP_SEARCH_BAR) {
      tutorialStep = STEP_SECTOR_BUTTONS;
      spotlight(SECTOR_BUTTONS_X, SECTOR_BUTTONS_Y, SECTOR_BUTTONS_W, SECTOR_BUTTONS_H);
      boss.updateTalkingBubble("These buttons filter stocks by industry sector.");
      boss.getChatBubble().addContinueButton(this::onContinuePressed);

    } else if (tutorialStep == STEP_SECTOR_BUTTONS) {
      tutorialStep = STEP_MAX_BUY;
      spotlight(MAX_BUY_BUTTON_X, MAX_BUY_BUTTON_Y, MAX_BUY_BUTTON_W, MAX_BUY_BUTTON_H);
      boss.updateTalkingBubble("Hit Max Buy to automatically fill in the most shares you can afford. Don't waste my money.");
      boss.getChatBubble().addContinueButton(this::onContinuePressed);

    } else if (tutorialStep == STEP_MAX_BUY) {
      tutorialStep = STEP_BUY_BUTTON;
      boss.visibleBoss(false);
      spotlight(BUY_BUTTON_X, BUY_BUTTON_Y, BUY_BUTTON_W, BUY_BUTTON_H);
      boss.updateTalkingBubble("Now click the buy button.");

    // --- shifted steps (previously 8 → now STEP_BUY_SUCCESS / 11) ---
    } else if (tutorialStep == STEP_BUY_SUCCESS) {
      tutorialStep = STEP_HISTORY_TAB;
      spotlight(PORTFOLIO_TAB_X, TAB_Y, PORTFOLIO_TAB_W, TAB_H);
      boss.updateTalkingBubble("Or look at your portfolio to see what you own.");

    } else if (tutorialStep == STEP_TRANSACTION_HISTORY) {
      tutorialStep = STEP_PORTFOLIO_TAB;
      spotlight(TAB_CONTENT_X, TAB_CONTENT_Y, TAB_CONTENT_W, TAB_CONTENT_H);
      boss.updateTalkingBubble("Check your portfolio to see what you own.");
      boss.getChatBubble().addContinueButton(this::onContinuePressed);

    } else if (tutorialStep == STEP_PORTFOLIO_TAB) {
      tutorialStep = STEP_PORTFOLIO_VIEW;
      spotlight(TAB_CONTENT_X, TAB_CONTENT_Y, TAB_CONTENT_W, TAB_CONTENT_H);
      boss.updateTalkingBubble("Check your portfolio to see what you own.");
      boss.getChatBubble().addContinueButton(this::onContinuePressed);

    } else if (tutorialStep == STEP_PORTFOLIO_VIEW) {
      tutorialStep = STEP_TRADE_TAB_2;
      spotlight(TRADE_TAB_X, TAB_Y, TRADE_TAB_W, TAB_H);
      boss.updateTalkingBubble("Now go back to the trade screen.");

    } else if (tutorialStep == STEP_SELL_BUTTON - 1) {
      // boss rant step (was step 14 originally)
      tutorialStep = STEP_SELL_BUTTON;
      boss.visibleBoss(false);
      spotlight(SELL_BUTTON_X, SELL_BUTTON_Y, SELL_BUTTON_W, SELL_BUTTON_H);
      boss.updateTalkingBubble("Click the sell button.");

    } else if (tutorialStep == STEP_SELL_SUCCESS) {
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