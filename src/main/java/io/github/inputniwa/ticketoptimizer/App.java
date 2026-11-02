package io.github.inputniwa.ticketoptimizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Starting point of the program.
 * Right now it loads your prices and your trips and shows what you spent per month.
 * Next weeks: compare normal tickets, Half-Fare, GA and regional passes.
 */
public class App {

    static final Path PRICES_FILE = Path.of("src/main/resources/prices.csv");
    static final Path MY_TRIPS_FILE = Path.of("data/my-trips.csv");          // private, not on GitHub
    static final Path EXAMPLE_TRIPS_FILE = Path.of("data/example-trips.csv"); // fake data for GitHub

    void main() throws Exception {
        List<PriceEntry> prices = CsvFiles.readPrices(PRICES_FILE);
        IO.println("Loaded " + prices.size() + " official prices.");

        Path tripsFile = Files.exists(MY_TRIPS_FILE) ? MY_TRIPS_FILE : EXAMPLE_TRIPS_FILE;
        List<Trip> trips = CsvFiles.readTrips(tripsFile);
        IO.println("Loaded " + trips.size() + " trips from " + tripsFile + ".");
        IO.println("");

        // Add up spending per month (TreeMap keeps the months in order)
        Map<YearMonth, Double> spentPerMonth = new TreeMap<>();
        for (Trip trip : trips) {
            spentPerMonth.merge(YearMonth.from(trip.date()), trip.priceChf(), Double::sum);
        }

        IO.println("Spent per month:");
        double total = 0;
        for (Map.Entry<YearMonth, Double> month : spentPerMonth.entrySet()) {
            IO.println(String.format("  %s   CHF %8.2f", month.getKey(), month.getValue()));
            total += month.getValue();
        }
        IO.println(String.format("  Total     CHF %8.2f", total));

        long estimates = trips.stream().filter(Trip::isEstimate).count();
        if (estimates > 0) {
            IO.println("");
            IO.println("Note: " + estimates + " of " + trips.size() + " trips are estimates from memory.");
        }
    }
}
