package io.github.inputniwa.ticketoptimizer;

import java.time.LocalDate;

/**
 * One train trip from the travel diary.
 *
 * @param date        the day of the trip
 * @param from        start station, e.g. "Rotkreuz"
 * @param to          end station, e.g. "Luzern"
 * @param priceChf    what was paid, in Swiss francs
 * @param ticketType  e.g. "single", "day pass", "return"
 * @param travelClass "1st" or "2nd"
 * @param source      where the row came from: "diary", "ticket", "bank" or "memory"
 */
public record Trip(
        LocalDate date,
        String from,
        String to,
        double priceChf,
        String ticketType,
        String travelClass,
        String source
) {
    /** True if the price is only an estimate (remembered, not checked). */
    public boolean isEstimate() {
        return source.equalsIgnoreCase("memory");
    }
}
