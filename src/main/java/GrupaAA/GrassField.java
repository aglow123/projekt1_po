package GrupaAA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class GrassField implements IWorldMap, IPositionChangeObserver{
    //parametry
    int typeOfBounds;
    int numberOfGrass;
    int height;
    int width;


    Vector2d lowerLeft, upperRight;
    Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
    Map<Vector2d, Grass> grasses = new HashMap<>();
    MapBoundary mapBoundary;


    public GrassField(){
        this(new MapBoundary(),1,10,100,100);
    }

    public GrassField(MapBoundary mapBoundary, int typeOfBounds, int numberOfGrass, int height, int width){
        this.mapBoundary = mapBoundary;
        this.typeOfBounds = typeOfBounds;   //1 stands for 'globe', 2 stands for 'hell portal'
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        for(int i=0; i<numberOfGrass; i++){
            PlantGrass();
        }
    }
    abstract public void PlantGrass();

    public void EatGrass(Vector2d position){
        this.grasses.remove(position);
        mapBoundary.remove(position);
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
        mapBoundary.add(animal);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animals.containsKey(position)){
            return animals.get(position);
        }
        else if (grasses.containsKey(position)){
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
        return new Vector2d[]{mapBoundary.lowerLeft(), mapBoundary.upperRight()};
    }

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
        mapBoundary.positionChanged(oldPosition, newPosition);
    }

    public Map<Vector2d, ArrayList<Animal>> animals(){
        return animals;
    }

    @Override
    public void cleanDeadAnimal(){
        for(ArrayList<Animal> animalList: animals.values()){
            for(Animal animal : animalList){
                if(animal.HP <= 0){
                    animalList.remove(animal);
                    mapBoundary.remove(animal.getPosition());
                    animal.removeObserver(this);
                }
            }
        }
    }
}