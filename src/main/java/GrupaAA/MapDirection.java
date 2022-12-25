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
        return switch (this) {
            case NORTH -> "Polnoc";
            case EAST -> "Wschod";
            case SOUTH -> "Poludnie";
            case WEST -> "Zachod";
            case NORTHWEST -> "Północny zachód";
            case NORTHEAST -> "Północny wschód";
            case SOUTHWEST -> "Południwy zachód";
            case SOUTHEAST -> "Południowy wschód";
            default -> throw new IllegalArgumentException();
        };
    }
    public MapDirection next(){
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
            default -> throw new IllegalArgumentException();
        };
    }
    public MapDirection previous(){
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHWEST -> WEST;
            case WEST -> SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            case NORTHEAST -> NORTH;
            default -> throw new IllegalArgumentException();
        };
    }
    public Vector2d toUnitVector(){
        double temp = 1/sqrt(2);
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(temp, -temp);
            case NORTHEAST -> new Vector2d(temp, temp);
            case SOUTHWEST -> new Vector2d(-temp, -temp);
            case SOUTHEAST -> new Vector2d(-temp, temp);
            default -> throw new IllegalArgumentException();
        };
    }
}
