package cz.reindl.arkanoidfx.utils;

import java.util.Random;

public class Utils {

    public static boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    public static int getRandomIntegerNumber(int startPoint, int bound) {
        return new Random().nextInt(startPoint, bound);
    }

    public static double getRandomDoubleNumber(double startPoint, double bound) {
        return new Random().nextDouble(startPoint, bound);
    }

}
