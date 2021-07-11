package com.shblock.colossalbattery.helper;

import java.text.DecimalFormat;

public class MathHelper {
    private static final String[] NUMBER_CHARS = new String[] {"K", "M", "G", "T", "P", "E"};
    private static final DecimalFormat energyValue = new DecimalFormat("###,###,###,###,###.###");

    /**
     * Cast a long number to a int value, if it overflows the int, return the max or min value of int.
     * @param num The long number to cast.
     * @return The result int.
     */
    public static int longToInt(long num) {
        try {
            return Math.toIntExact(num);
        } catch (ArithmeticException ignored) {
            return num > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
    }

    /**
     * Format a big number to a easy-to-read value e.g. 12345 -> 12.345K
     * @param value The number to format.
     * @return The formatted number.
     */
    public static String formatLong(long value) {
        if (value <= 1000) {
            return energyValue.format(value);
        }
        int cnt = 0;
        double num = value / 1000D;
        while (num >= 1000) {
            num /= 1000;
            cnt ++;
        }
        return energyValue.format(num) + NUMBER_CHARS[cnt];
    }

    /**
     * Format a big number to a easy-to-read value e.g. 12345 -> 12.345K
     * @param value The number to format.
     * @return The formatted number.
     */
    public static String formatInt(int value) {
        return formatLong(value);
    }
}
