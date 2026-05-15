package edu.ntnu.iir.bidata.idatt2003.group09.model.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.PortfolioRow;
import edu.ntnu.iir.bidata.idatt2003.group09.model.PlayerStatus;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for PortfolioScreen, exposing observable portfolio state for the view.
 */
public class PortfolioScreenModel {

    private final ObservableList<PortfolioRow> rows = FXCollections.observableArrayList();
    private final ObservableList<BigDecimal> chartValues = FXCollections.observableArrayList();

    private final ObjectProperty<BigDecimal> currentNetWorth = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<BigDecimal> previousNetWorth = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<BigDecimal> cash = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<PlayerStatus> status = new SimpleObjectProperty<>(null);

    public ObservableList<PortfolioRow> rowsProperty() {
        return rows;
    }

    public ObservableList<BigDecimal> chartValuesProperty() {
        return chartValues;
    }

    public ObjectProperty<BigDecimal> currentNetWorthProperty() {
        return currentNetWorth;
    }

    public ObjectProperty<BigDecimal> previousNetWorthProperty() {
        return previousNetWorth;
    }

    public ObjectProperty<BigDecimal> cashProperty() {
        return cash;
    }

    public ObjectProperty<PlayerStatus> statusProperty() {
        return status;
    }

    /**
     * Pulls current state from GameController and updates observable screen state.
     */
    public void updateFromGameState(GameController controller) {
        rows.setAll(buildRows(controller.getPortfolio().getShares()));
        chartValues.setAll(controller.getPortfolio().getValues());
        currentNetWorth.set(controller.getNetWorth());
        previousNetWorth.set(controller.getLastWeekNetWorth());
        cash.set(controller.getMoney());
        status.set(controller.getStatus());
    }

    private List<PortfolioRow> buildRows(List<Share> shares) {
        Map<String, Share> mergedShares = new HashMap<>();

        for (Share share : shares) {
            String symbol = share.getStock().getSymbol();
            if (mergedShares.containsKey(symbol)) {
                Share existing = mergedShares.get(symbol);
                BigDecimal totalQuantity = existing.getQuantity().add(share.getQuantity());
                BigDecimal totalCost = existing.getPurchasePrice().multiply(existing.getQuantity())
                    .add(share.getPurchasePrice().multiply(share.getQuantity()));
                BigDecimal avgPurchasePrice = totalCost.divide(totalQuantity, RoundingMode.HALF_UP);
                existing.setQuantity(totalQuantity);
                existing.setPurchasePrice(avgPurchasePrice);
            } else {
                mergedShares.put(symbol, new Share(share.getStock(), share.getQuantity(), share.getPurchasePrice()));
            }
        }

        return mergedShares.values().stream()
            .map(PortfolioRow::new)
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .toList();
    }
}