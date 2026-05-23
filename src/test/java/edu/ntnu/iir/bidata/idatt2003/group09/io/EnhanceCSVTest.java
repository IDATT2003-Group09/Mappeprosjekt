package edu.ntnu.iir.bidata.idatt2003.group09.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class EnhanceCSVTest {

	@TempDir
	Path tempDir;

	@Test
	void constructorShouldParseDataLinesAndTags() throws IOException {
		Path inputFile = tempDir.resolve("input.csv");
		Files.writeString(
				inputFile,
				"# Stocks for test\n"
						+ "# Symbol,Company,Price\n"
						+ "AAPL,Apple,123.45\n"
						+ "\n"
						+ "MSFT,Microsoft,300.00\n");

		EnhanceCSV enhancer = new EnhanceCSV(inputFile.toString(), List.of("Technology", "Finance"));

		assertEquals(2, enhancer.getDataLineCount());
		assertEquals(2, enhancer.getTagCount());
	}

	@Test
	void addAvailableTagShouldIncreaseTagCount() throws IOException {
		EnhanceCSV enhancer = createEnhancerWithSingleDataLine(List.of("Tech"));

		enhancer.addAvailableTag("Energy");

		assertEquals(2, enhancer.getTagCount());
		assertTrue(enhancer.getTags().contains("Energy"));
	}

	@Test
	void addAvailableTagsShouldIncreaseTagCount() throws IOException {
		EnhanceCSV enhancer = createEnhancerWithSingleDataLine(List.of("Tech"));

		enhancer.addAvailableTags(new String[] {"Energy", "Utilities"});

		assertEquals(3, enhancer.getTagCount());
		assertTrue(enhancer.getTags().contains("Energy"));
		assertTrue(enhancer.getTags().contains("Utilities"));
	}

	@Test
	void clearTagsShouldRemoveAllTags() throws IOException {
		EnhanceCSV enhancer = createEnhancerWithSingleDataLine(List.of("Tech", "Finance"));

		enhancer.clearTags();

		assertEquals(0, enhancer.getTagCount());
		assertTrue(enhancer.getTags().isEmpty());
	}

	@Test
	void getTagsShouldReturnDefensiveCopy() throws IOException {
		EnhanceCSV enhancer = createEnhancerWithSingleDataLine(List.of("Tech"));

		List<String> copy = enhancer.getTags();
		copy.add("Mutated");

		assertEquals(1, enhancer.getTagCount());
		assertFalse(enhancer.getTags().contains("Mutated"));
	}

	@Test
	void setMaxVolatilityShouldUpdateMaxVolatility() throws IOException {
		EnhanceCSV enhancer = createEnhancerWithSingleDataLine(List.of("Tech"));

		enhancer.setMaxVolatility(4);

		assertEquals(4, enhancer.getMaxVolatility());
	}

	@Test
	void setMaxVolatilityShouldThrowForNonPositiveValues() throws IOException {
		EnhanceCSV enhancer = createEnhancerWithSingleDataLine(List.of("Tech"));

		assertThrows(IllegalArgumentException.class, () -> enhancer.setMaxVolatility(0));
		assertThrows(IllegalArgumentException.class, () -> enhancer.setMaxVolatility(-1));
	}

	@Test
	void writeEnhancedCsvShouldPreserveCommentsAndEnhanceData() throws IOException {
		Path inputFile = tempDir.resolve("stocks.csv");
		Files.writeString(
				inputFile,
				"# Test dataset\n"
						+ "# Symbol,Company,Price\n"
						+ "AAPL,Apple,123.45\n"
						+ "MSFT,Microsoft,300.00\n");

		EnhanceCSV enhancer = new EnhanceCSV(inputFile.toString(), List.of("FixedTag"));
		enhancer.setMaxVolatility(3);

		Path outputFile = tempDir.resolve("enhanced.csv");
		enhancer.writeEnhancedCsv(outputFile.toString());

		List<String> lines = Files.readAllLines(outputFile);
		assertEquals(5, lines.size());
		assertEquals("# Test dataset", lines.get(0));
		assertEquals("# Symbol,Company,Price", lines.get(1));
		assertEquals("Symbol,Company,Price,Tag,Volatility", lines.get(2));

		assertEnhancedLine(lines.get(3), "AAPL,Apple,123.45", "FixedTag", 3);
		assertEnhancedLine(lines.get(4), "MSFT,Microsoft,300.00", "FixedTag", 3);
	}

	@Test
	void writeEnhancedCsvShouldWriteDefaultHeaderWhenNoCommentHeader() throws IOException {
		Path inputFile = tempDir.resolve("stocks-no-header.csv");
		Files.writeString(inputFile, "AAPL,Apple,123.45\n");

		EnhanceCSV enhancer = new EnhanceCSV(inputFile.toString(), new ArrayList<>());
		enhancer.setMaxVolatility(2);

		Path outputFile = tempDir.resolve("enhanced-no-header.csv");
		enhancer.writeEnhancedCsv(outputFile.toString());

		List<String> lines = Files.readAllLines(outputFile);
		assertEquals(2, lines.size());
		assertEquals("Tag,Volatility", lines.get(0));

		String[] parts = lines.get(1).split(",", -1);
		assertEquals(5, parts.length);
		assertEquals("AAPL", parts[0]);
		assertEquals("Apple", parts[1]);
		assertEquals("123.45", parts[2]);
		assertEquals("", parts[3]);

		int volatility = Integer.parseInt(parts[4]);
		assertTrue(volatility >= 1 && volatility <= 2);
	}

	private EnhanceCSV createEnhancerWithSingleDataLine(List<String> tags) throws IOException {
		Path inputFile = tempDir.resolve("single-line.csv");
		Files.writeString(inputFile, "AAPL,Apple,123.45\n");
		return new EnhanceCSV(inputFile.toString(), tags);
	}

	private void assertEnhancedLine(
			String line, String expectedPrefix, String expectedTag, int maxVolatilityValue) {
		String[] parts = line.split(",", -1);
		String[] expectedPrefixParts = expectedPrefix.split(",", -1);

		assertEquals(expectedPrefixParts.length + 2, parts.length);
		for (int i = 0; i < expectedPrefixParts.length; i++) {
			assertEquals(expectedPrefixParts[i], parts[i]);
		}

		assertEquals(expectedTag, parts[parts.length - 2]);
		int volatility = Integer.parseInt(parts[parts.length - 1]);
		assertTrue(volatility >= 1 && volatility <= maxVolatilityValue);
	}
}
