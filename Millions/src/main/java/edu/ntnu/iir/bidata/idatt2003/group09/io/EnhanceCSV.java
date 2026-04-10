package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.*;
import java.util.*;

/**
 * EnhanceCSV allows adding tags to CSV files to create enhanced versions.
 * Preserves comments and headers, and appends one text tag to each data line.
 */
public class EnhanceCSV {

  private String filePath;
  private List<String> comments;
  private String header;
  private List<String[]> dataLines;
  private List<String> availableTags;
  private Random random;

  public EnhanceCSV(String filePath, List<String> availableTags) {
    this.filePath = filePath;
    this.comments = new ArrayList<>();
    this.dataLines = new ArrayList<>();
    this.availableTags = new ArrayList<>(availableTags);
    this.random = new Random();
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
   * Add a single tag to the available tags pool.
   *
   * @param tag the tag to add
   */
  public void addAvailableTag(String tag) {
    this.availableTags.add(tag);
  }

  /**
   * Add multiple tags to the available tags pool.
   *
   * @param tags array of tags to add
   */
  public void addAvailableTags(String[] tags) {
    for (String tag : tags) {
      this.availableTags.add(tag);
    }
  }



  /**
   * Write the enhanced CSV to a new file with one random text tag applied to each stock.
   *
   * @param outputFilePath the path where the enhanced CSV will be written
   * @param maxTagsPerStock kept for compatibility; only one tag is written per row
   */
  public void writeEnhancedCsv(String outputFilePath, int maxTagsPerStock) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
      for (String comment : comments) {
        writer.println(comment);
      }

      writer.println(header + ",Tag");

      for (String[] dataLine : dataLines) {
        writer.print(String.join(",", dataLine));
        writer.print(",");
        writer.println(getRandomTag());
      }

      System.out.println("Enhanced CSV written to: " + outputFilePath);
    } catch (IOException e) {
      System.err.println("Error writing enhanced CSV: " + e.getMessage());
    }
  }

  /**
   * Generate one random tag.
   *
   * @return one randomly selected tag, or an empty string if none are available
   */
  private String getRandomTag() {
    if (availableTags.isEmpty()) {
      return "";
    }

    return availableTags.get(random.nextInt(availableTags.size()));
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
   * Get the number of available tags.
   *
   * @return number of available tags
   */
  public int getTagCount() {
    return availableTags.size();
  }

  /**
   * Clear all available tags.
   */
  public void clearTags() {
    availableTags.clear();
  }

  /**
   * Get all available tags.
   *
   * @return list of available tags
   */
  public List<String> getTags() {
    return new ArrayList<>(availableTags);
  }
}
