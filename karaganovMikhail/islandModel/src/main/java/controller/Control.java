package controller;

import biologicalSubjects.Animal;
import biologicalSubjects.Bear;
import biologicalSubjects.Deer;
import biologicalSubjects.Rabbit;
import location.Area;
import location.Island;
import settings.SettingsReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

public class Control {
    public static void main(String[] args) throws InterruptedException {
        SettingsReader animalSettings = new SettingsReader();
        Island island = new Island();



//        System.out.println(island);
//        System.out.println(island.getArea(0, 0).getPopulations());
//        System.out.println(SettingsReader.getAteProbability("Bear"));
//        System.out.println(island);
//        for (int i = 0; i < 10; i++) {
//            island.getIslandArea().values().forEach(Area::perform);
//            System.out.println(island);
//        }



            IslandWorker islandWorker = new IslandWorker(island);
            islandWorker.start();


    }
}
