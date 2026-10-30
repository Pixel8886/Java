package island.config;

import island.model.SpeciesType;

import java.util.EnumMap;
import java.util.Map;

import static island.model.SpeciesType.*;

public final class Config {

    private Config() {
    }


    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
    public static final long TICK_MILLIS = 1000;
    public static final int MAX_TICKS = 0;



    public static final int PLANT_START_MAX = 100;
    public static final int PLANT_GROWTH_MAX = 30;


    public static final double START_SATIETY = 0.5;
    public static final double HUNGER_RATE = 0.1;
    public static final int REPRODUCE_CHANCE = 8;
    public static final int EAT_ATTEMPTS = 5;

    private static final Map<SpeciesType, Integer> START_COUNT = new EnumMap<>(SpeciesType.class);

    private static final Map<SpeciesType, Integer> LITTER = new EnumMap<>(SpeciesType.class);

    static {
        START_COUNT.put(WOLF, 30);
        START_COUNT.put(BOA, 30);
        START_COUNT.put(FOX, 30);
        START_COUNT.put(BEAR, 5);
        START_COUNT.put(EAGLE, 20);
        START_COUNT.put(HORSE, 100);
        START_COUNT.put(DEER, 100);
        START_COUNT.put(RABBIT, 500);
        START_COUNT.put(MOUSE, 1000);
        START_COUNT.put(GOAT, 200);
        START_COUNT.put(SHEEP, 200);
        START_COUNT.put(BOAR, 100);
        START_COUNT.put(BUFFALO, 30);
        START_COUNT.put(DUCK, 400);
        START_COUNT.put(CATERPILLAR, 2000);

        LITTER.put(WOLF, 2);
        LITTER.put(BOA, 2);
        LITTER.put(FOX, 2);
        LITTER.put(BEAR, 1);
        LITTER.put(EAGLE, 1);
        LITTER.put(HORSE, 1);
        LITTER.put(DEER, 1);
        LITTER.put(RABBIT, 2);
        LITTER.put(MOUSE, 3);
        LITTER.put(GOAT, 2);
        LITTER.put(SHEEP, 2);
        LITTER.put(BOAR, 2);
        LITTER.put(BUFFALO, 1);
        LITTER.put(DUCK, 2);
        LITTER.put(CATERPILLAR, 3);
    }

    public static int startCount(SpeciesType type) {
        return START_COUNT.getOrDefault(type, 0);
    }

    public static int litter(SpeciesType type) {
        return LITTER.getOrDefault(type, 1);
    }

    private static final int[][] EAT_CHANCE = {
            {  0,   0,   0,   0,   0,  10,  15,  60,  80,  60,  70,  15,  10,  40,   0,   0},  // Волк
            {  0,   0,  15,   0,   0,   0,   0,  20,  40,   0,   0,   0,   0,  10,   0,   0},  // Удав
            {  0,   0,   0,   0,   0,   0,   0,  70,  90,   0,   0,   0,   0,  60,  40,   0},  // Лиса
            {  0,  80,   0,   0,   0,  40,  80,  80,  90,  70,  70,  50,  20,  10,   0,   0},  // Медведь
            {  0,   0,  10,   0,   0,   0,   0,  90,  90,   0,   0,   0,   0,  80,   0,   0},  // Орел
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 100},  // Лошадь
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 100},  // Олень
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 100},  // Кролик
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  90, 100},  // Мышь
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 100},  // Коза
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 100},  // Овца
            {  0,   0,   0,   0,   0,   0,   0,   0,  50,   0,   0,   0,   0,   0,  90, 100},  // Кабан
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 100},  // Буйвол
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  90, 100},  // Утка
            {  0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 100},  // Гусеница
    };

    public static int eatChance(SpeciesType eater, SpeciesType prey) {
        return EAT_CHANCE[eater.ordinal()][prey.ordinal()];
    }

    public static int plantEatChance(SpeciesType eater) {
        return EAT_CHANCE[eater.ordinal()][SpeciesType.VALUES.length];
    }
}
