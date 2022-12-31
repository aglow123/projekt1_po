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
    Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    Map<Vector2d, Grass> grasses = new HashMap<>();
    Map<Vector2d, Integer> corpsesList = new HashMap<>();

    abstract public void PlantGrass();

    public void EatGrass(Vector2d position){
        this.grasses.remove(position);
//        mapBoundary.remove(position);
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
//        mapBoundary.add(animal);
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
        System.out.println(position);
        return grasses.getOrDefault(position, null);
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
//    public Vector2d[] setBorders(){
//        return new Vector2d[]{mapBoundary.lowerLeft(), mapBoundary.upperRight()};
//    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        ArrayList<Animal> animalList = animals.get(oldPosition);
        for(Animal animal: animalList){
            if(animal.getPosition() == newPosition){
                animalList.remove(animal);
                if (!this.isOccupied(animal.getPosition())) {
                    animals.put(animal.getPosition(), new ArrayList<>());
                }
                animals.get(animal.getPosition()).add(animal);
            }
        }
//        mapBoundary.positionChanged(oldPosition, newPosition);
    }

    public Map<Vector2d, ArrayList<Animal>> animals(){
        return animals;
    }

    @Override
    public void cleanDeadAnimal(){
        for(ArrayList<Animal> animalList: animals.values()){
            for(Animal animal : animalList){
                if(animal.HP <= 0){
                    if(corpsesList.containsKey(animal.getPosition()))
                        corpsesList.put(animal.getPosition(),corpsesList.get(animal.getPosition())+1);
                    else
                        corpsesList.put(animal.getPosition(), 1);
                    animalList.remove(animal);
//                    mapBoundary.remove(animal.getPosition());
                    animal.removeObserver(this);
                }
            }
        }
    }
}