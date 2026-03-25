package edu.ntnu.iir.bidata.idatt2003.group09.io;

import static org.junit.jupiter.api.Assertions.*;

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

        assertNotNull(first.getSector());
        assertFalse(first.getSector().isBlank());
        assertTrue(first.getRisk() >= 1 && first.getRisk() <= 7);
    }

    @Test
    void readFromResourceShouldThrowWhenResourceMissing() {
        assertThrows(IOException.class,
                () -> StockCsvReader.readFromResource("/does-not-exist.csv"));
    }

    @Test
    void readFromFileShouldParseValidCsv() throws IOException {
        Path csvFile = tempDir.resolve("stocks.csv");

        Files.writeString(csvFile,
                "AAPL,Apple Inc,123.45,Technology,4\n" +
                        "MSFT,Microsoft,300.00,Technology,3\n");

        List<Stock> stocks = StockCsvReader.readFromFile(csvFile);

        assertEquals(2, stocks.size());

        Stock first = stocks.get(0);
        assertEquals("AAPL", first.getSymbol());
        assertEquals("Apple Inc", first.getCompany());
        assertEquals(0, new BigDecimal("123.45").compareTo(first.getSalesPrice()));
        assertEquals("Technology", first.getSector());
        assertEquals(4, first.getRisk());

        Stock second = stocks.get(1);
        assertEquals("MSFT", second.getSymbol());
        assertEquals("Microsoft", second.getCompany());
        assertEquals(3, second.getRisk());
    }

    @Test
    void readFromFileShouldSkipCommentsAndBlankLines() throws IOException {
        Path csvFile = tempDir.resolve("stocks-with-comments.csv");

        Files.writeString(csvFile,
                "# this is a comment\n\n" +
                        "GOOG,Alphabet,250.10,Communication Services,4\n");

        List<Stock> stocks = StockCsvReader.readFromFile(csvFile);

        assertEquals(1, stocks.size());
        assertEquals("GOOG", stocks.getFirst().getSymbol());
    }

    @Test
    void readFromFileShouldThrowOnMalformedCsvLine() throws IOException {
        Path csvFile = tempDir.resolve("malformed.csv");

        Files.writeString(csvFile, "AAPL,Apple Inc,123.45\n");

        assertThrows(IOException.class,
                () -> StockCsvReader.readFromFile(csvFile));
    }

    @Test
    void readFromFileShouldThrowOnInvalidStockData() throws IOException {
        Path csvFile = tempDir.resolve("invalid-stock.csv");

        Files.writeString(csvFile,
                "AAPL,Apple Inc,-1.00,Technology,4\n");

        assertThrows(IOException.class,
                () -> StockCsvReader.readFromFile(csvFile));
    }

    @Test
    void readFromFileShouldThrowOnInvalidRiskFormat() throws IOException {
        Path csvFile = tempDir.resolve("invalid-risk.csv");

        Files.writeString(csvFile,
                "AAPL,Apple Inc,123.45,Technology,abc\n");

        assertThrows(IOException.class,
                () -> StockCsvReader.readFromFile(csvFile));
    }

    @Test
    void readFromFileShouldThrowOnRiskOutOfBounds() throws IOException {
        Path csvFile = tempDir.resolve("invalid-risk-range.csv");

        Files.writeString(csvFile,
                "AAPL,Apple Inc,123.45,Technology,10\n");

        assertThrows(IOException.class,
                () -> StockCsvReader.readFromFile(csvFile));
    }

    @Test
    void readFromFileShouldThrowOnEmptySector() throws IOException {
        Path csvFile = tempDir.resolve("invalid-sector.csv");

        Files.writeString(csvFile,
                "AAPL,Apple Inc,123.45,,4\n");

        assertThrows(IOException.class,
                () -> StockCsvReader.readFromFile(csvFile));
    }

    @Test
    void readFromFileShouldHandleMultipleValidLines() throws IOException {
        Path csvFile = tempDir.resolve("many-stocks.csv");

        Files.writeString(csvFile,
                "AAPL,Apple Inc,100.00,Technology,4\n" +
                        "MSFT,Microsoft,200.00,Technology,3\n" +
                        "KO,Coca-Cola,50.00,Consumer Staples,1\n");

        List<Stock> stocks = StockCsvReader.readFromFile(csvFile);

        assertEquals(3, stocks.size());

        assertEquals("KO", stocks.get(2).getSymbol());
        assertEquals("Consumer Staples", stocks.get(2).getSector());
        assertEquals(1, stocks.get(2).getRisk());
    }

    @Test
    void readFromFileShouldThrowOnNullPath() {
        assertThrows(NullPointerException.class,
                () -> StockCsvReader.readFromFile(null));
    }

    @Test
    void readFromResourceShouldThrowOnNullPath() {
        assertThrows(NullPointerException.class,
                () -> StockCsvReader.readFromResource(null));
    }
}