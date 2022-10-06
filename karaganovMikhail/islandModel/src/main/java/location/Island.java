package location;

import biologicalSubjects.*;
import util.Randomizer;
import util.ReflectionService;
import settings.SettingsReader;

import java.util.*;

public class Island {
    public static final List<Class> animalClassesList = Arrays.asList(Bear.class, Boar.class, Boa.class, Buffalo.class, Caterpillar.class, Deer.class,
            Duck.class, Eagle.class, Fox.class, Goat.class, Horse.class, Mouse.class, Rabbit.class, Sheep.class, Wolf.class);

    public static final int islandLength = SettingsReader.getMapRows();
    public static final int islandWidth = SettingsReader.getMapCols();

    private Map<Coordinate, Area> islandArea;


    public Island() {
        islandArea = new LinkedHashMap<>();
        initializeArea();
        System.out.println("Ostrov sozdan");
    }


    private void initializeArea() {
        for (int i = 0; i < islandLength; i++) {
            for (int j = 0; j < islandWidth; j++) {
                islandArea.put(new Coordinate(i, j), new Area());
                synchronized (this.getArea(i,j)){
                    int grassAmount = Randomizer.getRandomInt(SettingsReader.getMaxQuantity("Grass"));
                    for (int k = 0; k < grassAmount; k++) {
                        addGrassOnArea(i, j);
                    }
                    ArrayList<Animal> animalsForArea = generatePullOfAnimalsForArea();
                    for (Animal animal : generatePullOfAnimalsForArea()) {
                        addAnimalOnArea(i, j, animal);

                    }
                }
            }
        }
    }

    private ArrayList<Animal> generatePullOfAnimalsForArea() {
        ArrayList<Animal> animalsForArea = new ArrayList<>();
        Set<Integer> chosenClasses = Randomizer.randomSetOfInt(animalClassesList.size());
        for (Integer numOfClass : chosenClasses) {
            Class animClass = animalClassesList.get(numOfClass);
            int maxNumItemOfType = SettingsReader.getAnimalProperties(animClass.getSimpleName())
                    .getMaxQuantity();
            int numAnimItem = Randomizer.getRandomInt(maxNumItemOfType + 1);
            for (int i = 0; i < numAnimItem; i++) {
                Animal animal = ReflectionService.getAnimalInstance(animClass, this);
//                allAnimalsOfIsland.add(animal);
                animalsForArea.add(animal);
            }
        }
        return animalsForArea;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < islandLength; i++) {
            for (int j = 0; j < islandWidth; j++) {
                result += "Area " + i + ":" + j + " ";
                result += getArea(i,j).toString();
                result += "\n";
            }
        }
        result += "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";
        return result;
    }

    public Map<Coordinate, Area> getIslandArea() {
        return islandArea;
    }

    public Area getArea(int x, int y) {
        return getIslandArea().get(new Coordinate(x, y));
    }

    public Area getArea(Coordinate coordinate) {
        return getIslandArea().get(coordinate);
    }

    private void addGrassOnArea(int i, int j) {
        Area area = this.getArea(i, j);
        area.getGrassOnArea().add(new Grass(area));
    }

    public void addAnimalOnArea(int x, int y, Animal animal) {
//            getArea(x, y).addAnimalToPopulation(animal);
//            getArea(x, y).getWhoOnTheArea().add(animal);
            getArea(x, y).addAnimalToPopulation(animal);
            animal.setCoordinate(x, y);
    }

    public void addAnimalOnArea(Coordinate coordinate, Animal animal) {
//        getArea(coordinate).getWhoOnTheArea().add(animal);
        getArea(coordinate).addAnimalToPopulation(animal);
        animal.setCoordinate(coordinate);
    }
}
