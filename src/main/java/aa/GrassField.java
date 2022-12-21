package aa;

import java.util.*;

public class GrassField extends AbstractWorldMap{
    int numberOfGrass;
    Vector2d lowerLeftEquator, upperRightEquator;
    Map<Vector2d, Grass> grasses = new HashMap<>();
    private final MapBoundary mapBoundary;

    public GrassField(int numberOfGrass){
        this(new MapBoundary(), numberOfGrass, 100, 100);
    }

    public GrassField(MapBoundary mapBoundary, int numberOfGrass, int height, int width){
        this.mapBoundary = mapBoundary;
        this.numberOfGrass = numberOfGrass;
        this.lowerLeftEquator = new Vector2d(0,height * 2/5);
        this.upperRightEquator = new Vector2d(width,height * 3/5);
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        for(int i=0; i<numberOfGrass; i++){
            PlantGrass();
        }
    }

    public void PlantGrass(){
        //zalesione rowniki
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

    public void EatAndPlantNewGrass(Vector2d position){
        PlantGrass();
        this.grasses.remove(position);
        mapBoundary.remove(position);
    }

    public boolean isPlanted(Vector2d position){
        return grasses.containsKey(position);
    }

    public boolean isOccupied(Vector2d position){
        return super.isOccupied(position) || isPlanted(position);
    }

    public Vector2d[] setBorders(){
        return new Vector2d[]{mapBoundary.lowerLeft(), mapBoundary.upperRight()};
    }

    @Override
    public boolean place(Animal animal) throws IllegalArgumentException{
        if (!canMoveTo(animal.getPosition())){
            throw new IllegalArgumentException("You cannot place an animal in the position outside the map");
        }
        if (this.isOccupied(animal.getPosition())){
            if (this.objectAt(animal.getPosition()) instanceof Animal)
                throw new IllegalArgumentException("You cannot place an animal in the position of another animal");
            else if (this.objectAt(animal.getPosition()) instanceof Grass){
                EatAndPlantNewGrass(animal.getPosition());
            }
        }
        animals.put(animal.getPosition(), animal);
        animal.addObserver(this);
        mapBoundary.add(animal);
        return true;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animals.containsKey(position)){
            return animals.get(position);
        }
        else if (grasses.containsKey(position)){
            return grasses.get(position);
        }
        return null;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        super.positionChanged(oldPosition, newPosition);
        mapBoundary.positionChanged(oldPosition, newPosition);
    }

}
