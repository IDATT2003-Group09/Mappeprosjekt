package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.util.List;
import java.util.ArrayList;

/**
 * facotry containing the different sector tags a stock can have
 */
public class TagsFactory {

    List<String> tags = List.of(
            "Technology",
            "Consumer Discretionary",
            "Communication Services",
            "Financials",
            "Consumer Staples",
            "Health Care",
            "Energy",
            "Industrials",
            "Materials",
            "Utilities",
            "Real Estate"
    );

  private String inputFilePath = "src/main/resources/csv/input/";
  private String outputFilePath = "src/main/resources/csv/output/";

  /**
   * Uses EnhanceCSV with the predeclared sectors to enhance a csv
   * @param inputFile the file that should be enhances
   * @param outputFile where it should be written to
   */
  public void enhanceCsv(String inputFile, String outputFile) {
    EnhanceCSV enhancer = new EnhanceCSV(inputFilePath + inputFile, tags);
    enhancer.writeEnhancedCsv(outputFilePath + outputFile);
  }

  /**
   * incase you need the tags elsewhere
   */
  public List<String> getTags(){
    return new ArrayList<>(tags);
  }

}
