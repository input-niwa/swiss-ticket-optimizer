package io.github.inputniwa.ticketoptimizer;

import java.util.List;

/** Rule: no card, no pass. You pay every ticket at full price (what I do now). */
public class NormalTickets implements TicketOption {

    @Override
    public String name() {
        return "Normal tickets";
    }

    @Override
    public double monthlyCost(List<Trip> tripsInMonth) {
        return TicketOption.sumOfTickets(tripsInMonth);
    }
}
