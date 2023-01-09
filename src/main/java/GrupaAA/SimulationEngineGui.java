package GrupaAA;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Random;

public class SimulationEngineGui extends SimulationEngine {
    private final IGuiObserver observer;
    private volatile boolean running = true;

    public SimulationEngineGui(GrassField map, Vector2d[] positions, int initHP, int birthCost, int minHP, int genLength, int dailyNewGrass, String animalBehaviour, int minMutation, int maxMutation, String mutation, int plantEnergy, IGuiObserver observer){
        super(map, positions, initHP, birthCost, minHP, genLength);
        this.dailyNewGrass = dailyNewGrass;
        this.whichBehavior = animalBehaviour;
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;
        this.whichMutation = mutation;
        this.plantEnergy = plantEnergy;
        this.observer = observer;
    }

    public void reload(){
        this.observer.reload();
    }

    public void suspend(){
        this.running = false;
    }

    public void resume(){
        this.running = true;
    }


    @Override
    public void run() {
        while(this.map.animals().size() > 0) {
            Platform.runLater(this::reload);
            map.cleanDeadAnimal();
            moveAnimals();
            eatGrasses();
            makeBabies();
            plantDailyGrass();
            System.out.println(map.animals);
            sleepThread(1000);
            while(!running){}
        }
    }

    public void sleepThread(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.out.println("interrupted exception on sleep");
        }
    }

    public void moveAnimals(){
        ArrayList<Animal> allAnimals = map.animals();
        for(int animalIndex=0; animalIndex<allAnimals.size(); animalIndex++){
            Animal animal = allAnimals.get(animalIndex);
            int genIndex =  new Random().nextInt(Genotypes.maxGenLength);
            genIndex = Variants.animalBehavior(whichBehavior, genIndex);
            animal.activatedGen = animal.animalGens[genIndex];
            animal.move(animal.animalGens[genIndex]);
        }
        sleepThread(200);

    }

    public void eatGrasses(){
        ArrayList<Grass> allGrasses = map.grasses();
        for(int grassIndex=0; grassIndex<allGrasses.size(); grassIndex++){
            Grass grass = allGrasses.get(grassIndex);
            if(map.animals.get(grass.position) != null){
                findWinner(map.animals.get(grass.position),1).get(0).eatPlant(plantEnergy);
                map.EatGrass(grass.position);
            }
        }
        sleepThread(200);

    }

    public void makeBabies(){
        ArrayList<Vector2d> positions = map.positions();
        for(int posIndex=0; posIndex<positions.size(); posIndex++){
            ArrayList<Animal> animalList = map.animalsOn(positions.get(posIndex));
            if(animalList.size() >= 2){
                ArrayList<Animal> parents = findWinner(animalList, 2);
                parents.get(0).multiplication(parents.get(1), whichMutation, minMutation, maxMutation);
            }
        }
        sleepThread(200);
    }

    public void plantDailyGrass(){
        for(int i = 0;i < dailyNewGrass; i++){
            if(!map.IsFull()) {
                map.PlantGrass();
            }
            else{
                break;
            }
        }
        sleepThread(200);
    }
}
