package main.java.GrupaAA;

public class Animal {
    private static final Vector2d DEF_POSITION = new Vector2d(2,2);
    private MapDirection orientation;
    private Vector2d position;
    protected HealthPoints HP = new HealthPoints(10);
    public Animal(Vector2d initialPosition){
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
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

}
