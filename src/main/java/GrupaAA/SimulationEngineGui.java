package GrupaAA;

import javafx.application.Platform;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimulationEngineGui extends SimulationEngine {
    private IGuiObserver observer;

    private final int moveDelay = 300;

    public SimulationEngineGui(GrassField map, Vector2d[] positions, IGuiObserver observer, String chosenBehaviour){
        super(map, positions, chosenBehaviour);
        this.observer = observer;
    }

    void reload(){
        this.observer.reload();
    }

    @Override
    public void run() {
        Platform.runLater(this::reload);

        while(this.map.animals().size() > 0) {
//            sleepThread();
            moveAnimals();
            eatGrasses();
            makeBabies();
            System.out.println(map.animals);
        }

    }

    public void sleepThread(){
        try {
            Thread.sleep(moveDelay);
        } catch (InterruptedException ex) {
            System.out.println("interrupted exception on sleep");
        }
    }

    public void moveAnimals(){
        ArrayList<Animal> allAnimals = map.animals();
        for(int animalIndex=0; animalIndex<allAnimals.size(); animalIndex++){
            Animal animal = allAnimals.get(animalIndex);
            sleepThread();
            int genIndex =  day % Genotypes.maxGenLength; //zrobic jakis random
            genIndex = Variants.animalBehavior(whichBehavior, genIndex);
            animal.move(animal.animalGens[genIndex]);
        }
        Platform.runLater(this::reload);
    }

    public void eatGrasses(){
        ArrayList<Grass> allGrasses = map.grasses();
        for(int grassIndex=0; grassIndex<allGrasses.size(); grassIndex++){
            Grass grass = allGrasses.get(grassIndex);
            if(map.animals.get(grass.position) != null){
                sleepThread();
                findWinner(map.animals.get(grass.position),1).get(0).raiseHP(plantEnergy);
                map.EatGrass(grass.position);
                Platform.runLater(this::reload);
            }
        }
    }

    public void makeBabies(){
        ArrayList<Vector2d> positions = map.positions();
        for(int posIndex=0; posIndex<positions.size(); posIndex++){
            ArrayList<Animal> animalList = map.animalsOn(positions.get(posIndex));
            if(animalList.size() >= 2){
                System.out.println("makebabies");
                sleepThread();
                ArrayList<Animal> parents = findWinner(animalList, 2);
                parents.get(0).multiplication(parents.get(1), "mutation");
                Platform.runLater(this::reload);
            }
        }
    }
}
