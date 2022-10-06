package util;

import biologicalSubjects.Animal;
import location.Island;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

public final class ReflectionService {

    public static Animal getAnimalInstance(Class animalClass, Island island){
        Animal animal = null;
        try {
            animal = (Animal) animalClass.getConstructor(boolean.class, Island.class)
                    .newInstance(ThreadLocalRandom.current().nextBoolean(), island);
        } catch (InstantiationException | InvocationTargetException
                 | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return animal;
    }
}
