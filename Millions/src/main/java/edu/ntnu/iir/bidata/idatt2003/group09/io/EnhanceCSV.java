package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * EnhanceCSV allows adding tags to CSV files to create enhanced versions.
 * Preserves comments and headers, and appends tags to each data line.
 */
public class EnhanceCSV {

  private String filePath;
  private List<String> comments;
  private String header;
  private List<String[]> dataLines;
  private List<String> tags;

  public EnhanceCSV(String filePath) {
    this.filePath = filePath;
    this.comments = new ArrayList<>();
    this.dataLines = new ArrayList<>();
    this.tags = new ArrayList<>();
    readCsvFile();
  }

  /**
   * Reads the CSV file and parses comments, header, and data lines.
   */
  private void readCsvFile() {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean headerFound = false;

      while ((line = reader.readLine()) != null) {
        if (line.startsWith("#")) {
          comments.add(line);
        } else if (!headerFound && !line.trim().isEmpty()) {
          header = line;
          headerFound = true;
        } else if (!line.trim().isEmpty()) {
          String[] parts = line.split(",");
          dataLines.add(parts);
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading CSV file: " + e.getMessage());
    }
  }

  /**
   * Add a single tag to all data lines.
   *
   * @param tag the tag to add
   */
  public void addTag(String tag) {
    this.tags.add(tag);
  }

  /**
   * Add multiple tags to all data lines.
   *
   * @param tags array of tags to add
   */
  public void addTags(String[] tags) {
    for (String tag : tags) {
      this.tags.add(tag);
    }
  }

  /**
   * Generate a tag (can be overridden or used with specific logic).
   * By default, returns an empty string. Override or use with addTag().
   *
   * @return a generated tag
   */
  private String generateTag() {
    return "";
  }

  /**
   * Add auto-generated tags to all data lines.
   * The generateTag() method can be overridden in subclasses for custom logic.
   *
   * @param count number of tags to generate
   */
  public void addGeneratedTags(int count) {
    for (int i = 0; i < count; i++) {
      String tag = generateTag();
      if (!tag.isEmpty()) {
        this.tags.add(tag);
      }
    }
  }

  /**
   * Write the enhanced CSV to a new file.
   *
   * @param outputFilePath the path where the enhanced CSV will be written
   */
  public void writeEnhancedCsv(String outputFilePath) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
      // Write comments
      for (String comment : comments) {
        writer.println(comment);
      }

      // Write header with tags
      writer.print(header);
      for (String tag : tags) {
        writer.print("," + tag);
      }
      writer.println();

      // Write data lines with tags
      for (String[] dataLine : dataLines) {
        writer.print(String.join(",", dataLine));
        for (String tag : tags) {
          writer.print(",");
        }
        writer.println();
      }

      System.out.println("Enhanced CSV written to: " + outputFilePath);
    } catch (IOException e) {
      System.err.println("Error writing enhanced CSV: " + e.getMessage());
    }
  }

  /**
   * Get the number of data lines in the CSV.
   *
   * @return number of data lines
   */
  public int getDataLineCount() {
    return dataLines.size();
  }

  /**
   * Get the number of tags added so far.
   *
   * @return number of tags
   */
  public int getTagCount() {
    return tags.size();
  }

  /**
   * Clear all tags.
   */
  public void clearTags() {
    tags.clear();
  }

  /**
   * Get all tags currently added.
   *
   * @return list of tags
   */
  public List<String> getTags() {
    return new ArrayList<>(tags);
  }
}
