package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.util.List;
import java.util.ArrayList;

public class TagsFactory {

  private List<String> tags = List.of("Tech", "Finance", "Health", "Energy", "Consumer", "Industrial", "Utilities", "Real Estate", "Materials", "Telecom");
  private int maxTags = 5;

  private String inputFilePath = "src/main/resources/csv/input/";
  private String outputFilePath = "src/main/resources/csv/enhanced/";

  public void enhanceCsv(String inputFile, String outputFile) {
  
    
    EnhanceCSV enhancer = new EnhanceCSV(inputFilePath + inputFile, tags);
    enhancer.writeEnhancedCsv(outputFilePath + outputFile, maxTags);
  }

  public List<String> getTags(){
    return new ArrayList<>(tags);
  }

}
