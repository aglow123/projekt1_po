package GrupaAA;

import java.util.*;

public class ForestedEquators extends GrassField{
    Vector2d lowerLeftEquator, upperRightEquator;

    public ForestedEquators(int typeOfBounds, int numberOfGrass){
        this(new MapBoundary(), typeOfBounds, numberOfGrass, 100, 100);
    }

    public ForestedEquators(MapBoundary mapBoundary, int typeOfBounds, int numberOfGrass, int height, int width){
        super(mapBoundary, typeOfBounds, numberOfGrass, height, width);
        this.lowerLeftEquator = new Vector2d(0,height * 2/5);
        this.upperRightEquator = new Vector2d(width,height * 3/5);
    }

    public void PlantGrass(){
        int widthOfEquator = this.upperRightEquator.y - this.lowerLeftEquator.y + 1;
        boolean val = new Random().nextDouble() < 0.2;     //20% szans ze val is true to sadzimy poza rownikiem
        int x, y;
        Vector2d newPosition;
        Random rand = new Random();
        if(!val){
            while(true) {
                x = rand.nextInt(this.upperRightEquator.x);
                y = rand.nextInt(widthOfEquator);
                newPosition = new Vector2d(x, y + this.lowerLeftEquator.y);
                if (!isOccupied(newPosition)){
                    break;
                }
            }
        }
        else{
            while(true) {
                x = rand.nextInt(this.upperRight.x);
                y = rand.nextInt(this.upperRight.y);
                if(y<this.lowerLeftEquator.y || y>this.upperRightEquator.y){
                    newPosition = new Vector2d(x, y);
                    if (!isOccupied(newPosition)){
                        break;
                    }
                }
            }
        }
        Grass grass = new Grass(newPosition);
        grasses.put(newPosition, grass);
        mapBoundary.add(grass);
    }

//    public boolean isOccupied(Vector2d position){
//        return super.isOccupied(position) || isPlanted(position);
//    }

//    public Vector2d[] setBorders(){
//        return new Vector2d[]{mapBoundary.lowerLeft(), mapBoundary.upperRight()};
//    }
}
