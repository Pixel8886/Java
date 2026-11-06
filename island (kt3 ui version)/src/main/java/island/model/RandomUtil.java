package island.model;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomUtil {

    private RandomUtil() {
    }

    public static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public static boolean chance(int percent) {
        return ThreadLocalRandom.current().nextInt(100) < percent;
    }
}
