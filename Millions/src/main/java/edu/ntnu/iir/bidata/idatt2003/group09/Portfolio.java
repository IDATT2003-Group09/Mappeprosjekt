package edu.ntnu.iir.bidata.idatt2003.group09;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Portfolio {

  private List<Share> shares;

  /***
   * intitalizes the shares list
   */
  public Portfolio() {
    shares = new ArrayList<>();
  }

  /***
   * returns true if adding share was successfull
   */
  public boolean addShare(Share share) {
    return shares.add(share);
  }

  /***
   * returns true if removing share was successfull
   */
  public boolean removeShare(Share share) {
    return shares.remove(share);
  }

  /***
   * returns a unmodifiable verison of the shares list
   */
  public List<Share> getShares() {
    return Collections.unmodifiableList(shares);
  }

  /***
   * returns a list containing all shares of a specific symbol
   */
  public List<Share> getShares(String symbol) {
    List<Share> matchingShares = new ArrayList<>();

    for (Share share : shares) {
      if (share.getStock().getSymbol().equalsIgnoreCase(symbol)) {
        matchingShares.add(share);
      }
    }

    return matchingShares;
  }

}
