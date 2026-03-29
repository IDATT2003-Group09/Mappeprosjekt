package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.util.List;
import java.util.Objects;

public class Title {
  
  private String title;
  private final Player player;
  private final Exchange exchange;
  private final List<String> titleList = List.of(
    "Novice",
    "Investor",
    "Speculator"
  );

  public Title(Player player, Exchange exchange) {
    this.player = Objects.requireNonNull(player, "Player cannot be null");
    this.exchange = Objects.requireNonNull(exchange, "Exchange cannot be null");
    this.title = assignTitle(this.player);
  }
  public void setTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("""
          Title cannot be null or blank
          """);
    }
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  /**
   * Assigns a title using the player's current status and the current week from the exchange.
   *
   * @param player the player to evaluate
   * @return the assigned title
   * @throws NullPointerException if player is {@code null}
   */
  public String assignTitle(Player player) {
    Objects.requireNonNull(player, "Player cannot be null");
    int weeksPlayed = exchange.getWeek();
    return assignTitle(player.getStatus(), weeksPlayed);
  }

  /**
   * Assigns a title based on status and weeks played.
   *
   * @param status the player's status in percent relative to starting capital
   * @param weeksPlayed number of weeks played
   * @return the assigned title
   * @throws IllegalArgumentException if weeksPlayed is negative
   */
  public String assignTitle(int status, int weeksPlayed) {
    if (weeksPlayed < 0) {
      throw new IllegalArgumentException("Weeks played cannot be negative");
    }

    if (status >= 100 && weeksPlayed >= 20) {
      this.title = titleList.get(2);
    } else if (status >= 20 && weeksPlayed >= 10) {
      this.title = titleList.get(1);
    } else {
      this.title = titleList.get(0);
    }
    return title;
  }
}
