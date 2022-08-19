package controller;

import biologicalSubjects.Animal;
import biologicalSubjects.Grass;
import location.Area;
import location.Island;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AnimalWorker implements Runnable {

    private final Island island;
    private final String animalType;
    private final Queue<AnimalTask> tasks = new ConcurrentLinkedQueue<>();

    public AnimalWorker(String animalType, Island island) {
        this.animalType = animalType;
        this.island = island;
    }

    @Override
    public void run() {
        List<Area> areas = island.getIslandArea().values().stream().toList();
        for (Area area : areas) {
            createAndPerformTasks(area);
        }
    }

    public void createAndPerformTasks(Area area){
        Set<Animal> animals = area.getPopulations().get(animalType);
        if (animals != null) {
            area.getLock().lock();
            try {
                for (Animal animal : animals){
                    tasks.add(new AnimalTask(animal));
                }
            } finally {
                area.getLock().unlock();
            }
        }
        tasks.forEach(AnimalTask::perform);
        tasks.clear();

    }
}

