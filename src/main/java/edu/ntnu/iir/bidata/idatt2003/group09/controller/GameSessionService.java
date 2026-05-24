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
import java.util.Random;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Locale;

/**
 * Tjenesteklasse som oppretter og laster spillsesjoner.
 */
public class GameSessionService {

    private static final Logger LOGGER = Logger.getLogger(GameSessionService.class.getName());

    /**
     * Holder det som trengs for å vise en aktiv spillsesjon i UI-et.
     *
     * @param controller spillkontrolleren for sesjonen
     * @param stocks aksjelisten som brukes i visningene
     * @param tutorialMode om sesjonen kjører i veiledningsmodus
     */
    public record GameSession(GameController controller, List<Stock> stocks, boolean tutorialMode) {
    }

    /**
     * Oppretter en ny tutorialsesjon med standard data og lagrer den med en gang.
     *
     * @param playerName navn på spilleren
     * @param startingMoney startkapital oppgitt av brukeren
     * @return en ferdig konfigurert spillsesjon i tutorialmodus
     * @throws IOException hvis aksjedata ikke kan leses
     */
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

    /**
     * Oppretter en ny ordinær spillsesjon basert på valgene fra opprett-skjermen.
     *
     * @param playerName navn på spilleren
     * @param experienceLevel valgt vanskelighetsgrad
     * @param exchangeChoice valgt marked eller kilde for aksjedata
     * @param startingMoney startkapital oppgitt av brukeren
     * @return en ferdig konfigurert spillsesjon
     * @throws IOException hvis aksjedata ikke kan leses eller behandles
     */
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

    /**
     * Laster en tidligere lagret spillsesjon.
     *
     * @param fileName navn på lagringsfilen som skal leses
     * @return en valgfri spillsesjon; tom hvis filen ikke inneholder gyldig spilltilstand
     */
    public Optional<GameSession> loadSession(String fileName) {
        /**
         * Load a previously saved session using `SaveManager`. Returns an empty
         * optional when the file contained invalid data or could not be read.
         *
         * @param fileName the filename to load
         * @return optional game session
         */
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
        /**
         * Resolve an exchange choice to a list of `Stock` instances. Supports
         * built-in resources (`sp500`, `random`) and `custom:<path>` which
         * will be enhanced and read from a user-provided CSV file.
         *
         * @param exchangeChoice identifier or custom:path
         * @return list of Stock
         * @throws IOException when reading or enhancing custom CSV fails
         */
        if (exchangeChoice == null) {
            return StockCsvReader.readDefaultResource();
        }

        String trimmed = exchangeChoice.trim().toLowerCase();
        if (trimmed.equals("random")) {
            // Read the bundled random CSV as stocks, then shuffle and perturb in-memory
            List<Stock> base = StockCsvReader.readFromResource("/csv/output/random.csv");
            // Perturb prices and shuffle
            Random r = new Random();
            for (Stock s : base) {
                try {
                    java.math.BigDecimal p = s.getSalesPrice();
                    double price = p.doubleValue();
                    double factor = 1.0 + ((r.nextDouble() * 2.0 - 1.0) * 0.2);
                    double newPrice = Math.max(0.01, price * factor);
                    java.math.BigDecimal newBd = new java.math.BigDecimal(String.format(Locale.ROOT, "%.2f", newPrice));
                    s.addNewSalesPrice(newBd);
                } catch (Exception ignored) {
                }
            }
            Collections.shuffle(base, r);
            return base;
        } else if (trimmed.equals("sp500")) {
            return StockCsvReader.readFromResource("/csv/output/sp500.csv");
        } else if (trimmed.startsWith("custom:")) {
            String filePath = trimmed.substring("custom:".length());
            Path selectedCsvFile = Path.of(filePath);
            // Store uploaded custom CSV in user app data for reuse
            String appData = System.getenv("APPDATA");
            if (appData == null) appData = System.getProperty("user.home");
            java.io.File importDir = new java.io.File(appData, "Millions/imports");
            if (!importDir.exists()) importDir.mkdirs();
            String safeName = selectedCsvFile.getFileName().toString().replaceAll("[^a-zA-Z0-9._-]", "_");
            Path stored = importDir.toPath().resolve(safeName);
            Files.copy(selectedCsvFile, stored, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            Path enhancedCsv = enhanceCustomCsv(stored);
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
        // Randomize custom uploads per-game: shuffle rows and perturb prices
        enhancer.setShuffle(true);
        enhancer.setPerturbPrices(true);
        enhancer.writeEnhancedCsv(enhancedFile.toString());
        enhancedFile.toFile().deleteOnExit();
        return enhancedFile;
    }
}
