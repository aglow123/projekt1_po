package aa;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    Vector2d lowerLeft, upperRight;
    Map<Vector2d, Animal> animals = new HashMap<>();

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    @Override
    public boolean place(Animal animal) throws IllegalArgumentException{
        if (!canMoveTo(animal.getPosition())){
            throw new IllegalArgumentException("You cannot place an animal in the position outside the map");
        }
        if (this.isOccupied(animal.getPosition())){
            throw new IllegalArgumentException("You cannot place an animal in the position of another animal");
        }
        animals.put(animal.getPosition(), animal);
        animal.addObserver(this);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public String toString() {
        Vector2d[] borders = setBorders();
        return new MapVisualizer(this).draw(borders[0], borders[1]);
    }

    abstract public Vector2d[] setBorders();

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        Animal animal = animals.get(oldPosition);
        animals.remove(oldPosition);
        animals.put(newPosition, animal);
    }

    public String animals(){
        return animals.toString();
    }
}
