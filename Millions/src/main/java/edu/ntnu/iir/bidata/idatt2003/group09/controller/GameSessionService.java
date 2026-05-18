package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.io.EnhanceCSV;
import edu.ntnu.iir.bidata.idatt2003.group09.io.GameState;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import edu.ntnu.iir.bidata.idatt2003.group09.io.TagsFactory;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.PlayerStatus;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSessionService {

    private static final Logger LOGGER = Logger.getLogger(GameSessionService.class.getName());

    public record GameSession(GameController controller, List<Stock> stocks, boolean tutorialMode) {
    }

    public GameSession createTutorialSession(String playerName, String startingMoney) throws IOException {
        String normalizedSaveFileName = SaveManager.normalizeSaveFileName(playerName + "-tutorial");
        List<Stock> stocks = StockCsvReader.readFromResource("/csv/output/sp500.csv");

        BigDecimal startMoney = parseStartingMoney(startingMoney);
        Player player = new Player(playerName, startMoney, "Easy");
        Exchange exchange = new Exchange("S&P 500 Tutorial", stocks);

        PlayerStatus status = player.getStatus(0);
        exchange.setCommissionRate(getTutorialCommissionRate(status));

        GameController controller = new GameController(exchange, player, normalizedSaveFileName);
        controller.saveGame();

        return new GameSession(controller, stocks, true);
    }

    public GameSession createNewSession(String playerName, String experienceLevel, String exchangeChoice, String startingMoney)
        throws IOException {
        String normalizedSaveFileName = SaveManager.normalizeSaveFileName(playerName);
        List<Stock> stocks = loadStocksForExchange(exchangeChoice);
        BigDecimal startMoney = parseStartingMoney(startingMoney);
        Player player = new Player(playerName, startMoney, experienceLevel);
        Exchange exchange = new Exchange(getExchangeName(exchangeChoice), stocks);

        exchange.setCommissionRate(getDifficultyCommissionRate(experienceLevel));

        GameController controller = new GameController(exchange, player, normalizedSaveFileName);
        controller.saveGame();

        return new GameSession(controller, stocks, false);
    }

    public Optional<GameSession> loadSession(String fileName) {
        GameState state = SaveManager.load(fileName);
        if (state == null) {
            return Optional.empty();
        }

        String normalizedSaveFileName = SaveManager.normalizeSaveFileName(fileName);
        GameController controller = new GameController(
            state.getExchange(),
            state.getPlayer(),
            normalizedSaveFileName,
            state.getProgress()
        );

        return Optional.of(new GameSession(controller, state.getExchange().getStocks(), false));
    }

    private BigDecimal parseStartingMoney(String money) {
        try {
            return new BigDecimal(money);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Invalid starting money input: {0}. Using default.", money);
            return new BigDecimal("100000");
        }
    }

    private BigDecimal getTutorialCommissionRate(PlayerStatus status) {
        return switch (status) {
            case NOVICE -> new BigDecimal("0.005");
            case INVESTOR -> new BigDecimal("0.003");
            case SPECULATOR -> new BigDecimal("0.001");
        };
    }

    private BigDecimal getDifficultyCommissionRate(String experienceLevel) {
        if ("Medium".equalsIgnoreCase(experienceLevel)) {
            return new BigDecimal("0.01");
        }
        if ("Hard".equalsIgnoreCase(experienceLevel)) {
            return new BigDecimal("0.02");
        }
        return new BigDecimal("0.005");
    }

    private List<Stock> loadStocksForExchange(String exchangeChoice) throws IOException {
        if (exchangeChoice == null) {
            return StockCsvReader.readDefaultResource();
        }

        String trimmed = exchangeChoice.trim().toLowerCase();
        if (trimmed.equals("random")) {
            return StockCsvReader.readFromResource("/csv/output/random.csv");
        } else if (trimmed.equals("sp500")) {
            return StockCsvReader.readFromResource("/csv/output/sp500.csv");
        } else if (trimmed.startsWith("custom:")) {
            String filePath = trimmed.substring("custom:".length());
            Path selectedCsvFile = Path.of(filePath);
            Path enhancedCsv = enhanceCustomCsv(selectedCsvFile);
            return StockCsvReader.readFromFile(enhancedCsv);
        } else {
            return StockCsvReader.readDefaultResource();
        }
    }

    private String getExchangeName(String exchangeChoice) {
        if (exchangeChoice == null) {
            return "Main Exchange";
        }

        return switch (exchangeChoice.trim().toLowerCase()) {
            case "random" -> "Random Exchange";
            case "sp500" -> "S&P 500";
            default -> exchangeChoice.trim().toLowerCase().startsWith("custom:")
                ? "Custom Exchange"
                : "Main Exchange";
        };
    }

    private Path enhanceCustomCsv(Path selectedCsvFile) throws IOException {
        Path enhancedFile = Files.createTempFile("millions-custom-enhanced-", ".csv");
        EnhanceCSV enhancer = new EnhanceCSV(selectedCsvFile.toString(), new TagsFactory().getTags());
        enhancer.writeEnhancedCsv(enhancedFile.toString());
        enhancedFile.toFile().deleteOnExit();
        return enhancedFile;
    }
}
