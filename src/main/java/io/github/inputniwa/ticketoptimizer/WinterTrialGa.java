package io.github.inputniwa.ticketoptimizer;

import java.time.YearMonth;
import java.util.List;

/**
 * Rule: a one-month GA trial. Pay once, then every trip in Switzerland is free for that month.
 * Only sold for a few weeks in winter (2026: start between 9 November and 21 December),
 * so it only counts in November and December.
 */
public class WinterTrialGa implements TicketOption {

    private static final YearMonth FIRST_MONTH = YearMonth.of(2026, 11);
    private static final YearMonth LAST_MONTH = YearMonth.of(2026, 12);

    private final double pricePerMonth;

    public WinterTrialGa(double pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    @Override
    public String name() {
        return "Winter trial GA";
    }

    @Override
    public double monthlyCost(List<Trip> tripsInMonth) {
        return pricePerMonth;
    }

    @Override
    public boolean isAvailableIn(YearMonth month) {
        return !month.isBefore(FIRST_MONTH) && !month.isAfter(LAST_MONTH);
    }
}
