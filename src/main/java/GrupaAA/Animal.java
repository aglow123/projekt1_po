package main.java.GrupaAA;

import java.util.ArrayList;

public class Animal implements IMapElement {
    private static final Vector2d DEF_POSITION = new Vector2d(2,2);
    private MapDirection orientation;
    private Vector2d position;
    protected HealthPoints HP;
    private IWorldMap map;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IWorldMap map){
        this(map, DEF_POSITION);
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.HP = new HealthPoints(100);
        map.place(this);
    }

    public String toString(){
        return switch (orientation) {
            case NORTH -> "^";
            case EAST -> ">";
            case SOUTH -> "v";
            case WEST -> "<";
            case NORTHWEST -> "NW";
            case NORTHEAST -> "NE";
            case SOUTHWEST -> "SW";
            case SOUTHEAST -> "SE";
            default -> this.toString();
        };
    }
    public MapDirection getOrientation(){return this.orientation;}

    public Vector2d getPosition(){return this.position;}
    @Override
    public String getElementName() {
        switch(this.orientation){
            case NORTH: { return "0.png";}
            case WEST: { return "6.png";}
            case SOUTH: { return "4.png";}
            case EAST: { return "2.png";}
            case NORTHWEST: { return "7.png";}
            case SOUTHEAST: { return "3.png";}
            case SOUTHWEST: { return "5.png";}
            case NORTHEAST: { return "1.png";}
            default: { return "candy.png";}
        }
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    //powrzucalam funckje ktore mi beda potrzebne do mapy, jakby cos bylo nie zrozumiale - pytaj
    void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public Animal move(MoveDirection direction){
        Vector2d newPosition = null;
        switch(direction) {
            case FORWARD -> {
                newPosition = position.add(orientation.toUnitVector());
            }
            case FORWARD_RIGHT -> {
                orientation = orientation.next();
                newPosition = position.add(orientation.toUnitVector());
            }
            case RIGHT -> {
                orientation = (orientation.next()).next();
                newPosition = position.add(orientation.toUnitVector());
            }
            case BACKWARD_RIGHT -> {
                orientation = ((orientation.next()).next()).next();
                newPosition = position.subtract(orientation.toUnitVector());
            }
            case BACKWARD -> {
                newPosition = position.subtract(orientation.toUnitVector());
            }
            case BACKWARD_LEFT -> {
                orientation = ((orientation.previous()).previous()).previous();
                newPosition = position.add(orientation.toUnitVector());
            }
            case LEFT -> {
                orientation = (orientation.previous()).previous();
                newPosition = position.add(orientation.toUnitVector());
            }
            case FORWARD_LEFT -> {
                orientation = orientation.previous();
                orientation = orientation.previous();
            }
            default -> {
            }
        }

        if(!this.map.isOccupied(newPosition)) {
            Vector2d oldPosition = this.position;
            this.position = newPosition;
            positionChanged(oldPosition, newPosition);

        }
        else if(this.map.objectAt(newPosition) instanceof Grass){
            if (map instanceof GrassField && ((GrassField) map).isPlanted(newPosition)) {
                ((GrassField) map).EatAndPlantNewGrass(newPosition);
                this.raiseHP(1);
                Vector2d oldPosition = this.position;
                this.position = newPosition;
                positionChanged(oldPosition, newPosition);
            }
        }
        else if(this.map.objectAt(newPosition) instanceof Animal){
            Animal that = (Animal) this.map.objectAt(newPosition);
            map.multiplication(this, that);
            Vector2d oldPosition = this.position;
            this.position = newPosition;
            positionChanged(oldPosition, newPosition);
        }
        return this;
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer: observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }
    public void turnAround(){
        for(int i =0; i<4; i++){
            orientation = orientation.next();
        }
    }

    public HealthPoints raiseHP(int points){
        this.HP.points += points;
        return this.HP;
    }

    public int loseHP(int points){
        return this.HP.points -= points;
    }


    public void setPosition(Vector2d newPosition){
        this.position = newPosition;//zmienic pozycje na te
    }
}
