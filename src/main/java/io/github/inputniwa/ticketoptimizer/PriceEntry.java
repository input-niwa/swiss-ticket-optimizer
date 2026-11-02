package io.github.inputniwa.ticketoptimizer;

import java.time.LocalDate;

/**
 * One official price from prices.csv, e.g. "GA 2nd class youth" for CHF 2780.
 *
 * @param item        what the price is for
 * @param priceChf    the price in Swiss francs
 * @param validFor    how long it lasts, e.g. "1 year", "1 month", "1 day", "single trip"
 * @param source      the link or place where the price was found
 * @param dateChecked the day the price was looked up
 */
public record PriceEntry(
        String item,
        double priceChf,
        String validFor,
        String source,
        LocalDate dateChecked
) {
}
