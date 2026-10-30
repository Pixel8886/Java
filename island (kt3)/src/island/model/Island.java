package island.model;

import island.config.Config;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class Island {

    private final int width = Config.WIDTH;
    private final int height = Config.HEIGHT;
    private final Location[][] grid = new Location[width][height];

    public Island() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Location(x, y);
                grid[x][y].growPlants(ThreadLocalRandom.current().nextInt(Config.PLANT_START_MAX + 1));
            }
        }
        for (SpeciesType type : SpeciesType.VALUES) {
            for (int i = 0; i < Config.startCount(type); i++) {
                placeRandomly(type);
            }
        }
    }

    private void placeRandomly(SpeciesType type) {
        for (int attempt = 0; attempt < 20; attempt++) {
            Location loc = grid[ThreadLocalRandom.current().nextInt(width)][ThreadLocalRandom.current().nextInt(height)];
            if (loc.count(type) < type.getMaxPerCell()) {
                loc.add(type.create());
                return;
            }
        }
    }

    public Location getLocation(int x, int y) { return grid[x][y]; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

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
