package location;

import biologicalSubjects.Animal;
import biologicalSubjects.Grass;
import biologicalSubjects.Herbivorous;
import util.Randomizer;
import settings.SettingsReader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Area {
    private final CopyOnWriteArrayList<Animal> whoOnTheArea;

    public Map<String, HashSet<Animal>> getPopulations() {
        return populations;
    }

    private Map<String, HashSet<Animal>> populations;


    public Collection<HashSet<Animal>> getAllAnimals (){
            Set<Animal> allAnimals = new HashSet<>();
        //            for (HashSet<Animal> value : values) {
//                allAnimals.addAll(value);
//            }
//            return allAnimals;
//        System.out.println(values);
        return populations.values();
    }



    private final Lock lock = new ReentrantLock();


    private CopyOnWriteArrayList<Grass> grassOnArea;

    Area() {
        whoOnTheArea = new CopyOnWriteArrayList<>();
        grassOnArea = new CopyOnWriteArrayList<>();
        populations = new ConcurrentHashMap<>();
        int grassAmount = Randomizer.getRandomInt(SettingsReader.getAnimalProperties("Grass").getMaxQuantity());
        for (int i = 0; i < grassAmount; i++) {
            grassOnArea.add(new Grass(this));
        }

        whoOnTheArea.forEach(animal -> {
            String animalClass = animal.getClass().getSimpleName();
            if(populations.containsKey(animalClass)){
                populations.get(animalClass).add(animal);
            }
            else {
                populations.put(animalClass, new HashSet<Animal>());
                populations.get(animalClass).add(animal);
            }
        });

    }

    @Override
    public String toString() {
        Map<String, Integer> countOfAnimals = new HashMap<>();
        Set<String> keys = populations.keySet();
        for (String key : keys) {
            populations.get(key).forEach(animal -> {
                String icon = animal.getIcon();
                if (countOfAnimals.containsKey(icon)) {
                    countOfAnimals.replace(icon, countOfAnimals.get(icon) + 1);
                } else {
                    countOfAnimals.put(icon, 1);
                }
            });
            String icon = SettingsReader.getIcon("Grass");
            countOfAnimals.put(icon, grassOnArea.size());
        }
        return countOfAnimals.toString();

//        whoOnTheArea.stream().map(animal -> animal.getClass().getSimpleName())
//                .forEach(animal -> {
//                    String icon = SettingsReader.getIcon(animal);
//                    if (countOfAnimals.containsKey(icon)) {
//                        countOfAnimals.replace(icon, countOfAnimals.get(icon) + 1);
//                    } else {
//                        countOfAnimals.put(icon, 1);
//                    }
//                });
//        String icon = SettingsReader.getIcon("Grass");
//        countOfAnimals.put(icon, grassOnArea.size());
//        return countOfAnimals.toString();
    }

    public List<Animal> getWhoOnTheArea() {
        return whoOnTheArea;
    }

    public void addAnimalToPopulation(Animal animal) {
        try {
            String className = animal.getClass().getSimpleName();
            if (populations.containsKey(className)) {
                populations.get(className).add(animal);
            } else {
                populations.put(className, new HashSet<>());
                populations.get(className).add(animal);
            }
        } catch (Exception e) {
            System.out.println("тут хуета");
        }
    }

    public List<Animal> getHerbivorous (){
        List<Animal> resultList = getWhoOnTheArea().stream()
                .filter(animal -> animal instanceof Herbivorous)
                .collect(Collectors.toList());
        return resultList;
    }

    public List<Grass> getGrassOnArea() {
        return grassOnArea;
    }

    public Lock getLock() {
        return lock;
    }


    public void removeAnimal (Animal animal){
        String animalType = animal.getClass().getSimpleName();
        populations.get(animalType).remove(animal);
    }
    public int getNumOfAnimalOnArea(Animal animal){
        String animalType = animal.getClassName();
//        if (populations.get(animalType) != null && !populations.get(animalType).isEmpty()){
//            return populations.get(animalType).size();
//        }
//        else return 0;
//        return populations.get(animalType).size();

        AtomicInteger numOfAnimalOnArea= new AtomicInteger();
        whoOnTheArea.stream()
                .filter(animal1 -> animal1.getClass().equals(animal.getClass()))
                .map(animal1 -> numOfAnimalOnArea.getAndIncrement());
        return numOfAnimalOnArea.get();
    }

}
