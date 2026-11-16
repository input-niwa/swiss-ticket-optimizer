package io.github.inputniwa.ticketoptimizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Checks the program against the October example worked out by hand in rules.md:
 * 10 Luzern day passes (20.80 each) + 2 Zürich singles (23.00 each).
 */
class TicketOptionTest {

    private static final double CENT = 0.005; // allowed rounding difference

    /** The October example from rules.md. */
    private static List<Trip> october() {
        List<Trip> trips = new ArrayList<>();
        for (int day = 1; day <= 10; day++) {
            trips.add(new Trip(LocalDate.of(2026, 10, day), "Rotkreuz", "Luzern", 20.80, "day pass", "2nd", "memory"));
        }
        trips.add(new Trip(LocalDate.of(2026, 10, 3), "Rotkreuz", "Zürich HB", 23.00, "single", "2nd", "memory"));
        trips.add(new Trip(LocalDate.of(2026, 10, 3), "Rotkreuz", "Zürich HB", 23.00, "single", "2nd", "memory"));
        return trips;
    }

    @Test
    void normalTicketsCostWhatIPaid() {
        assertEquals(254.00, new NormalTickets().monthlyCost(october()), CENT);
    }

    @Test
    void halfFareIsCardPerMonthPlusHalfOfEveryTicket() {
        assertEquals(137.00, new HalfFare(120).monthlyCost(october()), CENT);
    }

    @Test
    void passepartoutCoversLuzernButNotZurich() {
        assertEquals(165.00, new PassepartoutMonthly(119).monthlyCost(october()), CENT);
    }

    @Test
    void gaIsYearlyPriceSpreadOverTwelveMonths() {
        // rules.md says 231.70, which is 2780 / 12 rounded; the exact value is 231.666...
        assertEquals(231.67, new GeneralAbonnement(2780).monthlyCost(october()), CENT);
    }

    @Test
    void passepartoutWorksInBothDirections() {
        Trip back = new Trip(LocalDate.of(2026, 9, 3), "Luzern", "Rotkreuz", 20.80, "day pass", "2nd", "ticket");
        assertTrue(PassepartoutMonthly.isCovered(back));
    }

    @Test
    void passepartoutDoesNotCoverZug() {
        Trip zug = new Trip(LocalDate.of(2026, 9, 24), "Rotkreuz", "Zug", 6.00, "single", "2nd", "memory");
        assertFalse(PassepartoutMonthly.isCovered(zug));
    }

    @Test
    void winterTrialGaOnlyInNovemberAndDecember() {
        WinterTrialGa trial = new WinterTrialGa(260);
        assertFalse(trial.isAvailableIn(YearMonth.of(2026, 10)));
        assertTrue(trial.isAvailableIn(YearMonth.of(2026, 11)));
        assertTrue(trial.isAvailableIn(YearMonth.of(2026, 12)));
        assertFalse(trial.isAvailableIn(YearMonth.of(2027, 1)));
        assertEquals(260.00, trial.monthlyCost(october()), CENT);
    }
}
