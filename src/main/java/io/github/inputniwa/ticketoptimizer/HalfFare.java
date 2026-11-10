package io.github.inputniwa.ticketoptimizer;

import java.util.List;

/** Rule: pay the card once a year (spread over 12 months), then every ticket costs half. */
public class HalfFare implements TicketOption {

    private final double cardPricePerYear;

    public HalfFare(double cardPricePerYear) {
        this.cardPricePerYear = cardPricePerYear;
    }

    @Override
    public String name() {
        return "Half-Fare card";
    }

    @Override
    public double monthlyCost(List<Trip> tripsInMonth) {
        return cardPricePerYear / 12 + TicketOption.sumOfTickets(tripsInMonth) / 2;
    }
}
