package island.model;

import island.config.Config;

import java.util.EnumMap;
import java.util.Map;

/** Остров: двумерный массив клеток. */
public class Island {

    private final int width = Config.WIDTH;
    private final int height = Config.HEIGHT;
    private final Location[][] grid = new Location[width][height];

    public Island() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Location(x, y);
                grid[x][y].growPlants(RandomUtil.nextInt(0, Config.PLANT_START_MAX));
            }
        }
        for (SpeciesType type : SpeciesType.VALUES) {
            for (int i = 0; i < Config.startCount(type); i++) {
                placeRandomly(type);
            }
        }
    }

    /** Ставит животное в случайную клетку, где ещё есть место (делается до старта потоков). */
    private void placeRandomly(SpeciesType type) {
        for (int attempt = 0; attempt < 20; attempt++) {
            Location loc = grid[RandomUtil.nextInt(0, width - 1)][RandomUtil.nextInt(0, height - 1)];
            if (loc.count(type) < type.getMaxPerCell()) {
                loc.add(type.create());
                return;
            }
        }
    }

    public Location getLocation(int x, int y) { return grid[x][y]; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /** Сколько животных каждого вида сейчас на острове. */
    public Map<SpeciesType, Integer> countAnimals() {
        Map<SpeciesType, Integer> result = new EnumMap<>(SpeciesType.class);
        for (SpeciesType t : SpeciesType.VALUES) {
            result.put(t, 0);
        }
        for (Location[] column : grid) {
            for (Location loc : column) {
                loc.lock();
                try {
                    for (SpeciesType t : SpeciesType.VALUES) {
                        result.merge(t, loc.count(t), Integer::sum);
                    }
                } finally {
                    loc.unlock();
                }
            }
        }
        return result;
    }

    public long countPlants() {
        long sum = 0;
        for (Location[] column : grid) {
            for (Location loc : column) {
                loc.lock();
                try {
                    sum += loc.getPlants();
                } finally {
                    loc.unlock();
                }
            }
        }
        return sum;
    }
}
