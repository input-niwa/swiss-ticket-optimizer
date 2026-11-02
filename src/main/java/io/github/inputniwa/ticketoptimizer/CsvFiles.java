package io.github.inputniwa.ticketoptimizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the two data files: the official prices and the travel diary.
 *
 * The reading is forgiving on purpose, so the files stay easy to write by hand:
 * - spaces around commas are ignored
 * - empty lines are skipped
 * - heading lines starting with "--" or "#" are skipped
 * - dates can be 30/10/2026, 30.10.2026 or 2026-10-30
 */
public final class CsvFiles {

    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d.M.yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE
    );

    private CsvFiles() {
        // only static helper methods, no objects needed
    }

    /** Reads prices.csv. Columns: item, price_chf, valid_for, source_link, date_checked */
    public static List<PriceEntry> readPrices(Path file) throws IOException {
        List<PriceEntry> prices = new ArrayList<>();
        for (String[] cells : readRows(file, 5)) {
            prices.add(new PriceEntry(
                    cells[0],
                    parsePrice(cells[1]),
                    cells[2],
                    cells[3],
                    parseDate(cells[4])
            ));
        }
        return prices;
    }

    /** Reads the trips file. Columns: date, from, to, price_chf, ticket_type, class, source */
    public static List<Trip> readTrips(Path file) throws IOException {
        List<Trip> trips = new ArrayList<>();
        for (String[] cells : readRows(file, 7)) {
            trips.add(new Trip(
                    parseDate(cells[0]),
                    cells[1],
                    cells[2],
                    parsePrice(cells[3]),
                    cells[4],
                    cells[5],
                    cells[6]
            ));
        }
        return trips;
    }

    /**
     * Turns a file into rows of trimmed cells, skipping the header,
     * empty lines and heading lines. Stops with a clear message if a row
     * has the wrong number of columns.
     */
    private static List<String[]> readRows(Path file, int expectedColumns) throws IOException {
        List<String> lines = Files.readAllLines(file);
        List<String[]> rows = new ArrayList<>();
        boolean headerSkipped = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).strip();
            if (line.isEmpty() || line.startsWith("--") || line.startsWith("#")) {
                continue;
            }
            if (!headerSkipped) {
                headerSkipped = true; // first real line is the column names
                continue;
            }
            String[] cells = line.split(",", -1);
            if (cells.length != expectedColumns) {
                throw new IllegalArgumentException(
                        file.getFileName() + " line " + (i + 1) + ": expected " + expectedColumns
                                + " values but found " + cells.length + " -> " + line);
            }
            for (int c = 0; c < cells.length; c++) {
                cells[c] = cells[c].strip();
            }
            rows.add(cells);
        }
        return rows;
    }

    static double parsePrice(String text) {
        // allow "3'995" or "3995" or "20.80"
        return Double.parseDouble(text.replace("'", ""));
    }

    static LocalDate parseDate(String text) {
        for (DateTimeFormatter format : DATE_FORMATS) {
            try {
                return LocalDate.parse(text, format);
            } catch (DateTimeParseException ignored) {
                // try the next format
            }
        }
        throw new IllegalArgumentException("Unknown date format: " + text);
    }
}
