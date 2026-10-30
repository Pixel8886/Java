package island.model;

import island.model.herbivore.*;
import island.model.predator.*;

import java.util.function.Supplier;


public enum SpeciesType {
    WOLF       ("🐺", "Волк",        50,    30,  3,  8,    Wolf::new),
    BOA        ("🐍", "Удав",        15,    30,  1,  3,    Boa::new),
    FOX        ("🦊", "Лиса",         8,    30,  2,  2,    Fox::new),
    BEAR       ("🐻", "Медведь",    500,     5,  2,  80,   Bear::new),
    EAGLE      ("🦅", "Орел",         6,    20,  3,  1,    Eagle::new),
    HORSE      ("🐎", "Лошадь",     400,    20,  4,  60,   Horse::new),
    DEER       ("🦌", "Олень",      300,    20,  4,  50,   Deer::new),
    RABBIT     ("🐇", "Кролик",       2,   150,  2,  0.45, Rabbit::new),
    MOUSE      ("🐁", "Мышь",      0.05,   500,  1,  0.01, Mouse::new),
    GOAT       ("🐐", "Коза",        60,   140,  3,  10,   Goat::new),
    SHEEP      ("🐑", "Овца",        70,   140,  3,  15,   Sheep::new),
    BOAR       ("🐗", "Кабан",      400,    50,  2,  50,   Boar::new),
    BUFFALO    ("🐃", "Буйвол",     700,    10,  3,  100,  Buffalo::new),
    DUCK       ("🦆", "Утка",         1,   200,  4,  0.15, Duck::new),
    CATERPILLAR("🐛", "Гусеница",  0.01,  1000,  0,  0,    Caterpillar::new);

    public static final SpeciesType[] VALUES = values();

    private final String emoji;
    private final String title;
    private final double weight;
    private final int maxPerCell;
    private final int speed;
    private final double foodNeeded;
    private final Supplier<Animal> factory;

    SpeciesType(String emoji, String title, double weight, int maxPerCell,
                int speed, double foodNeeded, Supplier<Animal> factory) {
        this.emoji = emoji;
        this.title = title;
        this.weight = weight;
        this.maxPerCell = maxPerCell;
        this.speed = speed;
        this.foodNeeded = foodNeeded;
        this.factory = factory;
    }

    public Animal create() {
        return factory.get();
    }

    public String getEmoji() { return emoji; }
    public String getTitle() { return title; }
    public double getWeight() { return weight; }
    public int getMaxPerCell() { return maxPerCell; }
    public int getSpeed() { return speed; }
    public double getFoodNeeded() { return foodNeeded; }
}
