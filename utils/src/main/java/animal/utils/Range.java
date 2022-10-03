package animal.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Range<T extends Comparable<T>> {

    @NotNull
    private final T boundOne;

    @NotNull
    private final T boundTwo;

    private final boolean lowerInclusive;

    private final boolean upperInclusive;

    public Range(@NotNull T boundOne, @NotNull T boundTwo, boolean lowerInclusive, boolean upperInclusive) {
        assert boundOne.compareTo(boundTwo) != 0;

        this.boundOne = boundOne;
        this.boundTwo = boundTwo;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
    }

    public Range(@NotNull T boundOne, @NotNull T boundTwo) {
        this(boundOne, boundTwo, true, false);
    }

    public T getLowerBound() {
        if (boundOne.compareTo(boundTwo) < 0) {
            return boundOne;
        } else {
            return boundTwo;
        }
    }

    public T getUpperBound() {
        if (boundOne.compareTo(boundTwo) > 0) {
            return boundOne;
        } else {
            return boundTwo;
        }
    }

    public <U extends T> boolean isInRange(@Nullable final U value) {
        if (value == null) {
            return false;
        }

        int compareLower = getLowerBound().compareTo(value);
        int compareUpper = getUpperBound().compareTo(value);

        boolean satisfyLower = lowerInclusive ? compareLower >= 0 : compareLower > 0;
        boolean satisfyUpper = upperInclusive ? compareUpper <= 0 : compareUpper < 0;

        return satisfyLower && satisfyUpper;
    }
}
