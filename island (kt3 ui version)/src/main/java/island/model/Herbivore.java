package island.model;

public abstract class Herbivore extends Animal {

    protected Herbivore(SpeciesType type) {
        super(type);
    }

    @Override
    public void eat(Location location) {
        eatAnimals(location);
        eatPlants(location);
    }
}
