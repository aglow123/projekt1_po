package GrupaAA;

import javafx.application.Platform;

import java.util.ArrayList;

public class SimulationEngineGui extends SimulationEngine {
    private IGuiObserver observer;

    private final int moveDelay = 300;

    public SimulationEngineGui(GrassField map, Vector2d[] positions, IGuiObserver observer){
        super(map,positions);
        this.observer = observer;
    }

    void reload(){
        this.observer.reload();
    }

    @Override
    public void run() {
        Platform.runLater(this::reload);
        if (this.map.animals().size() == 0) {
            System.out.println("animals empty");
            return;
        }
        moveAnimals();
        eatGrasses();
        makeBabies();
    }

    public void sleepThread(){
        try {
            Thread.sleep(moveDelay);
        } catch (InterruptedException ex) {
            System.out.println("interrupted exception on sleep");
        }
    }

    public void moveAnimals(){
        for(ArrayList<Animal> animalList: map.animals().values()){
            for(Animal animal : animalList){
                sleepThread();
                int genIndex = day % Genotypes.maxGenLength; //zrobic jakis random
                genIndex = Variants.animalBehavior(whichBehavior, genIndex);
                animal.move(animal.animalGens[genIndex]);
                Platform.runLater(this::reload);
            }
        }
    }

    public void eatGrasses(){
        for(Grass grass: map.grasses.values()){
            if(map.animals.get(grass.position) != null){
                sleepThread();
                findWinner(map.animals.get(grass.position),1).get(0).raiseHP(plantEnergy);
                map.EatGrass(grass.position);
                Platform.runLater(this::reload);
            }
        }
    }

    public void makeBabies(){
        for(ArrayList<Animal> animalList: map.animals().values()){
            if(animalList.size() >= 2){
                sleepThread();
                ArrayList<Animal> parents = findWinner(animalList, 2);
                parents.get(0).multiplication(parents.get(1), "mutation");
                Platform.runLater(this::reload);
            }
        }
    }
}
