package util;

import biologicalSubjects.Animal;
import biologicalSubjects.*;

import java.util.concurrent.ThreadLocalRandom;

public final class AnimalFactory {

    static public Animal newCopyAnimal (Animal parent){
        String parentClass = parent.getClass().getSimpleName();
        Animal child =
                switch (parentClass){
                    case "Bear" -> new Bear(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Wolf" -> new Wolf(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Boa" -> new Boa(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Boar" -> new Boar(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Buffalo" -> new Buffalo(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Caterpillar" -> new Caterpillar(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Deer" -> new Deer(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Duck"-> new Duck(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Eagle" -> new Eagle(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Fox"-> new Fox(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Goat" -> new Goat(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Horse" -> new Horse(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Mouse" -> new Mouse(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Rabbit" -> new Rabbit(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    case "Sheep" -> new Sheep(ThreadLocalRandom.current().nextBoolean(), parent.getIsland());
                    default -> {
                        throw new RuntimeException("Wrong class");
                    }
        };
        return child;
    }
}
