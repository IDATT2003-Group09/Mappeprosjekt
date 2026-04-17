package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.TradeScreenView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class TradeScreenController {
    private final GameController controller;
    private final Runnable onSaveAndQuit;
    private final boolean tutorialMode;
    private final TutorialOverlay tutorialOverlay;
    private final NumberFormat currencyFormat;

    private ObservableList<Stock> allStocks;
    private ObservableList<Stock> filteredStocks;
    private Set<String> selectedSectors;
    private int lastLevel = 1;

    private TradeScreenView view;

    public TradeScreenController(GameController controller, List<Stock> stocks, 
                                 Runnable onSaveAndQuit, boolean tutorialMode, 
                                 TutorialOverlay tutorialOverlay) {
        this.controller = controller;
        this.onSaveAndQuit = onSaveAndQuit;
        this.tutorialMode = tutorialMode;
        this.tutorialOverlay = tutorialOverlay;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        
        this.allStocks = FXCollections.observableArrayList(stocks);
        this.filteredStocks = FXCollections.observableArrayList(allStocks);
    }

    public void setView(TradeScreenView view) {
        this.view = view;
    }

    public GameController getController() {
        return controller;
    }

    public NumberFormat getCurrencyFormat() {
        return currencyFormat;
    }

    public boolean isTutorialMode() {
        return tutorialMode;
    }

    public TutorialOverlay getTutorialOverlay() {
        return tutorialOverlay;
    }

    public ObservableList<Stock> getFilteredStocks() {
        return filteredStocks;
    }

    public ObservableList<Stock> getAllStocks() {
        return allStocks;
    }

    public Set<String> getSelectedSectors() {
        return selectedSectors;
    }

    public void setSelectedSectors(Set<String> selectedSectors) {
        this.selectedSectors = selectedSectors;
    }

    public void filterStockList(String searchText, Set<String> selectedSectors) {
        List<Stock> filteredBySector = allStocks.stream()
            .filter(stock -> {
                if (selectedSectors == null || selectedSectors.isEmpty()) {
                    return true;
                }
                String stockSector = stock.getSector();
                return stockSector != null && selectedSectors.contains(stockSector);
            })
            .collect(Collectors.toList());
        
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredStocks.setAll(filteredBySector);
            return;
        }

        String lowerCaseSearch = searchText.toLowerCase().trim();
        List<Stock> filtered = filteredBySector.stream()
            .filter(stock -> 
                stock.getSymbol().toLowerCase().contains(lowerCaseSearch) ||
                stock.getCompany().toLowerCase().contains(lowerCaseSearch)
            )
            .collect(Collectors.toList());
        
        filteredStocks.setAll(filtered);
    }

    public void filterBySectors(String searchText, Set<String> selectedSectors) {
        List<Stock> filteredBySector = allStocks.stream()
            .filter(stock -> {
                if (selectedSectors.isEmpty()) {
                    return true;
                }
                String stockSector = stock.getSector();
                return stockSector != null && selectedSectors.contains(stockSector);
            })
            .collect(Collectors.toList());
        
        if (searchText != null && !searchText.trim().isEmpty()) {
            String lowerCaseSearch = searchText.toLowerCase().trim();
            filteredBySector = filteredBySector.stream()
                .filter(stock -> 
                    stock.getSymbol().toLowerCase().contains(lowerCaseSearch) ||
                    stock.getCompany().toLowerCase().contains(lowerCaseSearch)
                )
                .collect(Collectors.toList());
        }

        filteredStocks.setAll(filteredBySector);
    }

    public void buySelectedStock(Stock selectedStock, String quantityText) throws Exception {
        if (selectedStock == null) {
            throw new IllegalArgumentException("Please select a stock first.");
        }
        
        BigDecimal quantity = parseQuantity(quantityText);
        BigDecimal price = selectedStock.getSalesPrice();
        BigDecimal commissionRate = controller.getExchange().getCommissionRate();
        Share tempShare = new Share(selectedStock, quantity, price);
        PurchaseCalculator calc = new PurchaseCalculator(tempShare, commissionRate);
        BigDecimal commission = calc.calculateCommission();
        BigDecimal tax = calc.calculateTax();
        BigDecimal total = calc.calculateTotal();

        view.showTransactionOverlay("Buy", selectedStock.getSymbol(), quantity, price, 
                                    commission, tax, total, () -> {
            controller.buy(selectedStock.getSymbol(), quantity);
            view.setStatusText("Bought " + quantity + " of " + selectedStock.getSymbol());
            onTutorialBuySuccess();
            view.refreshStockList();
            refreshInfo();
        });
    }

    public void sellSelectedStock(Stock selectedStock, String quantityText) throws Exception {
        if (selectedStock == null) {
            throw new IllegalArgumentException("Please select a stock first.");
        }
        
        List<Share> shares = controller.getPortfolio().getShares(selectedStock.getSymbol());
        if (shares.isEmpty()) {
            throw new IllegalArgumentException("You do not own this stock.");
        }
        
        BigDecimal quantity = parseQuantity(quantityText);
        BigDecimal price = selectedStock.getSalesPrice();
        BigDecimal commissionRate = controller.getExchange().getCommissionRate();
        Share tempShare = new Share(selectedStock, quantity, price);
        PurchaseCalculator calc = new PurchaseCalculator(tempShare, commissionRate);
        BigDecimal commission = calc.calculateCommission();
        BigDecimal tax = calc.calculateTax();
        BigDecimal total = calc.calculateTotal();

        view.showTransactionOverlay("Sell", selectedStock.getSymbol(), quantity, price, 
                                    commission, tax, total, () -> {
            controller.sell(selectedStock.getSymbol(), quantity);
            view.setStatusText("Sold " + quantity.stripTrailingZeros().toPlainString()
                + " of " + selectedStock.getSymbol());
            onTutorialSellSuccess();
            view.refreshStockList();
            refreshInfo();
        });
    }

    public BigDecimal calculateMaxBuyQuantity(Stock selectedStock) {
        if (selectedStock == null) return BigDecimal.ZERO;
        
        BigDecimal price = selectedStock.getSalesPrice();
        BigDecimal cash = controller.getMoney();
        if (price.compareTo(BigDecimal.ZERO) <= 0 || cash.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal commissionRate = controller.getExchange().getCommissionRate();
        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = cash.divide(price, 0, RoundingMode.FLOOR).add(BigDecimal.ONE);
        BigDecimal best = BigDecimal.ZERO;
        
        while (low.compareTo(high) < 0) {
            BigDecimal mid = low.add(high).divide(new BigDecimal("2"), 0, RoundingMode.FLOOR);
            if (mid.compareTo(BigDecimal.ZERO) <= 0) {
                low = mid.add(BigDecimal.ONE);
                continue;
            }
            Share tempShare = new Share(selectedStock, mid, price);
            PurchaseCalculator calc = new PurchaseCalculator(tempShare, commissionRate);
            BigDecimal totalCost = calc.calculateTotal();
            if (totalCost.compareTo(cash) <= 0) {
                best = mid;
                low = mid.add(BigDecimal.ONE);
            } else {
                high = mid;
            }
        }
        return best;
    }

    private BigDecimal parseQuantity(String quantityText) {
        try {
            BigDecimal quantity = new BigDecimal(quantityText.trim());
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantity must be > 0");
            }
            return quantity;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number");
        }
    }

    public void refreshInfo() {
        if (view == null) return;
        
        var progress = controller.getProgress();
        var player = controller.getPlayer();
        
        int week = controller.getWeek();
        int currentQuarter = (week / 13) + 1;
        BigDecimal requirement = progress.getCurrentTarget();
        BigDecimal netWorth = player.getNetWorth();
        
        double progressValue = 0;
        if (requirement.compareTo(BigDecimal.ZERO) > 0) {
            progressValue = netWorth
                .divide(requirement, 4, RoundingMode.HALF_UP)
                .doubleValue();
        }
        progressValue = Math.max(0, Math.min(progressValue, 1.0));
        
        int checkpointLevel = progress.getCheckpointLevel();
        boolean levelUp = checkpointLevel > lastLevel;
        if (levelUp) {
            lastLevel = checkpointLevel;
        }
        
        view.updateInfo(
            controller.getMoney(),
            controller.getPortfolio().getShares().size(),
            controller.getNetWorth(),
            week,
            progress.getWeeksUntilDeadline(),
            currentQuarter,
            requirement,
            netWorth,
            progressValue,
            levelUp ? checkpointLevel : -1
        );
    }

    public void nextWeek() {
        controller.nextWeek();
        if (view != null) {
            view.refreshStockList();
            refreshInfo();
            view.updateSelectedStockGraph();
            onTutorialNextWeek();
        }
    }

    public Set<String> getAllSectors(List<Stock> stocks) {
        return stocks.stream()
            .map(Stock::getSector)
            .filter(sector -> sector != null && !sector.isEmpty())
            .collect(Collectors.toSet());
    }

    private void onTutorialStockSelected() {
        if (tutorialMode && tutorialOverlay != null) {
            tutorialOverlay.onStockSelected();
        }
    }

    private void onTutorialBuySuccess() {
        if (tutorialMode && tutorialOverlay != null) {
            tutorialOverlay.onBuySuccess();
        }
    }

    private void onTutorialNextWeek() {
        if (tutorialMode && tutorialOverlay != null) {
            tutorialOverlay.onNextWeek();
        }
    }

    private void onTutorialSellSuccess() {
        if (tutorialMode && tutorialOverlay != null) {
            tutorialOverlay.onSellSuccess();
        }
    }

    public void onStockSelected() {
        onTutorialStockSelected();
    }

    public void onBuyButtonClicked() {
        if (tutorialMode && tutorialOverlay != null) {
            tutorialOverlay.onBuyButtonClicked();
        }
    }

    public void onSellButtonClicked() {
        if (tutorialMode && tutorialOverlay != null) {
            tutorialOverlay.onSellButtonClicked();
        }
    }
}