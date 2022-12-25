package GrupaAA;

import java.util.ArrayList;

public class Animal {
    private static final Vector2d DEF_POSITION = new Vector2d(2,2);
    private MapDirection orientation;
    private Vector2d position;
    protected HealthPoints HP = new HealthPoints(10);
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
        return switch (orientation) {
            case NORTH -> "^";
            case EAST -> ">";
            case SOUTH -> "v";
            case WEST -> "<";
            default -> this.toString();
        };
    }
    public MapDirection getOrientation(){return this.orientation;}

    public Vector2d getPosition(){return this.position;}

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
        //sporo do przekopiowania z wczeniejszej wersji, w grass field obsluguje granice mapy
        return this;
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer: observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }
    public void turnAround(){
        //obrocic orientacje zwiarzaka o 180 stopni
    }

    public void loseHP(){
        //pomniejszyc wartosc HP o ustalona wartosc (taka sama jak w przypadku rozmnazania)
    }

    public void setPosition(Vector2d newPosition){
        //zmienic pozycje na te
    }
}
