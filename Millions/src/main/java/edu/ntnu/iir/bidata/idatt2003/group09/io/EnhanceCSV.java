package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.*;
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
   * Write the enhanced CSV to a new file with random tags applied to each stock.
   * Each stock gets a random subset of available tags.
   *
   * @param outputFilePath the path where the enhanced CSV will be written
   * @param maxTagsPerStock the maximum number of tags to randomly apply to each stock
   */
  public void writeEnhancedCsv(String outputFilePath, int maxTagsPerStock) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
      // Write comments
      for (String comment : comments) {
        writer.println(comment);
      }

      // Write header with tag columns
      writer.print(header);
      for (int i = 1; i <= availableTags.size(); i++) {
        writer.print(",Tag_" + i);
      }
      writer.println();

      // Write data lines with randomly selected tags as 1/0 values
      for (String[] dataLine : dataLines) {
        writer.print(String.join(",", dataLine));

        // Generate random tags for this stock
        List<String> randomTags = getRandomTags(maxTagsPerStock);
        Set<String> selectedTags = new HashSet<>(randomTags);

        // Write 1 if the tag is selected for this stock, else 0
        for (String tag : availableTags) {
          writer.print(selectedTags.contains(tag) ? ",1" : ",0");
        }
        writer.println();
      }

      System.out.println("Enhanced CSV written to: " + outputFilePath);
    } catch (IOException e) {
      System.err.println("Error writing enhanced CSV: " + e.getMessage());
    }
  }

  /**
   * Generate a random subset of tags.
   *
   * @param maxTags the maximum number of tags to select
   * @return a list of randomly selected tags
   */
  private List<String> getRandomTags(int maxTags) {
    if (availableTags.isEmpty()) {
      return new ArrayList<>();
    }

    int boundedMax = Math.min(maxTags, availableTags.size());
    if (boundedMax <= 0) {
      return new ArrayList<>();
    }

    int numTags = random.nextInt(boundedMax) + 1;
    List<String> shuffled = new ArrayList<>(availableTags);
    Collections.shuffle(shuffled, random);
    
    return shuffled.subList(0, numTags);
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
