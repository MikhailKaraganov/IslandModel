package biologicalSubjects;


import location.Island;

public class Duck extends Animal implements Herbivorous , Predator{



    public Duck(boolean sex, Island island) {
        super(sex, island);
    }
}
