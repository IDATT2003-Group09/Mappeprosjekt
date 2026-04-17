package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

/**
 * Reads stock data from a CSV file and converts each row into a {@link Stock}.
 */
public final class StockCsvReader {

  private static final Path DEFAULT_FILE = Path.of("src/main/resources/csv/output/sp500.csv");

  private StockCsvReader() {
  }

  /**
   * Reads stocks from the default resource.
   *
   * @return a list of parsed stocks
   * @throws IOException if the resource cannot be read
   */
  public static List<Stock> readDefaultResource() throws IOException {
    return readFromFile(DEFAULT_FILE);
  }

  /**
   * Reads stocks from a classpath resource.
   *
   * @param resourcePath resource path such as sp500.csv}
   * @return a list of parsed stocks
   * @throws IOException if the resource cannot be opened or read
   */
  public static List<Stock> readFromResource(String resourcePath) throws IOException {
    Objects.requireNonNull(resourcePath, "resourcePath cannot be null");

    String normalizedResourcePath = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
    InputStream inputStream = StockCsvReader.class.getResourceAsStream(normalizedResourcePath);

    if (inputStream == null && "/sp500.csv".equals(normalizedResourcePath)) {
      inputStream = StockCsvReader.class.getResourceAsStream("/csv/output/sp500.csv");
    }

    if (inputStream == null) {
      throw new IOException("Resource not found: " + normalizedResourcePath);
    }

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      return parse(reader);
    }
  }

  /**
   * Reads stocks from a filesystem path.
   *
   * @param filePath path to a CSV file
   * @return a list of parsed stocks
   * @throws IOException if the file cannot be read
   */
  public static List<Stock> readFromFile(Path filePath) throws IOException {
    Objects.requireNonNull(filePath, "filePath cannot be null");

    try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
      return parse(reader);
    }
  }

  private static List<Stock> parse(BufferedReader reader) throws IOException {
    List<Stock> stocks = new ArrayList<>();
    String line;
    int lineNumber = 0;

    while ((line = reader.readLine()) != null) {
      lineNumber++;
      String trimmedLine = line.trim();

      if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
        continue;
      }

      if (trimmedLine.toLowerCase().startsWith("ticker,")) {
        continue;
      }

      String[] parts = trimmedLine.split(",", 5);
      if (parts.length != 5) {
        throw new IOException("Invalid CSV format on line " + lineNumber + ": " + line);
      }

      String symbol = parts[0].trim();
      String company = parts[1].trim();
      String priceText = parts[2].trim();
      String sector = parts[3].trim();
      String riskText = parts[4].trim();

      int risk;
      try {
          risk = Integer.parseInt(riskText);
      } catch (NumberFormatException e) {
          throw new IOException("Invalid risk value on line " + lineNumber + ": " + line, e);
      }

      try {
        stocks.add(new Stock(symbol, company, new BigDecimal(priceText), sector, risk));
      } catch (IllegalArgumentException ex) {
        throw new IOException("Invalid stock data on line " + lineNumber + ": " + line, ex);
      }
    }

    return stocks;
  }
}
