package dev.army.utils;

import java.util.HashMap;
import java.util.Map;

public class RomanNumerals {
    public static int convert(String numerals) {
        int length = numerals.length();
        int result = 0;

        for (int i = 0; i < length; i++) {
            int current = values.getOrDefault(numerals.charAt(i), -1);
            if (i < length-1 && current < values.getOrDefault(numerals.charAt(i+1), -1)) {
                result -= current;
            } else {
                result += current;
            }
        }
        return result;
    }

    private static final Map<Character, Integer> values = new HashMap<>(){{
        put('I', 1);
        put('V', 5);
        put('X', 10);
        put('L', 50);
        put('C', 100);
        put('D', 500);
        put('M', 1000);
    }};

    // test cases
    static void main() {
        System.out.format("value of I is %d\n", convert("I"));
        System.out.format("value of IV is %d\n", convert("IV"));
        System.out.format("value of IX is %d\n", convert("IX"));
        System.out.format("value of XXIV is %d\n", convert("XXIV"));
        System.out.format("value of MCMIV is %d\n", convert("MCMIV"));
        System.out.format("value of MCMXCIV is %d\n", convert("MCMXCIV"));
    }
}
