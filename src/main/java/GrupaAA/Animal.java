package main.java.GrupaAA;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements IMapElement {

    //parametry
    int HP ;
    int plantEnergy;
    int birthCost;
    int minHP;
    int maxMutations;


    private static final Vector2d DEF_POSITION = new Vector2d(2,2);
    private static final int initHP = 100;
    public Genotypes genotypes = new Genotypes();
    public int[] animalGens;
    private MapDirection orientation;
    private Vector2d position;
    private IWorldMap map;
    private int age;
    boolean isAlive;
    private int childeren;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IWorldMap map){
        this(map, DEF_POSITION);
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.animalGens = genotypes.genotypes;
        this.age = 0;
        this.childeren = 0;
        this.isAlive = true;
        map.place(this);
    }

    public String toString(){
        return switch (orientation) {
            case NORTH -> "^";
            case EAST -> ">";
            case SOUTH -> "v";
            case WEST -> "<";
            case NORTHWEST -> "NW";
            case NORTHEAST -> "NE";
            case SOUTHWEST -> "SW";
            case SOUTHEAST -> "SE";
            default -> this.toString();
        };
    }
    public MapDirection getOrientation(){return this.orientation;}

    public Vector2d getPosition(){return this.position;}
    @Override
    public String getElementName() {
        switch (this.orientation) {
            case NORTH -> {
                return "0.png";
            }
            case WEST -> {
                return "6.png";
            }
            case SOUTH -> {
                return "4.png";
            }
            case EAST -> {
                return "2.png";
            }
            case NORTHWEST -> {
                return "7.png";
            }
            case SOUTHEAST -> {
                return "3.png";
            }
            case SOUTHWEST -> {
                return "5.png";
            }
            case NORTHEAST -> {
                return "1.png";
            }
            default -> {
                return "candy.png";
            }
        }
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public Animal move(int direction){
        this.age +=1;
        Vector2d newPosition = null;
        switch(direction) {
            case 0 -> {
                newPosition = position.add(orientation.toUnitVector());
            }
            case 1 -> {
                orientation = orientation.next();
                newPosition = position.add(orientation.toUnitVector());
            }
            case 2 -> {
                orientation = (orientation.next()).next();
                newPosition = position.add(orientation.toUnitVector());
            }
            case 3 -> {
                orientation = ((orientation.next()).next()).next();
                newPosition = position.subtract(orientation.toUnitVector());
            }
            case 4 -> {
                newPosition = position.subtract(orientation.toUnitVector());
            }
            case 5 -> {
                orientation = ((orientation.previous()).previous()).previous();
                newPosition = position.add(orientation.toUnitVector());
            }
            case 6 -> {
                orientation = (orientation.previous()).previous();
                newPosition = position.add(orientation.toUnitVector());
            }
            case 7 -> {
                orientation = orientation.previous();
                newPosition = position.add(orientation.toUnitVector());
            }
            default -> {
            }
        }

        if(!this.map.isOccupied(newPosition)) {
            Vector2d oldPosition = this.position;
            this.position = newPosition;
            positionChanged(oldPosition, newPosition);

        }
        else if(this.map.objectAt(newPosition) instanceof Grass){
            if (map instanceof GrassField && ((GrassField) map).isPlanted(newPosition)) {
                ((GrassField) map).EatAndPlantNewGrass(newPosition);
                this.raiseHP(plantEnergy);
                Vector2d oldPosition = this.position;
                this.position = newPosition;
                positionChanged(oldPosition, newPosition);
            }
        }
        else if(this.map.objectAt(newPosition) instanceof Animal){
            Animal that = (Animal) this.map.objectAt(newPosition);
            multiplication(that);
            Vector2d oldPosition = this.position;
            this.position = newPosition;
            positionChanged(oldPosition, newPosition);
        }
        return this;
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer: observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }
    public void turnAround(){
        for(int i =0; i<4; i++){
            orientation = orientation.next();
        }
    }

    public void raiseHP(int points){
        this.HP += points;
    }

    public void loseHP(int points){
        this.HP -= points;
    }


    public void setPosition(Vector2d newPosition){
        this.position = newPosition;
    }


    public void multiplication(Animal animal1){
        if(animal1.HP>minHP && this.HP>minHP) {
            Random generator = new Random();
            int l = generator.nextInt(2); //0 - lewa. 1-prawa
            double a1Weight = 0.25;
            double a2Weight = 0.75;
            if(animal1.HP>=this.HP){
                a1Weight = 0.75;
                a2Weight = 0.25;
            }

            int a1Gens = (int)Math.round(a1Weight*animal1.animalGens.length);
            int a2Gens = (int)Math.round(a2Weight*this.animalGens.length);
            int n = a2Gens+a1Gens;
            int[] newGens = new int[n];

            if(animal1.HP>=this.HP) {
                System.arraycopy(animal1.animalGens, 0, newGens, 0, a1Gens);
                int[]pom = reverse(this.animalGens);
                System.arraycopy(pom, 0, newGens, a1Gens, a2Gens-1);
            }
            else {
                System.arraycopy(this.animalGens, 0, newGens, 0, a2Gens);
                int[] pom = reverse(animal1.animalGens);
                System.arraycopy(pom, 0, newGens, a2Gens, a1Gens - 1);
            }
            int x=this.genotypes.intGenerator(maxMutations);
            for(int i =0; i<x; i++){
                newGens[this.genotypes.intGenerator(n)] = this.genotypes.intGenerator(8);
            }

            int healthPoint = birthCost*2;
            animal1.loseHP(birthCost);
            this.loseHP(birthCost); //zajmie miejsce rodzica w animals bo już jest taki klucz
            Animal baby = new Animal(this.map, animal1.position);
            baby.genotypes.genotypes = newGens;
            baby.HP = healthPoint;
            animal1.childeren +=1;
            this.childeren +=1;
        }

    }
    public static int[] reverse(int[] data) {
        for (int left = 0, right = data.length - 1; left < right; left++, right--) {
            int temp = data[left];
            data[left] = data[right];
            data[right] = temp;
        }
        return data;
    }

    public void death(){
        if (this.HP == 0){
            this.isAlive = false;
        }
    }
}

