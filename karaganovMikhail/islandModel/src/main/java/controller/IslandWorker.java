package controller;

import location.Island;
import settings.SettingsReader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IslandWorker extends Thread{
    private final Island island;
    private final long LIFE_CYCLE_DURATION = SettingsReader.getCycleDuration();
    private final Boolean STOP_ON_TIMEOUT = SettingsReader.getStopOnTimeout();
    private final int GAME_DURATION = SettingsReader.getGameDuration();

    public IslandWorker(Island island) {
        this.island = island;
    }

    @Override
    public void run() {
        System.out.println(island.toString());
        ScheduledExecutorService gameScheduledThreadPool = Executors.newScheduledThreadPool(4);
        try {
            gameScheduledThreadPool.scheduleWithFixedDelay(this::runAndWaitAnimalWorkers, LIFE_CYCLE_DURATION, LIFE_CYCLE_DURATION, TimeUnit.MILLISECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (STOP_ON_TIMEOUT) runTimer();

    }

    private void runAndWaitAnimalWorkers() {
        ArrayList<AnimalWorker> animalWorkers = new ArrayList<>();
        for (Class animalType : Island.animalClassesList) {
            String animalClass = animalType.getSimpleName();
            animalWorkers.add(new AnimalWorker(animalClass, island));
        }
        //
        int CORE_POOL_SIZE = 4;
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(CORE_POOL_SIZE);
        for (AnimalWorker animalWorker : animalWorkers) {
            fixedThreadPool.submit(animalWorker);
        }
        fixedThreadPool.shutdown();

        try {
            if (fixedThreadPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS)) {
                System.out.println(island.toString());
            }
        } catch (InterruptedException e) {
            //
            System.out.println("The game is finished");
        }
    }

    private void runTimer() {
        try {
            Thread.sleep(GAME_DURATION);
        } catch (InterruptedException e) {
            System.out.println("The game is already finished");
        }
        //
        System.out.println("The game is over by timeout");
        System.exit(1);
    }
}
