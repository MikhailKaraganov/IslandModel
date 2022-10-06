package util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class Randomizer {

    public static boolean getRandomBoolean(){
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static int getRandomInt(int maxInt){
        int bound = Math.max(maxInt, 1);
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static boolean getProbability(int percent) {
        return getRandomInt(101) < percent;
    }

    public static Set<Integer> randomSetOfInt (int range){
        Set<Integer> resultSet = new HashSet<>();
        int currentNumber = 100;
        int setLength = Randomizer.getRandomInt(range);
        for (int i = 0; i < setLength; i++) {
            while (resultSet.contains(currentNumber) || currentNumber == 100) {
                currentNumber = Randomizer.getRandomInt(range);
                if (!resultSet.contains(currentNumber)) {
                    resultSet.add(currentNumber);
                    break;
                }
            }
        }
        return resultSet;
    }
}
