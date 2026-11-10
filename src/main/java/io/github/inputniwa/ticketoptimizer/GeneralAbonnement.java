package io.github.inputniwa.ticketoptimizer;

import java.util.List;

/** Rule: pay the GA once a year (spread over 12 months), then every trip in Switzerland is free. */
public class GeneralAbonnement implements TicketOption {

    private final double gaPricePerYear;

    public GeneralAbonnement(double gaPricePerYear) {
        this.gaPricePerYear = gaPricePerYear;
    }

    @Override
    public String name() {
        return "GA";
    }

    @Override
    public double monthlyCost(List<Trip> tripsInMonth) {
        return gaPricePerYear / 12;
    }
}
