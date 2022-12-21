package aa;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public String toString(){
        switch (this){
            case NORTH:
                return "Polnoc";
            case EAST:
                return "Wschod";
            case SOUTH:
                return "Poludnie";
            case WEST:
                return "Zachod";
            default: throw new IllegalArgumentException();
        }
    }
    public MapDirection next(){
        switch (this){
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            default: throw new IllegalArgumentException();
        }
    }
    public MapDirection previous(){
        switch (this){
            case NORTH:
                return WEST;
            case EAST:
                return NORTH;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            default: throw new IllegalArgumentException();
        }
    }
    public Vector2d toUnitVector(){
        switch (this){
            case NORTH:
                return new Vector2d(0,1);
            case EAST:
                return new Vector2d(1,0);
            case SOUTH:
                return new Vector2d(0,-1);
            case WEST:
                return new Vector2d(-1,0);
            default: throw new IllegalArgumentException();
        }
    }
}