package com.shblock.colossalbattery.helper;

public class MathHelper {
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
}
