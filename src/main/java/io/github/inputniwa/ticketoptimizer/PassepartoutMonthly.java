package io.github.inputniwa.ticketoptimizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule: pay the monthly pass, then Rotkreuz - Luzern trips are free.
 * Trips outside the 4 zones (Zug, Zürich) still cost full price.
 */
public class PassepartoutMonthly implements TicketOption {

    private final double passPricePerMonth;

    public PassepartoutMonthly(double passPricePerMonth) {
        this.passPricePerMonth = passPricePerMonth;
    }

    @Override
    public String name() {
        return "Passepartout monthly";
    }

    @Override
    public double monthlyCost(List<Trip> tripsInMonth) {
        List<Trip> notCovered = new ArrayList<>();
        for (Trip trip : tripsInMonth) {
            if (!isCovered(trip)) {
                notCovered.add(trip);
            }
        }
        return passPricePerMonth + TicketOption.sumOfTickets(notCovered);
    }

    /** Covered = a trip between Rotkreuz and Luzern, in either direction. */
    static boolean isCovered(Trip trip) {
        return isRotkreuzLuzern(trip.from(), trip.to()) || isRotkreuzLuzern(trip.to(), trip.from());
    }

    private static boolean isRotkreuzLuzern(String a, String b) {
        return a.equalsIgnoreCase("Rotkreuz") && b.equalsIgnoreCase("Luzern");
    }
}
