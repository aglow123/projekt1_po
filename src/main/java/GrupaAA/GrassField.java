package GrupaAA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static GrupaAA.ToxicCorpses.valueSort;

public abstract class GrassField implements IWorldMap, IPositionChangeObserver {

    int day;
    int typeOfBounds;
    Vector2d lowerLeft, upperRight;
    Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    Map<Vector2d, Grass> grasses = new HashMap<>();
    Map<Vector2d, ArrayList<Animal>> corpsesList = new HashMap<>();

    abstract public void PlantGrass();

    public void EatGrass(Vector2d position) {
        this.grasses.remove(position);
    }

    public boolean IsFull() {
        return grasses.size() == upperRight.x * upperRight.y;
    }

    @Override
    public void place(Animal animal) throws IllegalArgumentException {
        if (!this.isOccupied(animal.getPosition())) {
            animals.put(animal.getPosition(), new ArrayList<>());
        }
        animals.get(animal.getPosition()).add(animal);
        animal.addObserver(this);
    }

    @Override
    public ArrayList<Animal> animalsAt(Vector2d position) {
        if (animals.containsKey(position)) {
            return animals.get(position);
        }
        return null;
    }

    @Override
    public Grass grassAt(Vector2d position) {
        if (grasses.containsKey(position)) {
            return grasses.get(position);
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public boolean isPlanted(Vector2d position) {
        return grasses.containsKey(position);
    }

    public int howManyFreeFields() {
        int free = 0;
        for (int i = this.lowerLeft.x; i < this.upperRight.x; i++) {
            for (int j = this.lowerLeft.y; j < this.upperRight.y; j++) {
                if (!isOccupied(new Vector2d(i, j)) && !isPlanted(new Vector2d(i, j))) {
                    free += 1;
                }
            }
        }
        return free;
    }

    public int popularGenotype() {
        Map<Integer, Integer> gens = new HashMap<>();
        if(animals().size() == 0){
            return -1;
        }
        for (Animal animal : animals()) {
            for (int gen : animal.animalGens) {
                if (gens.containsKey(gen)) {
                    gens.put(gen, gens.get(gen) + 1);
                } else {
                    gens.put(gen, 1);
                }
            }
        }
        gens = valueSort(gens);
        return (int) gens.keySet().toArray()[0];
    }

    public double averageHP(){
        int amount = animals().size();
        if(amount == 0){
            return 0.0;
        }
        int sum = 0;
        for(Animal animal: animals()){
            sum += animal.HP;
        }
        return sum * 1.0/amount;
    }

    public double averageAge(){
        int amount = animals().size() + corpses().size();
        if(amount == 0){
            return 0.0;
        }
        int sum = 0;
        for(Animal animal: animals()){
            sum += animal.age;
        }
        for(Animal animal: corpses()){
            sum += animal.age;
        }
        return sum * 1.0/amount;
    }

    public ArrayList<Animal> corpses(){
        ArrayList<Animal> allCorpses = new ArrayList<>();
        for(ArrayList<Animal> animalList : corpsesList.values()){
            allCorpses.addAll(animalList);
        }
        return allCorpses;
    }

    public String toString() {
        Vector2d[] borders = setBorders();
        return new MapVisualizer(this).draw(borders[0], borders[1]);
    }

    public Vector2d[] setBorders(){
        return new Vector2d[]{this.lowerLeft, this.upperRight};
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        ArrayList<Animal> animalList = new ArrayList<>(animals.get(oldPosition));
        for(Animal animal: animalList){
            if(animal.getPosition() == newPosition){
                animals.get(oldPosition).remove(animal);
                if(animals.get(oldPosition).size() == 0){
                    animals.remove(oldPosition);
                }
                if (!this.isOccupied(animal.getPosition())) {
                    animals.put(animal.getPosition(), new ArrayList<>());
                }
                animals.get(animal.getPosition()).add(animal);
            }
        }
    }

    public ArrayList<Animal> animals(){
        ArrayList<Animal> allAnimals = new ArrayList<>();
        for(ArrayList<Animal> animalList : animals.values()){
            allAnimals.addAll(animalList);
        }
        return allAnimals;
    }

    public ArrayList<Animal> animalsOn(Vector2d position){
        return new ArrayList<>(animals.get(position));
    }

    public ArrayList<Vector2d> positions(){
        return new ArrayList<>(animals.keySet());
    }

    public ArrayList<Grass> grasses(){
        return new ArrayList<>(grasses.values());
    }

    public void nextDay(){
        this.day += 1;
    }

    @Override
    public void cleanDeadAnimal(){
        ArrayList<Animal> allAnimals = animals();
        for(int animalIndex=0; animalIndex<allAnimals.size(); animalIndex++){
            Animal animal = allAnimals.get(animalIndex);
            if(animal.HP <= 0){
                if(!corpsesList.containsKey(animal.getPosition())) {
                    corpsesList.put(animal.getPosition(), new ArrayList<>());
                }
                corpsesList.get(animal.getPosition()).add(animal);
                animals.get(animal.getPosition()).remove(animal);
                if(animals.get(animal.getPosition()).size() == 0){
                    animals.remove(animal.getPosition());
                }
                animal.removeObserver(this);
                animal.dayOfDeath = day;
                animal.isAlive = false;
            }
        }
    }
}