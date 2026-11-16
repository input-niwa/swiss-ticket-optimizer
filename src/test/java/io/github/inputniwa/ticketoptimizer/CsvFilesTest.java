package io.github.inputniwa.ticketoptimizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Checks that the hand-written files are read the forgiving way they're written. */
class CsvFilesTest {

    @Test
    void readsAllThreeDateStyles() {
        LocalDate expected = LocalDate.of(2026, 10, 30);
        assertEquals(expected, CsvFiles.parseDate("30/10/2026"));
        assertEquals(expected, CsvFiles.parseDate("30.10.2026"));
        assertEquals(expected, CsvFiles.parseDate("2026-10-30"));
    }

    @Test
    void readsPricesWithSwissApostrophe() {
        assertEquals(3995.0, CsvFiles.parsePrice("3'995"), 0.001);
        assertEquals(20.80, CsvFiles.parsePrice("20.80"), 0.001);
    }

    @Test
    void skipsHeadingsAndEmptyLinesAndIgnoresSpaces(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("prices.csv");
        Files.writeString(file, """
                item,price_chf,valid_for,source_link,date_checked

                                 -- Yearly passes --
                Half-fare card youth, 120, 1 year, https://www.sbb.ch, 30/10/2026
                # a comment line
                GA 2nd class youth, 2780, 1 year, https://www.sbb.ch, 30/10/2026
                """);

        List<PriceEntry> prices = CsvFiles.readPrices(file);

        assertEquals(2, prices.size());
        assertEquals("Half-fare card youth", prices.get(0).item());
        assertEquals(120.0, prices.get(0).priceChf(), 0.001);
    }

    @Test
    void wrongNumberOfColumnsGivesClearError(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("prices.csv");
        Files.writeString(file, "item,price_chf,valid_for,source_link,date_checked\nGA, 2780\n");
        assertThrows(IllegalArgumentException.class, () -> CsvFiles.readPrices(file));
    }
}
