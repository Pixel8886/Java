package island.model;

/**
 * Травоядные едят растения. Часть из них по таблице №1 ест и животных
 * (утка и мышь едят гусениц, кабан ест мышей и гусениц), поэтому сначала
 * пробуем животных, потом растения.
 */
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
