package cz.reindl.arkanoidfx.utils;

import java.util.Random;

public class Utils {

    public static boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    public static int getRandomNumber(int bound, int startPoint) {
        return new Random().nextInt(bound) + startPoint;
    }

}
