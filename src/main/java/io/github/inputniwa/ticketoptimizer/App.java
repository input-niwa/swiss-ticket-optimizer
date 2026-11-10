package io.github.inputniwa.ticketoptimizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Starting point of the program.
 * Loads your prices and trips, then shows for every month
 * what each ticket option would have cost and which one is cheapest.
 */
public class App {

    static final Path PRICES_FILE = Path.of("src/main/resources/prices.csv");
    static final Path MY_TRIPS_FILE = Path.of("data/my-trips.csv");          // private, not on GitHub
    static final Path EXAMPLE_TRIPS_FILE = Path.of("data/example-trips.csv"); // fake data for GitHub

    void main() throws Exception {
        Prices prices = new Prices(CsvFiles.readPrices(PRICES_FILE));

        Path tripsFile = Files.exists(MY_TRIPS_FILE) ? MY_TRIPS_FILE : EXAMPLE_TRIPS_FILE;
        List<Trip> trips = CsvFiles.readTrips(tripsFile);
        IO.println("Loaded " + trips.size() + " trips from " + tripsFile + ".");

        // The options to compare, with prices taken from prices.csv (youth prices)
        List<TicketOption> options = List.of(
                new NormalTickets(),
                new HalfFare(prices.get("Half-fare card youth")),
                new PassepartoutMonthly(prices.get("Passepartout monthly 4 zones youth")),
                new GeneralAbonnement(prices.get("GA 2nd class youth"))
        );

        // Sort trips into months (TreeMap keeps the months in order)
        Map<YearMonth, List<Trip>> tripsPerMonth = new TreeMap<>();
        for (Trip trip : trips) {
            tripsPerMonth.computeIfAbsent(YearMonth.from(trip.date()), m -> new ArrayList<>()).add(trip);
        }

        for (Map.Entry<YearMonth, List<Trip>> month : tripsPerMonth.entrySet()) {
            IO.println("");
            IO.println(month.getKey() + "  (" + month.getValue().size() + " trips)");

            TicketOption cheapest = null;
            double cheapestCost = Double.MAX_VALUE;
            for (TicketOption option : options) {
                double cost = option.monthlyCost(month.getValue());
                IO.println(String.format("  %-22s CHF %8.2f", option.name(), cost));
                if (cost < cheapestCost) {
                    cheapestCost = cost;
                    cheapest = option;
                }
            }
            IO.println("  Cheapest: " + cheapest.name());
        }
    }
}
