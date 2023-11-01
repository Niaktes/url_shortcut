package ru.job4j.shortcut.util;

import java.util.Random;

public class RandomGenerator {

    private RandomGenerator() { }

    private static final int LEFT_LIMIT = 48;
    private static final int RIGHT_LIMIT = 122;
    private static final Random RANDOM = new Random();


    public static String generate(int length) {
        return RANDOM.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}