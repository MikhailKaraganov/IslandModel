package settings;



import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//@JsonIgnoreProperties(ignoreUnknown = true)
public final class AnimalsProperties implements Serializable {
    private HashMap<String, AnimalPropTemplate> animalProperties = new HashMap<>();

    private HashMap<String, HashMap<String, Integer>> ateProbability = new HashMap<>();

    private HashMap<String, Integer> maxChildrenNum = new HashMap<>();

    private Boolean stopOnTimeout;
    private Integer gameDuration;
    private Long cycleDuration;
    private int mapRows;
    private int mapCols;


    public AnimalsProperties() {
    }


    public HashMap<String, HashMap<String, Integer>> getAteProbability() {
        return ateProbability;
    }

    public HashMap<String, Integer> getMaxChildrenNum() {
        return maxChildrenNum;
    }

    public HashMap<String, AnimalPropTemplate> getAnimalProperties() {
        return animalProperties;
    }

    public Boolean getStopOnTimeout() {
        return stopOnTimeout;
    }

    public Integer getGameDuration() {
        return gameDuration;
    }

    public Long getCycleDuration() {
        return cycleDuration;
    }

    public int getMapRows() {
        return mapRows;
    }

    public int getMapCols() {
        return mapCols;
    }
}
