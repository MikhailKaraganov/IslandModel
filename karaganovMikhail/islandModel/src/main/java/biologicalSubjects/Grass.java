package biologicalSubjects;

import location.Area;
import location.Coordinate;
import util.Randomizer;
import settings.SettingsReader;

import java.util.List;
import java.util.stream.Collectors;

public class Grass{
    Area area;
    String icon;
    boolean readyToReproduce;

    public Grass(Area area) {
        this.area = area;
        readyToReproduce = Randomizer.getRandomBoolean();
    }

    public void reproduce (){
        List<Grass> suitableGrass = area.getGrassOnArea().stream()
                .filter(grass -> grass.readyToReproduce).toList();
        int randomIndex = Randomizer.getRandomInt(suitableGrass.size());
        if (!suitableGrass.isEmpty()){
            suitableGrass.get(randomIndex).setReadyToReproduce(false);
        }
        int newGrass = numGrassToAdd();
        for (int i = 0; i < newGrass; i++) {
            area.getGrassOnArea().add(new Grass(area));
        }
    }

    private int numGrassToAdd (){
        int nowOnArea = this.area.getGrassOnArea().size();
        int newChildGrass = Randomizer.getRandomInt(SettingsReader.getMaxChildrenNum("Grass"));
        int newGrass = newChildGrass + nowOnArea < SettingsReader.getMaxQuantity("Grass") ? newChildGrass :
                newChildGrass - ((nowOnArea+newChildGrass) -SettingsReader.getMaxQuantity("Grass"));
        return newGrass;
    }
    public void setReadyToReproduce(boolean readyToReproduce) {
        this.readyToReproduce = readyToReproduce;
    }

}
