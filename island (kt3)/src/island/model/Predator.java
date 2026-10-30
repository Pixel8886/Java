package island.model;

public abstract class Predator extends Animal {

    protected Predator(SpeciesType type) {
        super(type);
    }

    @Override
    public void eat(Location location) {
        eatAnimals(location);
    }
}
