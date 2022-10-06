package location;

import java.util.concurrent.ThreadLocalRandom;


public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return 11 * this.getX() + 13 * this.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (this.hashCode() == obj.hashCode() && obj.getClass().equals(this.getClass())) {
            return this.getX() == ((Coordinate) obj).getX() &&
                    this.getY() == ((Coordinate) obj).getY();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getX() + ":" + getY();
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Coordinate newCoordinate (Coordinate oldCoordinate, int steps){
        int x = oldCoordinate.getX();
        int y = oldCoordinate.getY();
        for (int i = 0; i < steps ; i++) {
            boolean xOrY = ThreadLocalRandom.current().nextBoolean();
            if(xOrY){
                if(x == 0){
                    x +=1;
                }
                else {
                    boolean plusOrMinus = ThreadLocalRandom.current().nextBoolean();
                    if(plusOrMinus && x < Island.islandLength-1){
                        x +=1;
                    }
                    else {
                        x -=1;
                    }
                }
            }
            else {
                if(y == 0){
                    y +=1;
                }
                else {
                    boolean plusOrMinus = ThreadLocalRandom.current().nextBoolean();
                    if(plusOrMinus && y < Island.islandWidth - 1){
                        y +=1;
                    }
                    else {
                        y -=1;
                    }
                }
            }
        }
        return new Coordinate(x,y);
    }
}
