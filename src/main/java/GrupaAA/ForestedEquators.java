package GrupaAA;

import java.util.*;

public class ForestedEquators extends GrassField{
    Vector2d lowerLeftEquator, upperRightEquator;

    public ForestedEquators(int height, int width, int typeOfBounds, int numberOfGrass){
        this.typeOfBounds = typeOfBounds;   //1 stands for 'globe', 2 stands for 'hell portal'
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        this.lowerLeftEquator = new Vector2d(0,height * 2/5);
        this.upperRightEquator = new Vector2d(width,height * 3/5);
        for(int i=0; i<numberOfGrass; i++){
            PlantGrass();
        }
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
                if (!isPlanted(newPosition)){
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
                    if (!isPlanted(newPosition)){
                        break;
                    }
                }
            }
        }
        Grass grass = new Grass(newPosition);
        grasses.put(newPosition, grass);
    }
}
