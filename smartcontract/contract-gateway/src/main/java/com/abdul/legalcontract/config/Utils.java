package com.abdul.legalcontract.config;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    private static final Random RANDOM = new Random();

    public static String getEnvOrDefault(final String name, final String defaultValue) {
        return getEnvOrDefault(name, Function.identity(), defaultValue);
    }

    public static <T> T getEnvOrDefault(final String name, final Function<String, T> map, final T defaultValue) {
        var result = System.getenv(name);
        return result != null ? map.apply(result) : defaultValue;
    }

    /**
     * Generate a random integer in the range 0 to max - 1.
     *
     * @param max Maximum value (exclusive).
     * @return A random number.
     */
    public static int randomInt(final int max) {
        return RANDOM.nextInt(max);
    }

    /**
     * Pick a random element from a list.
     *
     * @param values Candidate elements.
     * @param <T>    Element type.
     * @return A randomly selected value.
     */
    public static <T> T randomElement(final List<? extends T> values) {
        return values.get(randomInt(values.size()));
    }

    /**
     * Pick a random element from an array, excluding the current value.
     *
     * @param values       Candidate elements.
     * @param currentValue Value to avoid.
     * @param <T>          Element type.
     * @return A random value.
     */
    public static <T> T differentElement(final List<? extends T> values, final T currentValue) {
        var candidateValues = values.stream()
                .filter(value -> !currentValue.equals(value))
                .collect(Collectors.toList());
        return randomElement(candidateValues);
    }

    private Utils() {
    }
}
