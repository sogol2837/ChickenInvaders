package chickenInvaders.util;

import java.util.Random;


public class RandomUtils {

    private static final Random RNG = new Random();

    private RandomUtils() {
    }

    public static boolean chance(double probability) {
        return RNG.nextDouble() < probability;
    }

    public static boolean coinFlip() {
        return RNG.nextBoolean();
    }

    public static int between(int minInclusive, int maxInclusive) {
        return minInclusive + RNG.nextInt(maxInclusive - minInclusive + 1);
    }

    public static <T> T pick(T[] items) {
        return items[RNG.nextInt(items.length)];
    }
}
