package GrupaAA;

import static java.lang.Math.sqrt;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTHWEST,
    NORTHEAST,
    SOUTHWEST,
    SOUTHEAST;

    public String toString(){
        switch (this) {
            case NORTH : return "Polnoc";
            case EAST : return "Wschod";
            case SOUTH : return "Poludnie";
            case WEST : return "Zachod";
            case NORTHWEST : return "Północny zachód";
            case NORTHEAST : return "Północny wschód";
            case SOUTHWEST : return "Południwy zachód";
            case SOUTHEAST : return "Południowy wschód";
            default : throw new IllegalArgumentException();
        }
    }
    public MapDirection next(){
        switch (this) {
            case NORTH : return NORTHEAST;
            case NORTHEAST : return EAST;
            case EAST : return SOUTHEAST;
            case SOUTHEAST : return SOUTH;
            case SOUTH : return SOUTHWEST;
            case SOUTHWEST : return WEST;
            case WEST : return NORTHWEST;
            case NORTHWEST : return NORTH;
            default : throw new IllegalArgumentException();
        }
    }
    public MapDirection previous(){
        switch (this) {
            case NORTH : return NORTHWEST;
            case NORTHWEST : return WEST;
            case WEST : return SOUTHWEST;
            case SOUTHWEST : return SOUTH;
            case SOUTH : return SOUTHEAST;
            case SOUTHEAST : return EAST;
            case EAST : return NORTHEAST;
            case NORTHEAST : return NORTH;
            default : throw new IllegalArgumentException();
        }
    }
    public Vector2d toUnitVector(){
        switch (this) {
            case NORTH : return new Vector2d(0, 1);
            case EAST : return new Vector2d(1, 0);
            case SOUTH : return new Vector2d(0, -1);
            case WEST : return new Vector2d(-1, 0);
            case NORTHWEST : return new Vector2d(1, -1);
            case NORTHEAST : return new Vector2d(1, 1);
            case SOUTHWEST : return new Vector2d(-1, -1);
            case SOUTHEAST : return new Vector2d(-1, 1);
            default : throw new IllegalArgumentException();
        }
    }
}
