package edu.ntnu.iir.bidata.idatt2003.group09;

import java.util.List;
import java.util.Objects;

public class Title {
  
  private String title;
  private final Player player;
  private final Exchange exchange;
  private final List<String> titleList = List.of(
    "Novice Investor",
    "Amateur Investor",
    "Skilled Investor",
    "Professional Investor",
    "Expert Investor",
    "Master Investor"
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

  public String assignTitle(Player player) {
    Objects.requireNonNull(player, "Player cannot be null");
    int weeksPlayed = exchange.getWeek();
    return assignTitle(player.getStatus(), weeksPlayed);
  }

  public String assignTitle(int status, int weeksPlayed) {
    if (weeksPlayed < 0) {
      throw new IllegalArgumentException("Weeks played cannot be negative");
    }

    int titleLevel = 0;
    for (int level = titleList.size() - 1; level >= 0; level--) {
      if (status >= getMinimumStatusForLevel(level)
          && weeksPlayed >= getMinimumWeeksForLevel(level)) {
        titleLevel = level;
        break;
      }
    }
    this.title = titleList.get(titleLevel);
    return title;
  }

  private int getMinimumStatusForLevel(int level) {
    return switch (level) {
      case 0 -> Integer.MIN_VALUE;
      case 1 -> -20;
      case 2 -> 0;
      case 3 -> 20;
      case 4 -> 40;
      case 5 -> 60;
      default -> throw new IllegalArgumentException("Unknown level: " + level);
    };
  }

  private int getMinimumWeeksForLevel(int level) {
    return switch (level) {
      case 0 -> 0;
      case 1 -> 2;
      case 2 -> 4;
      case 3 -> 8;
      case 4 -> 12;
      case 5 -> 20;
      default -> throw new IllegalArgumentException("Unknown level: " + level);
    };
  }
}
