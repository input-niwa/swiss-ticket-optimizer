package io.github.inputniwa.ticketoptimizer;

import java.util.List;

/**
 * Looks up official prices by their name in prices.csv,
 * so no price is typed into the code by hand.
 */
public final class Prices {

    private final List<PriceEntry> entries;

    public Prices(List<PriceEntry> entries) {
        this.entries = entries;
    }

    /** Finds a price by its exact item name (upper/lower case doesn't matter). */
    public double get(String item) {
        for (PriceEntry entry : entries) {
            if (entry.item().equalsIgnoreCase(item)) {
                return entry.priceChf();
            }
        }
        throw new IllegalArgumentException("No price called \"" + item + "\" in prices.csv");
    }
}
