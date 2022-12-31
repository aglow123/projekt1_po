package GrupaAA;

public class Grass implements IMapElement{
    Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }
    public Vector2d getPosition(){
        return this.position;
    }
    public String toString(){
        return "*";
    }

    @Override
    public String getElementName() {
        return "candy.png";
    }
}