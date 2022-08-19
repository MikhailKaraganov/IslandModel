package controller;

import biologicalSubjects.Animal;

public class AnimalTask {
    private final Animal animal;

    public AnimalTask(Animal animal) {
        this.animal = animal;
    }

    public void perform() {
        if (!animal.isDead()){
            animal.reproduce();
            animal.move();
            animal.eat();
        }
        if (animal.isDead()){
            animal.killAnimal();
        }
    }
}
