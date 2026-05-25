package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

/**
 * Immutable filter request for trade screen filtering.
 *
 * @param searchText free-text search query (symbol/company), may be {@code null}
 * @param ownedOnly true to keep only stocks currently owned by the player
 * @param winnersOnly true to keep only stocks with positive weekly change
 * @param losersOnly true to keep only stocks with negative weekly change
 */
public record TradeFilterRequest(
    String searchText,
    boolean ownedOnly,
    boolean winnersOnly,
    boolean losersOnly
) {
}
