package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

/**
 * Immutable filter request for trade screen filtering.
 */
public record TradeFilterRequest(
    String searchText,
    boolean allSectorsSelected,
    boolean ownedOnly,
    boolean winnersOnly,
    boolean losersOnly
) {
}
