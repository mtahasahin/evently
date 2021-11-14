package com.github.mtahasahin.evently.util;

import java.util.Random;

public class RandomStringGenerator {
    public static String generate(int length) {
        return new Random().ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
