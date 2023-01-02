package GrupaAA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GrassField implements IWorldMap, IPositionChangeObserver{
    //parametry
    int typeOfBounds;
    int numberOfGrass;
    int height;
    int width;

    Vector2d lowerLeft, upperRight;
    public Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    Map<Vector2d, Grass> grasses = new HashMap<>();
    Map<Vector2d, Integer> corpsesList = new HashMap<>();

    abstract public void PlantGrass();

    public void EatGrass(Vector2d position){
        this.grasses.remove(position);
    }

    public boolean IsFull(){
        return grasses.size() == upperRight.x * upperRight.y;
    }

    @Override
    public void place(Animal animal) throws IllegalArgumentException{
        if (!this.isOccupied(animal.getPosition())) {
            animals.put(animal.getPosition(), new ArrayList<>());
        }
        animals.get(animal.getPosition()).add(animal);
        animal.addObserver(this);
    }

    @Override
    public ArrayList<Animal> animalsAt(Vector2d position) {
        if (animals.containsKey(position)){
            return animals.get(position);
        }
        return null;
    }

    @Override
    public Grass grassAt(Vector2d position){
        if (grasses.containsKey(position)){
            return grasses.get(position);
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public boolean isPlanted(Vector2d position){return grasses.containsKey(position);}

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

    @Override
    public void cleanDeadAnimal(){
        ArrayList<Animal> allAnimals = animals();
        for(int animalIndex=0; animalIndex<allAnimals.size(); animalIndex++){
            Animal animal = allAnimals.get(animalIndex);
            if(animal.HP <= 0){
                if(corpsesList.containsKey(animal.getPosition()))
                    corpsesList.put(animal.getPosition(),corpsesList.get(animal.getPosition())+1);
                else {
                    corpsesList.put(animal.getPosition(), 1);
                }
                animals.get(animal.getPosition()).remove(animal);
                if(animals.get(animal.getPosition()).size() == 0){
                    animals.remove(animal.getPosition());
                }
                animal.removeObserver(this);
            }
        }
    }
}