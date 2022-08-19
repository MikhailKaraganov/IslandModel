package settings;

import biologicalSubjects.Animal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SettingsReader {
//    C:\Users\79165\IdeaProjects\\src\main\java\settings\AnimalCommonProperties.yaml
    final static String COMMON_ANIMAL_PATH = "karaganovMikhail/IslandModel/src/main/java/settings/AnimalCommonProperties.yaml";
    private ObjectMapper objMapper = new ObjectMapper(new YAMLFactory());
    private AnimalsProperties animalsProperties;
    static AnimalsProperties ANIMALS_PROPERTIES;



    static public Boolean getStopOnTimeout() {
        return getAnimalsProperties().getStopOnTimeout();
    }

    static public int getGameDuration() {
        return getAnimalsProperties().getGameDuration();
    }

    static public long getCycleDuration() {
        return getAnimalsProperties().getCycleDuration();
    }

    static public int getMapRows() {
        return getAnimalsProperties().getMapRows();
    }

    static public int getMapCols() {
        return getAnimalsProperties().getMapCols();
    }



    public SettingsReader() {
        try {
            ObjectMapper objMapper = new ObjectMapper(new YAMLFactory());
            animalsProperties = objMapper.readValue(new File(COMMON_ANIMAL_PATH), AnimalsProperties.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ANIMALS_PROPERTIES = animalsProperties;


    }


    public synchronized static AnimalPropTemplate getAnimalProperties (Animal animal){
        String className = animal.getClass().getSimpleName();
        return getAnimalsProperties().getAnimalProperties().get(className);
    }

    public synchronized static AnimalPropTemplate getAnimalProperties (String className){
        return getAnimalsProperties().getAnimalProperties().get(className);
    }

    static public AnimalsProperties getAnimalsProperties() {
        return ANIMALS_PROPERTIES;
    }

    static public Integer probabilityToBeEaten(Animal hunter, Animal victim){
        if (hunter == null || victim == null) return 0;
        String hunterClass = hunter.getClass().getSimpleName();
        String victimClass = victim.getClass().getSimpleName();
        Integer result = SettingsReader.getAnimalsProperties().getAteProbability()
                .get(hunterClass).get(victimClass);
        if (result == null){
            return 0;
        }
        return result;
    }

    static public HashMap<String, HashMap<String, Integer>> getAteProbabilities(){
        return getAnimalsProperties().getAteProbability();
    }

    static public HashMap<String, Integer> getAteProbability(String animal){
        return getAteProbabilities().get(animal);
    }

    static public Integer getMaxChildrenNum (Animal animal){
        return SettingsReader.getAnimalsProperties().getMaxChildrenNum()
                .get(animal.getClass().getSimpleName());
    }

    static public Integer getMaxChildrenNum (String animal){
        return SettingsReader.getAnimalsProperties().getMaxChildrenNum()
                .get(animal);
    }

    static public Integer getMaxQuantity(Animal animal){
        return getAnimalProperties(animal).getMaxQuantity();
    }

    static public Integer getMaxQuantity(String bio){
        return getAnimalProperties(bio).getMaxQuantity();
    }

    static public String getIcon(String className){
        return getAnimalProperties(className).getIcon();
    }
    static synchronized public double getWeight (Animal animal){
        return getAnimalProperties(animal).getMaxWeight();
    }

    static public float getMaxFoodRequired (Animal animal){
        return getAnimalProperties(animal).getMaxFoodRequired();
    }
}




