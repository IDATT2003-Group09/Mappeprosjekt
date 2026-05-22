package edu.ntnu.iir.bidata.idatt2003.group09.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TagsFactoryTest {

	@TempDir
	Path tempDir;

	@Test
	void getTagsShouldReturnAllDefaultTags() {
		TagsFactory factory = new TagsFactory();

		List<String> tags = factory.getTags();

		assertEquals(11, tags.size());
		assertTrue(tags.contains("Technology"));
		assertTrue(tags.contains("Financials"));
		assertTrue(tags.contains("Real Estate"));
	}

	@Test
	void getTagsShouldReturnDefensiveCopy() {
		TagsFactory factory = new TagsFactory();

		List<String> tags = factory.getTags();
		tags.clear();

		assertEquals(11, factory.getTags().size());
	}

	@Test
	void enhanceCsvShouldWriteEnhancedFileToConfiguredPaths() throws Exception {
		Path inputDir = tempDir.resolve("input");
		Path outputDir = tempDir.resolve("output");
		Files.createDirectories(inputDir);
		Files.createDirectories(outputDir);

		Path inputFile = inputDir.resolve("stocks.csv");
		Files.writeString(
				inputFile,
				"# Test data\n"
						+ "# Symbol,Company,Price\n"
						+ "AAPL,Apple,123.45\n");

		TagsFactory factory = new TagsFactory();
		setField(factory, "inputFilePath", inputDir.toString() + java.io.File.separator);
		setField(factory, "outputFilePath", outputDir.toString() + java.io.File.separator);

		factory.enhanceCsv("stocks.csv", "enhanced.csv");

		Path outputFile = outputDir.resolve("enhanced.csv");
		assertTrue(Files.exists(outputFile));

		List<String> lines = Files.readAllLines(outputFile);
		assertEquals(4, lines.size());
		assertEquals("# Test data", lines.get(0));
		assertEquals("# Symbol,Company,Price", lines.get(1));
		assertEquals("Symbol,Company,Price,Tag,Volatility", lines.get(2));

		String[] dataParts = lines.get(3).split(",", -1);
		assertEquals(5, dataParts.length);
		assertEquals("AAPL", dataParts[0]);
		assertEquals("Apple", dataParts[1]);
		assertEquals("123.45", dataParts[2]);
		assertFalse(dataParts[3].isBlank());
		assertTrue(factory.getTags().contains(dataParts[3]));

		int volatility = Integer.parseInt(dataParts[4]);
		assertTrue(volatility >= 1 && volatility <= 7);
	}

	private void setField(Object target, String fieldName, String value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}
}
