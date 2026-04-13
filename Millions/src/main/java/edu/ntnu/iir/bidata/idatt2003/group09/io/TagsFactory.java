package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.util.List;
import java.util.ArrayList;

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

  public void enhanceCsv(String inputFile, String outputFile) {
  
    
    EnhanceCSV enhancer = new EnhanceCSV(inputFilePath + inputFile, tags);
    enhancer.writeEnhancedCsv(outputFilePath + outputFile);
  }

  public List<String> getTags(){
    return new ArrayList<>(tags);
  }

}
