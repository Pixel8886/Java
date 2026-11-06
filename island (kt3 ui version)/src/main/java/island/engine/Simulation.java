package island.engine;

import island.config.Config;
import island.model.Animal;
import island.model.Island;
import island.model.Location;
import island.model.Plant;
import island.model.SpeciesType;
import island.model.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Simulation {

    private final Island island = new Island();
    private final AtomicInteger tick = new AtomicInteger();
    private final CountDownLatch finished = new CountDownLatch(1);
    private volatile boolean running;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3, Simulation::daemon);
    private final ExecutorService workers =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), Simulation::daemon);

    private static Thread daemon(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }

    public void start() {
        running = true;
        long period = Config.TICK_MILLIS;
        scheduler.scheduleAtFixedRate(this::growPlants, 0, period, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::animalsLifeCycle, 0, period, TimeUnit.MILLISECONDS);
        // статистика печатается в середине такта, когда жизненный цикл уже отработал
        scheduler.scheduleAtFixedRate(this::printStatistics, period / 2, period, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        running = false;
        scheduler.shutdownNow();
        workers.shutdownNow();
        finished.countDown();
    }

    public void awaitFinish() {
        try {
            finished.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Island getIsland() { return island; }
    public int getTick() { return tick.get(); }
    public boolean isRunning() { return running; }

    // ---------- Задание 1: рост растений ----------

    private void growPlants() {
        try {
            for (int x = 0; x < island.getWidth(); x++) {
                for (int y = 0; y < island.getHeight(); y++) {
                    Location loc = island.getLocation(x, y);
                    loc.lock();
                    try {
                        loc.growPlants(RandomUtil.nextInt(0, Config.PLANT_GROWTH_MAX));
                    } finally {
                        loc.unlock();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- Задание 2: жизненный цикл животных ----------

    private void animalsLifeCycle() {
        try {
            int currentTick = tick.incrementAndGet();
            List<Callable<Void>> jobs = new ArrayList<>();
            for (int y = 0; y < island.getHeight(); y++) {
                final int row = y;
                jobs.add(() -> {
                    for (int x = 0; x < island.getWidth(); x++) {
                        processLocation(island.getLocation(x, row), currentTick);
                    }
                    return null;
                });
            }
            for (Future<Void> f : workers.invokeAll(jobs)) {
                f.get();        // чтобы увидеть исключения из задач
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Один такт для всех животных одной клетки: покушать, размножиться, проголодаться, потом ходить. */
    private void processLocation(Location loc, int currentTick) {
        List<Animal> movers = new ArrayList<>();
        loc.lock();
        try {
            for (Animal animal : loc.snapshot()) {
                if (!animal.isAlive() || animal.getLastTick() == currentTick) {
                    continue;       // уже съели или уже ходило в этом такте
                }
                animal.setLastTick(currentTick);
                animal.eat(loc);
                animal.reproduce(loc, currentTick);
                animal.starve(loc);
                if (animal.isAlive()) {
                    movers.add(animal);
                }
            }
        } finally {
            loc.unlock();
        }
        for (Animal animal : movers) {
            move(animal, loc);
        }
    }

    /** Перемещение. Две клетки никогда не блокируются одновременно, поэтому deadlock невозможен. */
    private void move(Animal animal, Location from) {
        if (!animal.isAlive()) {
            return;                 // съели, пока ждало своей очереди
        }
        int[] d = animal.chooseDirection();
        int nx = clamp(from.getX() + d[0], island.getWidth() - 1);
        int ny = clamp(from.getY() + d[1], island.getHeight() - 1);
        Location to = island.getLocation(nx, ny);
        if (to == from) {
            return;
        }

        from.lock();
        try {
            if (!from.remove(animal)) {
                return;
            }
        } finally {
            from.unlock();
        }

        boolean moved = false;
        to.lock();
        try {
            if (to.count(animal.getType()) < animal.getType().getMaxPerCell()) {
                to.add(animal);
                moved = true;
            }
        } finally {
            to.unlock();
        }

        if (!moved) {               // там нет места: возвращаемся обратно
            from.lock();
            try {
                from.add(animal);
            } finally {
                from.unlock();
            }
        }
    }

    private static int clamp(int value, int max) {
        return Math.max(0, Math.min(max, value));
    }

    // ---------- Задание 3: статистика в консоль ----------

    private void printStatistics() {
        try {
            Map<SpeciesType, Integer> counts = island.countAnimals();
            int total = 0;
            StringBuilder sb = new StringBuilder("Такт ").append(tick.get()).append(" | ");
            for (SpeciesType t : SpeciesType.VALUES) {
                int c = counts.get(t);
                total += c;
                sb.append(t.getEmoji()).append(' ').append(c).append("  ");
            }
            sb.append("| ").append(Plant.EMOJI).append(' ').append(island.countPlants())
              .append(" | всего животных: ").append(total);
            System.out.println(sb);

            if (total == 0) {
                System.out.println("Все животные вымерли. Симуляция остановлена.");
                stop();
            } else if (Config.MAX_TICKS > 0 && tick.get() >= Config.MAX_TICKS) {
                System.out.println("Достигнут лимит тактов. Симуляция остановлена.");
                stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
