package cz.reindl.arkanoidfx.utils;

import java.util.Random;

public class Utils {

    private static final int[] numbers = new int[2];

    public static boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    public static int getRandomIntegerNumber(int startPoint, int bound) {
        return new Random().nextInt(startPoint, bound);
    }

    public static double getRandomDoubleNumber(double startPoint, double bound) {
        return new Random().nextDouble(startPoint, bound);
    }

    public static int getRandomArrayValue() {
        numbers[0] = 1;
        numbers[1] = -1;
        return numbers[new Random().nextInt(2)];
    }

}
