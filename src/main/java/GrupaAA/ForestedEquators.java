package GrupaAA;

import java.util.*;

public class ForestedEquators extends GrassField{
    Vector2d lowerLeftEquator, upperRightEquator;

    public ForestedEquators(int height, int width, int typeOfBounds, int numberOfGrass){
        this.typeOfBounds = typeOfBounds;   //1 stands for 'globe', 2 stands for 'hell portal'
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        this.day = 1;
        this.lowerLeftEquator = new Vector2d(0,height * 2/5);
        this.upperRightEquator = new Vector2d(width,height * 3/5);
        for(int i=0; i<numberOfGrass; i++){
            PlantGrass();
        }
    }

    public void PlantGrass(){
        int widthOfEquator = this.upperRightEquator.y - this.lowerLeftEquator.y + 1;
        Random rand = new Random();
        double val = rand.nextDouble();     //20% szans ze val < 0.2, wtedy sadzimy poza rownikiem
        int x, y;
        Vector2d newPosition;
        while(true){
            if(val>= 0.2){
                x = rand.nextInt(this.upperRightEquator.x);
                y = rand.nextInt(widthOfEquator);
                newPosition = new Vector2d(x, y + this.lowerLeftEquator.y);
            }
            else{
                x = rand.nextInt(this.upperRight.x);
                y = rand.nextInt(this.upperRight.y - this.upperRightEquator.y + this.lowerLeftEquator.y);
                if(y>=this.lowerLeftEquator.y) {
                    newPosition = new Vector2d(x, y + widthOfEquator - 1);
                }
                else{
                    newPosition = new Vector2d(x,y);
                }
            }
            if (!isPlanted(newPosition)){
                break;
            }
            else {
                val = rand.nextDouble();
            }
        }
        Grass grass = new Grass(newPosition);
        grasses.put(newPosition, grass);
    }
}
