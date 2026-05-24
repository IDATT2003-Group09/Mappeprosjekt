package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EnhanceCSV allows adding tags to CSV files to create enhanced versions.
 * Preserves comments and headers, and appends one text tag and one volatility score to each data line.
 */
public class EnhanceCSV {

  private static final Logger LOGGER = Logger.getLogger(EnhanceCSV.class.getName());

  private String filePath;
  private List<String> comments;
  private String header;
  private List<String[]> dataLines;
  private List<String> availableTags;
  private Random random;
  private static int maxVolatility = 7;
  private boolean shuffle = false;
  private boolean perturbPrices = false;
  private double maxPerturbFraction = 0.2; // up to +/-20%
  private boolean inputHasTagVolatility = false;
  private boolean randomizeSector = false;

  public EnhanceCSV(String filePath, List<String> availableTags) {
    this.filePath = filePath;
    this.comments = new ArrayList<>();
    this.dataLines = new ArrayList<>();
    this.availableTags = new ArrayList<>(availableTags);
    this.random = new Random();
    readCsvFile();
  }

  /**
   * Set an explicit Random instance to allow deterministic output in tests.
   *
   * @param random Random instance to use
   */
  public void setRandom(Random random) {
    this.random = random;
  }

  /**
   * Enable or disable shuffling of data rows when writing the enhanced CSV.
   *
   * @param shuffle true to shuffle rows
   */
  public void setShuffle(boolean shuffle) {
    this.shuffle = shuffle;
  }

  /**
   * Enable or disable random price perturbation when writing the enhanced CSV.
   *
   * @param perturbPrices true to randomly perturb prices
   */
  public void setPerturbPrices(boolean perturbPrices) {
    this.perturbPrices = perturbPrices;
  }

  /**
   * Enable random reassignment of the sector/Tag column for each row.
   * This will pick a random value from the available tags and place it into
   * the sector column (index 3) before writing.
   *
   * @param randomize true to randomize sector values
   */
  public void setRandomizeSector(boolean randomize) {
    this.randomizeSector = randomize;
  }

  /**
   * Set the maximum fraction to perturb prices by (e.g. 0.2 = +/-20%).
   *
   * @param fraction max perturbation fraction, must be >= 0
   */
  public void setMaxPerturbFraction(double fraction) {
    if (fraction < 0) throw new IllegalArgumentException("fraction must be >= 0");
    this.maxPerturbFraction = fraction;
  }

  /**
   * Reads the CSV file and parses comments, header, and data lines.
   */
  private void readCsvFile() {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;

      while ((line = reader.readLine()) != null) {
        if (line.startsWith("#")) {
          comments.add(line);
          String possibleHeader = line.substring(1).trim();
          if (header == null && possibleHeader.contains(",")) {
            header = possibleHeader;
          }
        } else if (!line.trim().isEmpty()) {
          String[] parts = line.split(",");
          // Detect an inline header like: Ticker,Name,Price,Tag,Volatility
          if (header == null && parts.length > 0 && parts[0].equalsIgnoreCase("ticker")) {
            header = String.join(",", parts);
            continue;
          }

          // Detect if input already has Tag and Volatility columns
          if (!inputHasTagVolatility && parts.length >= 5) {
            // if last column looks numeric and within volatility range, assume tag/vol columns present
            try {
              int v = Integer.parseInt(parts[parts.length - 1]);
              if (v >= 0 && v <= maxVolatility) {
                inputHasTagVolatility = true;
              }
            } catch (NumberFormatException ignored) {
            }
          }

          dataLines.add(parts);
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Error reading CSV file: " + filePath, e);
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
   * Set the maximum volatility score used when writing the enhanced CSV.
   *
   * @param maxVolatility the maximum allowed volatility score, must be greater than zero
   */
  public void setMaxVolatility(int maxVolatility) {
    if (maxVolatility <= 0) {
      throw new IllegalArgumentException("maxVolatility must be greater than zero");
    }
    EnhanceCSV.maxVolatility = maxVolatility;
  }

  /**
   * Get the maximum volatility score.
   *
   * @return the maximum volatility score
   */
  public int getMaxVolatility() {
    return maxVolatility;
  }
  /**
  * Write the enhanced CSV to a new file with one random text tag and volatility score applied to each stock.
   *
   * @param outputFilePath the path where the enhanced CSV will be written
   * @param maxTagsPerStock kept for compatibility; only one tag is written per row
   */
  public void writeEnhancedCsv(String outputFilePath) {
    List<String[]> toWrite = new ArrayList<>(dataLines);

    if (perturbPrices) {
      for (String[] parts : toWrite) {
        if (parts.length > 2) {
          try {
            double price = Double.parseDouble(parts[2]);
            double factor = 1.0 + ((random.nextDouble() * 2.0 - 1.0) * maxPerturbFraction);
            double newPrice = Math.max(0.01, price * factor);
            // format with two decimals
            parts[2] = String.format(Locale.ROOT, "%.2f", newPrice);
          } catch (Exception ignored) {
            // leave price as-is on parse failure
          }
        }
      }
    }

    if (shuffle) {
      Collections.shuffle(toWrite, random);
    }

    try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
      for (String comment : comments) {
        writer.println(comment);
      }

      if (header != null && !header.isBlank()) {
        writer.println(header + ",Tag,Volatility");
      } else {
        writer.println("Tag,Volatility");
      }

      for (String[] dataLine : toWrite) {
        if (randomizeSector && dataLine.length >= 4) {
          // overwrite sector column (index 3) with a random tag value
          dataLine[3] = getRandomTag();
        }

        if (inputHasTagVolatility && dataLine.length >= 2) {
          // overwrite the last two columns with new tag/volatility
          String[] copy = Arrays.copyOf(dataLine, Math.max(dataLine.length, 2));
          if (copy.length >= 2) {
            // determine base length (exclude last two columns)
            int baseLen = copy.length - 2;
            if (baseLen < 0) baseLen = 0;
            String[] base = Arrays.copyOf(copy, baseLen);
            writer.print(String.join(",", base));
            if (baseLen > 0) writer.print(",");
            writer.print(getRandomTag());
            writer.print(",");
            writer.println(getRandomVolatility());
          } else {
            writer.print(String.join(",", copy));
            writer.print(",");
            writer.print(getRandomTag());
            writer.print(",");
            writer.println(getRandomVolatility());
          }
        } else {
          writer.print(String.join(",", dataLine));
          writer.print(",");
          writer.print(getRandomTag());
          writer.print(",");
          writer.println(getRandomVolatility());
        }
      }

      LOGGER.info("Enhanced CSV written to: " + outputFilePath);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Error writing enhanced CSV: " + outputFilePath, e);
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
   * Generate one random volatility score.
   *
   * @return a random volatility score from 1 to maxVolatility
   */
  private int getRandomVolatility() {
    return random.nextInt(maxVolatility) + 1;
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
