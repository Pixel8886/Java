package island.model;


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class Location {

    private final int x;
    private final int y;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<SpeciesType, List<Animal>> animals = new EnumMap<>(SpeciesType.class);
    private int plants;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }

    public int getX() { return x; }
    public int getY() { return y; }


    public int count(SpeciesType type) {
        List<Animal> list = animals.get(type);
        return list == null ? 0 : list.size();
    }

    public void add(Animal animal) {
        animals.computeIfAbsent(animal.getType(), t -> new ArrayList<>()).add(animal);
    }

    public boolean remove(Animal animal) {
        List<Animal> list = animals.get(animal.getType());
        return list != null && list.remove(animal);
    }

    public Animal randomOf(SpeciesType type) {
        List<Animal> list = animals.get(type);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public List<Animal> snapshot() {
        List<Animal> result = new ArrayList<>();
        for (List<Animal> list : animals.values()) {
            result.addAll(list);
        }
        return result;
    }


    public int getPlants() { return plants; }

    public void growPlants(int amount) {
        plants = Math.min(Plant.MAX_PER_CELL, plants + amount);
    }

    public int takePlants(int wanted) {
        int taken = Math.min(plants, wanted);
        plants -= taken;
        return taken;
    }
}
