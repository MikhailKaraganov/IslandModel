package controller;

import biologicalSubjects.Animal;
import biologicalSubjects.Bear;
import biologicalSubjects.Deer;
import biologicalSubjects.Rabbit;
import location.Area;
import location.Island;
import settings.SettingsReader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;

public class Control {
    public static void main(String[] args) throws InterruptedException {


        SettingsReader animalSettings = new SettingsReader();
        Island island = new Island();

        IslandWorker islandWorker = new IslandWorker(island);
        islandWorker.start();
    }
}
