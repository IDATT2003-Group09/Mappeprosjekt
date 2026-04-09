package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StockSpecificEvent {

  private String partialHeadline;
  private String partialDescription;

	public StockSpecificEvent(String partialHeadline, String partialDescription) {
		this.partialHeadline = partialHeadline;
		this.partialDescription = partialDescription;
	}

  public String generateFullHeadline(String stockName) {
    return partialHeadline.replace("{stock}", stockName);
  }

  public String generateFullDescription(String stockName) {
    return partialDescription.replace("{stock}", stockName);
  }

}
