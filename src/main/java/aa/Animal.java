package aa;

import java.util.ArrayList;

public class Animal implements IMapElement{
    private static final Vector2d DEF_POSITION = new Vector2d(2,2);
    private MapDirection orientation;
    private Vector2d position;
    private IWorldMap map;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IWorldMap map){
        this(map, DEF_POSITION);
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        map.place(this);
    }

    public String toString(){
        switch (orientation){
            case NORTH: return "^";
            case EAST: return ">";
            case SOUTH: return "v";
            case WEST: return "<";
        }
        return this.toString();
    }

    @Override
    public String getElementName() {
        switch(this.orientation){
            case NORTH: { return "up.png";}
            case WEST: { return "left.png";}
            case SOUTH: { return "down.png";}
            case EAST: { return "right.png";}
            default: { return "candy.png";}
        }
    }

    public MapDirection getOrientation(){return this.orientation;}

    public Vector2d getPosition(){return this.position;}

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public Animal move(MoveDirection direction){
        Vector2d newPosition = null;
        switch (direction) {
            case FORWARD:
                newPosition = this.position.add(this.orientation.toUnitVector());
                break;
            case BACKWARD:
                newPosition = this.position.add(this.orientation.toUnitVector().opposite());
                break;
            case RIGHT:
                this.orientation = this.orientation.next();
                newPosition = this.position;
                break;
            case LEFT:
                this.orientation = this.orientation.previous();
                newPosition = this.position;
                break;
        }
        if (this.map.canMoveTo(newPosition)){
            if(!this.map.isOccupied(newPosition)) {
                Vector2d oldPosition = this.position;
                this.position = newPosition;
                positionChanged(oldPosition, newPosition);

            }
            else if(this.map.objectAt(newPosition) instanceof Grass){
                if (map instanceof GrassField && ((GrassField) map).isPlanted(newPosition)) {
                    ((GrassField) map).EatAndPlantNewGrass(newPosition);
                    Vector2d oldPosition = this.position;
                    this.position = newPosition;
                    positionChanged(oldPosition, newPosition);
                }
            }
        }
        return this;
    }

    void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer: observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }

}