package biologicalSubjects;

import location.Area;
import location.Coordinate;
import location.Island;
import util.AnimalFactory;
import util.Randomizer;
import settings.AnimalPropTemplate;
import settings.SettingsReader;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public abstract class Animal {
    private static volatile AtomicInteger countOfAllAnimals = new AtomicInteger(0);
    private static int maxItemOnArea;
    private boolean readyToReproduce;

    private boolean isDead;
    private String icon;


    private double weigh;


    private Island island;


    private int hungryLevel;


    private int maxSpeed;


    private boolean gender;
    private Coordinate coordinate;


    @Override
    public String toString() {
        return "Animal{" +
                "className=" + this.getClassName() +
                ", icon= " + getIcon() +
                ", hungryLevel=" + hungryLevel +
                ", gender=" + gender +
                ", coordinate=" + coordinate +
                ", maxSpeed=" + maxSpeed +
                ", weigh=" + weigh +
                ", maxItemOnArea=" + maxItemOnArea +
                ", isDead=" + isDead +
                '}';
    }

    public Animal(boolean gender, Island island) {
        Animal.countOfAllAnimals.incrementAndGet();
        this.isDead = false;
        this.gender = gender;
        this.island = island;
        AnimalPropTemplate animalPropTemplate = SettingsReader.getAnimalProperties(this);
        setIcon(animalPropTemplate.getIcon());
        setMaxItemOnArea(animalPropTemplate.getMaxQuantity());
        setMaxSpeed(animalPropTemplate.getMaxSpeed());
        setWeigh(animalPropTemplate.getMaxWeight());
        setHungryLevel(Randomizer.getRandomInt(101));
        setReadyToReproduce(Randomizer.getRandomBoolean());
    }

    public synchronized void move() {
        Area currentArea = island.getArea(this.coordinate);
        currentArea.getLock().lock();
        try {
            int steps = Randomizer.getRandomInt(this.getMaxSpeed() + 1);
            Coordinate newCoordinate = Coordinate.newCoordinate(this.coordinate, steps);
            int countForStop = 0;
            while (island.getArea(newCoordinate).getNumOfAnimalOnArea(this) >= SettingsReader.getMaxQuantity(this)) {
                newCoordinate = Coordinate.newCoordinate(this.coordinate, steps);
                countForStop++;

                if (countForStop > 10) {
                    break;
                }
            }
            for (int i = 0; i < steps; i++) {
                this.incrementHungryLevel();
            }
            this.island.getArea(this.coordinate).getPopulations().get(this.getClassName()).remove(this);
            this.setCoordinate(newCoordinate);
            this.island.addAnimalOnArea(newCoordinate, this);
            if (this.getHungryLevel() > 100) {
                this.setDead(true);
            }
        } finally {
            currentArea.getLock().unlock();
        }
    }

    public void eat() {
        Area currentArea = island.getArea(this.coordinate);
        currentArea.getLock().lock();
        try {
            double currentHungry = this.getHungryLevel() / 100.00;
            double foodNeedToFullSatisfy = SettingsReader.getMaxFoodRequired(this);
            double foodNeedForSatisfyNow = currentHungry * foodNeedToFullSatisfy;
            if (this instanceof Herbivorous) {
                int grassAmountOnArea = currentArea.getGrassOnArea().size();
                double grassToEat = Math.min(foodNeedForSatisfyNow, grassAmountOnArea);
                for (int i = 0; i < grassToEat; i++) {
                    if (currentArea.getGrassOnArea().size() > 0) {
                        currentArea.getGrassOnArea().remove(currentArea.getGrassOnArea().get(0));
                        foodNeedForSatisfyNow--;
                    }
                    if (foodNeedForSatisfyNow == 0) {
                        break;
                    }
                    this.setHungryLevel((int) ((foodNeedForSatisfyNow / foodNeedToFullSatisfy) * 100));
                }
            }
            //For Predators
            if (this instanceof Predator) {
                Set<String> classToEat = SettingsReader.getAteProbability(this.getClassName()).keySet();
                while (foodNeedForSatisfyNow > 0 && !classToEat.isEmpty() && this.getHungryLevel() < 100) {
                    Iterator<String> iter = classToEat.iterator();
                    while (true) {
                        if (iter.hasNext()) {
                            String victimClass = iter.next();
                            HashSet<Animal> animalsToAte = currentArea.getPopulations().get(victimClass);
                            if (animalsToAte != null) {
                                Iterator<Animal> iterator = animalsToAte.iterator();
                                while (this.getHungryLevel() > 0) {
                                    if (iterator.hasNext()) {
                                        Animal victim = iterator.next();
                                        double victimWeight = SettingsReader.getWeight(victim);
                                        int probabilityToEat = SettingsReader.probabilityToBeEaten(this, victim);
                                        if (Randomizer.getProbability(probabilityToEat)) {
                                            victim.setDead(true);
                                            foodNeedForSatisfyNow -= Math.min(victimWeight, foodNeedForSatisfyNow);
                                            this.setHungryLevel((int) ((foodNeedForSatisfyNow / foodNeedToFullSatisfy) * 100));
                                        }
                                    } else break;
                                }
                            }
                            if (!iter.hasNext() || this.getHungryLevel() > 100) {
                                this.incrementHungryLevel();
                                break;
                            } else {
                                this.incrementHungryLevel();
                            }
                        } else break;
                    }
                }
            }
            if (this.hungryLevel >= 100) {
                this.isDead = true;
            }
        } finally {
            currentArea.getLock().unlock();
        }
    }


    public void reproduce() {
        if (this.getHungryLevel() < 70 && this.isReadyToReproduce()) {
            Area currentArea = island.getArea(this.coordinate);
            currentArea.getLock().lock();
            try {
                Set<Animal> suitableAnimals = new HashSet<>();
                ArrayList<Animal> animals;
                Iterator<Animal> iterator = currentArea.getPopulations().get(this.getClassName()).iterator();
                while (true) {
                    if (iterator.hasNext()) {
                        Animal suitAnimal = iterator.next();
                        if (suitAnimal.isGender() != this.isGender() && suitAnimal.isReadyToReproduce()) {
                            suitableAnimals.add(suitAnimal);
                        }
                    } else break;
                }
                if (Randomizer.getProbability(50) && !suitableAnimals.isEmpty()) {
                    int currentAmountOfThisType = currentArea.getPopulations().get(this.getClassName()).size();
                    Iterator<Animal> iterator1 = suitableAnimals.iterator();
                    if (iterator1.hasNext()) {
                        Animal suitAnimal = iterator1.next();
                        suitAnimal.setReadyToReproduce(false);
                        this.setReadyToReproduce(false);
                        int numOfChildren = numChildToAdd(currentAmountOfThisType);
                        for (int i = 0; i < numOfChildren; i++) {
                            island.addAnimalOnArea(this.coordinate, AnimalFactory.newCopyAnimal(this));
                        }
                    }
                }
            } finally {
                currentArea.getLock().unlock();
            }
        }
        this.incrementHungryLevel();
    }

    private int numChildToAdd(int currentAmount) {
        Integer maxChildrenNum = SettingsReader.getMaxChildrenNum(this);
        int maxOfThisTypeOnArea = SettingsReader.getMaxQuantity(this);
        if (currentAmount >= maxOfThisTypeOnArea) {
            return 0;
        }
        int possibleNewAmount = Randomizer.getRandomInt(maxChildrenNum);
        return Math.min(maxOfThisTypeOnArea - currentAmount, possibleNewAmount);
    }

    public void killAnimal() {
        getIsland().getArea(this.getCoordinate()).removeAnimal(this);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Island getIsland() {
        return island;
    }

    public int getMaxSpeed() {
        return this.maxSpeed;
    }

    public void setCoordinate(int x, int y) {
        this.coordinate = new Coordinate(x, y);
    }

    protected void setMaxItemOnArea(int maxItemOnArea) {
        this.maxItemOnArea = maxItemOnArea;
    }

    public static int getMaxItemOnArea() {
        return maxItemOnArea;
    }

    public boolean isGender() {
        return gender;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setHungryLevel(int hungryLevel) {
        if (!this.getClassName().equals("Caterpillar")) {
            this.hungryLevel = Math.max(hungryLevel, 0);
        }
    }

    public void setReadyToReproduce(boolean readyToReproduce) {
        this.readyToReproduce = readyToReproduce;
    }

    public boolean isReadyToReproduce() {
        return readyToReproduce;
    }

    protected void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public int getHungryLevel() {
        return Math.max(hungryLevel, 0);
    }

    public double getWeigh() {
        return weigh;
    }

    private void setWeigh(double weigh) {
        this.weigh = weigh;
    }

    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    public void incrementHungryLevel() {
        this.hungryLevel++;
    }


    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
