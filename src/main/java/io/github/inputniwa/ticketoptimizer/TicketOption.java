package io.github.inputniwa.ticketoptimizer;

import java.util.List;

/**
 * One way of paying for train trips (normal tickets, Half-Fare, a pass, ...).
 * Each option answers one question: what would one month of these trips cost?
 * The rules come from rules.md.
 */
public interface TicketOption {

    /** Short name shown in the result table. */
    String name();

    /** What the given month of trips would have cost with this option, in CHF. */
    double monthlyCost(List<Trip> tripsInMonth);

    /** Adds up the normal ticket prices of some trips. */
    static double sumOfTickets(List<Trip> trips) {
        double sum = 0;
        for (Trip trip : trips) {
            sum += trip.priceChf();
        }
        return sum;
    }
}
