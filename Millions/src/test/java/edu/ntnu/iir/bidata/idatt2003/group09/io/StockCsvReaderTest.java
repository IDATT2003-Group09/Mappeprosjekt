package edu.ntnu.iir.bidata.idatt2003.group09.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.iir.bidata.idatt2003.group09.Stock;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class StockCsvReaderTest {

  @TempDir
  Path tempDir;

  @Test
  void readFromResourceShouldReturnStocks() throws IOException {
    List<Stock> stocks = StockCsvReader.readFromResource("/sp500.csv");

    assertFalse(stocks.isEmpty());
    Stock first = stocks.getFirst();
    assertFalse(first.getSymbol().isBlank());
    assertFalse(first.getCompany().isBlank());
    assertTrue(first.getSalesPrice().compareTo(BigDecimal.ZERO) > 0);
  }

  @Test
  void readFromResourceShouldThrowWhenResourceMissing() {
    assertThrows(IOException.class, () -> StockCsvReader.readFromResource("/does-not-exist.csv"));
  }

  @Test
  void readFromFileShouldParseValidCsv() throws IOException {
    Path csvFile = tempDir.resolve("stocks.csv");
    Files.writeString(csvFile, "AAPL,Apple Inc,123.45\nMSFT,Microsoft,300.00\n");

    List<Stock> stocks = StockCsvReader.readFromFile(csvFile);

    assertEquals(2, stocks.size());
    assertEquals("AAPL", stocks.get(0).getSymbol());
    assertEquals("Apple Inc", stocks.get(0).getCompany());
    assertEquals(0, new BigDecimal("123.45").compareTo(stocks.get(0).getSalesPrice()));
    assertEquals("MSFT", stocks.get(1).getSymbol());
  }

  @Test
  void readFromFileShouldSkipCommentsAndBlankLines() throws IOException {
    Path csvFile = tempDir.resolve("stocks-with-comments.csv");
    Files.writeString(csvFile, "# this is a comment\n\nGOOG,Alphabet,250.10\n");

    List<Stock> stocks = StockCsvReader.readFromFile(csvFile);

    assertEquals(1, stocks.size());
    assertEquals("GOOG", stocks.getFirst().getSymbol());
  }

  @Test
  void readFromFileShouldThrowOnMalformedCsvLine() throws IOException {
    Path csvFile = tempDir.resolve("malformed.csv");
    Files.writeString(csvFile, "AAPL,Apple Inc\n");

    assertThrows(IOException.class, () -> StockCsvReader.readFromFile(csvFile));
  }

  @Test
  void readFromFileShouldThrowOnInvalidStockData() throws IOException {
    Path csvFile = tempDir.resolve("invalid-stock.csv");
    Files.writeString(csvFile, "AAPL,Apple Inc,-1.00\n");

    assertThrows(IOException.class, () -> StockCsvReader.readFromFile(csvFile));
  }

  @Test
  void readFromFileShouldThrowOnNullPath() {
    assertThrows(NullPointerException.class, () -> StockCsvReader.readFromFile(null));
  }

  @Test
  void readFromResourceShouldThrowOnNullPath() {
    assertThrows(NullPointerException.class, () -> StockCsvReader.readFromResource(null));
  }
}
