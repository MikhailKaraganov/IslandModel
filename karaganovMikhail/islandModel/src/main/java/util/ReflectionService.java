package util;

import biologicalSubjects.Animal;
import location.Island;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

public final class ReflectionService {
//    public static int getIntValueOfClassStaticField(Class clazz, String fieldName) {
//        int resultInt = 0;
//        Field field = null;
//        try {
//            field = clazz.getDeclaredField(fieldName);
//            field.setAccessible(true);
//            resultInt = (int) field.get(null);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return resultInt;
//    }

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
