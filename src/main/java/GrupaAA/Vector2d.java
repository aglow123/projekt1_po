package GrupaAA;

import java.util.Objects;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }
    public boolean precedes(Vector2d other){
        return (other != null) && (this.x < other.x) && (this.y < other.y);
    }

    public boolean follows(Vector2d other){
        return (other != null) && (this.x >= other.x) && (this.y >= other.y);
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x,this.y + other.y);
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x,this.y - other.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (this.x == that.x && this.y == that.y);
    }
}
