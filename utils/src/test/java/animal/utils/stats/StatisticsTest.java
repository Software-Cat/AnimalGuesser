package animal.utils.stats;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {

    @Test
    void mode() {
        assertEquals(Set.of(1, 3), Statistics.mode(List.of(1, 1, 2, 3, 3, 4)));
        assertEquals(Set.of(1.0, 3.0), Statistics.mode(List.of(1.0, 1.0, 2.0, 3.0, 3.0, 4.0)));
        assertEquals(Set.of(1.0f, 3.0f), Statistics.mode(List.of(1.0f, 1.0f, 2.0f, 3.0f, 3.0f, 4.0f)));

        assertEquals(Set.of(0), Statistics.mode(List.of(0)));
    }


    @Test
    void median() {
        assertEquals(2, Statistics.median(List.of(1, 2, 3)));
        assertEquals(2.5, Statistics.median(List.of(1, 2, 3, 4)));
        assertEquals(1, Statistics.median(List.of(1, 1, 2, 1)));

        assertEquals(0, Statistics.median(List.of(0)));
    }

    @Test
    void sum() {
        assertEquals(3, Statistics.sum(List.of(1, 2), Integer::sum));

        assertEquals(1, Statistics.sum(List.of(1), Integer::sum));
    }

    @Test
    void min() {
        assertEquals(1, Statistics.min(List.of(1, 1, 2, 3, 3, 4)));
        assertEquals(1, Statistics.min(List.of(1.0, 1.0, 2.0, 3.0, 3.0, 4.0)));
        assertEquals(1, Statistics.min(List.of(1.0f, 1.0f, 2.0f, 3.0f, 3.0f, 4.0f)));

        assertEquals(1, Statistics.min(List.of(1)));
    }

    @Test
    void max() {
        assertEquals(4, Statistics.max(List.of(1, 1, 2, 3, 3, 4)));
        assertEquals(4, Statistics.max(List.of(1.0, 1.0, 2.0, 3.0, 3.0, 4.0)));
        assertEquals(4, Statistics.max(List.of(1.0f, 1.0f, 2.0f, 3.0f, 3.0f, 4.0f)));

        assertEquals(1, Statistics.max(List.of(1)));
    }

    @Test
    void lowerQuartile() {
        assertEquals(1, Statistics.lowerQuartile(List.of(1, 1, 2, 3, 3, 4)));
        assertEquals(1.5, Statistics.lowerQuartile(List.of(1.0, 2.0, 3.0, 3.0, 4.0)));
        assertEquals(2, Statistics.lowerQuartile(List.of(1.0f, 3.0f, 3.0f, 4.0f)));

        assertEquals(1, Statistics.lowerQuartile(List.of(1)));
    }

    @Test
    void upperQuartile() {
        assertEquals(3, Statistics.upperQuartile(List.of(1, 1, 2, 3, 3, 4)));
        assertEquals(3.5, Statistics.upperQuartile(List.of(1.0, 2.0, 3.0, 3.0, 4.0)));
        assertEquals(3.5, Statistics.upperQuartile(List.of(1.0f, 3.0f, 3.0f, 4.0f)));

        assertEquals(1, Statistics.upperQuartile(List.of(1)));
    }

    @Test
    void iqr() {
        assertEquals(2, Statistics.iqr(List.of(1, 1, 2, 3, 3, 4)));
        assertEquals(2, Statistics.iqr(List.of(1.0, 2.0, 3.0, 3.0, 4.0)));
        assertEquals(1.5, Statistics.iqr(List.of(1.0f, 3.0f, 3.0f, 4.0f)));

        assertEquals(0, Statistics.iqr(List.of(1)));
    }
}
