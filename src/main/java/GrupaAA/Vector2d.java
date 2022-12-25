package main.java.GrupaAA;

import java.util.Objects;

public class Vector2d {
    public double x;
    public double y;

    public Vector2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }
    public boolean precedes(Vector2d other){
        return (other != null) && (this.x <= other.x) && (this.y <= other.y);
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

    public Vector2d upperRight(Vector2d other){
        if (this.x>=other.x){
            if(this.y>=other.y){
                return new Vector2d(this.x, this.y);
            }
            else return new Vector2d(this.x, other.y);
        }
        else{
            if(this.y>=other.y){
                return new Vector2d(other.x, this.y);
            }
            else return new Vector2d(other.x, other.y);
        }
    }
    public Vector2d lowerLeft(Vector2d other){
        if (this.x<=other.x){
            if(this.y<=other.y){
                return new Vector2d(this.x, this.y);
            }
            else return new Vector2d(this.x, other.y);
        }
        else{
            if(this.y<=other.y){
                return new Vector2d(other.x, this.y);
            }
            else return new Vector2d(other.x, other.y);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

}
