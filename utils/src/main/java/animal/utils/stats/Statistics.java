package animal.utils.stats;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BinaryOperator;

public class Statistics {

    public static <T extends Number> T sum(@Nullable Collection<T> data, @NotNull BinaryOperator<T> summation) {
        if (data == null || data.size() == 0) {
            return null;
        }

        return data.stream()
                .reduce(summation)
                .orElse(null);
    }

    public static <T extends Number & Comparable<? super T>> T min(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return null;
        }

        return data.stream()
                .reduce((T a, T b) -> {
                    int res = a.compareTo(b);
                    if (res > 0) {
                        return b;
                    } else {
                        return a;
                    }
                }).get();
    }

    public static <T extends Number & Comparable<? super T>> double lowerQuartile(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return Double.NaN;
        }

        List<T> sorted = new java.util.ArrayList<>(data);
        Collections.sort(sorted);

        if (data.size() == 1) {
            return new ArrayList<>(data).get(0).doubleValue();
        } else {
            return median(sorted.subList(0, sorted.size() / 2));
        }
    }

    public static <T extends Number & Comparable<? super T>> double upperQuartile(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return Double.NaN;
        }

        List<T> sorted = new java.util.ArrayList<>(data);
        Collections.sort(sorted);

        if (data.size() == 1) {
            return new ArrayList<>(data).get(0).doubleValue();
        } else {
            return median(sorted.subList((sorted.size() + 1) / 2, sorted.size()));
        }
    }

    public static <T extends Number & Comparable<? super T>> double iqr(@Nullable Collection<T> data) {
        return upperQuartile(data) - lowerQuartile(data);
    }

    public static <T extends Number & Comparable<? super T>> T max(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return null;
        }

        return data.stream()
                .reduce((T a, T b) -> {
                    int res = a.compareTo(b);
                    if (res > 0) {
                        return a;
                    } else {
                        return b;
                    }
                }).get();
    }

    public static <T extends Number & Comparable<? super T>> T range(
            @Nullable Collection<T> data,
            @NotNull BinaryOperator<T> difference) {
        return difference.apply(max(data), min(data));
    }

    public static <T extends Number> double mean(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return Double.NaN;
        }

        return doubleSum(data) / data.size();
    }

    public static <T extends Number & Comparable<? super T>> double median(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return Double.NaN;
        }

        List<T> sorted = new java.util.ArrayList<>(data);
        Collections.sort(sorted);

        if (sorted.size() % 2 == 0) {
            return mean(List.of(sorted.get(sorted.size() / 2 - 1), sorted.get(sorted.size() / 2)));
        } else {
            return sorted.get(sorted.size() / 2).doubleValue();
        }
    }

    public static <T extends Number> Set<T> mode(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return null;
        }

        Multiset<T> set = HashMultiset.create();
        set.addAll(data);
        Set<T> modes = new HashSet<>();
        int maxCount = Integer.MIN_VALUE;

        for (Multiset.Entry<T> entry : set.entrySet()) {
            if (entry.getCount() > maxCount) {
                modes.clear();
                modes.add(entry.getElement());
                maxCount = entry.getCount();
            } else if (entry.getCount() == maxCount) {
                modes.add(entry.getElement());
            }
        }

        return modes;
    }

    private static <T extends Number> double doubleSum(@Nullable Collection<T> data) {
        if (data == null || data.size() == 0) {
            return 0;
        }

        return data.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }
}
