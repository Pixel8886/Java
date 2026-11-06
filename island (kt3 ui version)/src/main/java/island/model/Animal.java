package island.model;

import island.config.Config;

/** Общее поведение всех животных. */
public abstract class Animal {

    private final SpeciesType type;
    private double satiety;                 // сколько кг уже съедено (насыщение)
    private volatile boolean alive = true;
    private volatile int lastTick = -1;     // чтобы животное не ходило дважды за такт

    protected Animal(SpeciesType type) {
        this.type = type;
        this.satiety = type.getFoodNeeded() * Config.START_SATIETY;
    }


    public abstract void eat(Location location);


    protected void eatAnimals(Location location) {
        double need = type.getFoodNeeded();
        for (int i = 0; i < Config.EAT_ATTEMPTS && satiety < need; i++) {
            SpeciesType preyType = pickPrey(location);
            if (preyType == null) {
                return;
            }
            if (RandomUtil.chance(Config.eatChance(type, preyType))) {
                Animal prey = location.randomOf(preyType);
                if (prey != null && prey != this) {
                    prey.die(location);
                    satiety = Math.min(need, satiety + preyType.getWeight());
                }
            }
        }
    }


    protected void eatPlants(Location location) {
        double need = type.getFoodNeeded() - satiety;
        if (need <= 0 || !RandomUtil.chance(Config.plantEatChance(type))) {
            return;
        }
        int wanted = (int) Math.ceil(need / Plant.WEIGHT);
        int eaten = location.takePlants(wanted);
        satiety = Math.min(type.getFoodNeeded(), satiety + eaten * Plant.WEIGHT);
    }


    private SpeciesType pickPrey(Location location) {
        SpeciesType chosen = null;
        int seen = 0;
        for (SpeciesType t : SpeciesType.VALUES) {
            if (Config.eatChance(type, t) > 0 && location.count(t) > 0) {
                seen++;
                if (RandomUtil.nextInt(1, seen) == 1) {
                    chosen = t;
                }
            }
        }
        return chosen;
    }


    public void reproduce(Location location, int tick) {
        if (location.count(type) < 2
                || satiety < type.getFoodNeeded() * 0.5
                || !RandomUtil.chance(Config.REPRODUCE_CHANCE)) {
            return;
        }
        for (int i = 0; i < Config.litter(type) && location.count(type) < type.getMaxPerCell(); i++) {
            Animal child = type.create();
            child.lastTick = tick;          // детёныш начнёт действовать со следующего такта
            location.add(child);
        }
    }


    public int[] chooseDirection() {
        int steps = RandomUtil.nextInt(0, type.getSpeed());
        int dx = 0;
        int dy = 0;
        for (int i = 0; i < steps; i++) {
            switch (RandomUtil.nextInt(0, 3)) {
                case 0 -> dx++;
                case 1 -> dx--;
                case 2 -> dy++;
                default -> dy--;
            }
        }
        return new int[]{dx, dy};
    }

    /** Проголодаться; если насыщение кончилось, животное умирает. */
    public void starve(Location location) {
        double need = type.getFoodNeeded();
        if (need <= 0) {
            return;
        }
        satiety -= need * Config.HUNGER_RATE;
        if (satiety <= 0) {
            die(location);
        }
    }

    public void die(Location location) {
        alive = false;
        location.remove(this);
    }

    public SpeciesType getType() { return type; }
    public boolean isAlive() { return alive; }
    public int getLastTick() { return lastTick; }
    public void setLastTick(int tick) { this.lastTick = tick; }
}
