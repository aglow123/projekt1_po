package main.java.GrupaAA;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements IMapElement {
    private static final Vector2d DEF_POSITION = new Vector2d(2,2);
    private static final int initHP = 100;
    int HP ;
    public Genotypes genotypes = new Genotypes();
    public int[] animalGens;
    private MapDirection orientation;
    private Vector2d position;
    private IWorldMap map;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IWorldMap map){
        this(map, DEF_POSITION, initHP);
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int HP){
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.HP = HP;
        this.animalGens = genotypes.genotypes;
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
        switch(this.orientation){
            case NORTH: { return "0.png";}
            case WEST: { return "6.png";}
            case SOUTH: { return "4.png";}
            case EAST: { return "2.png";}
            case NORTHWEST: { return "7.png";}
            case SOUTHEAST: { return "3.png";}
            case SOUTHWEST: { return "5.png";}
            case NORTHEAST: { return "1.png";}
            default: { return "candy.png";}
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
                orientation = orientation.previous();
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
                this.raiseHP(1);
                Vector2d oldPosition = this.position;
                this.position = newPosition;
                positionChanged(oldPosition, newPosition);
            }
        }
        else if(this.map.objectAt(newPosition) instanceof Animal){
            Animal that = (Animal) this.map.objectAt(newPosition);
            map.multiplication(this, that);
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
        this.position = newPosition;//zmienic pozycje na te
    }


    public Animal multiplication(Animal animal1, Animal animal2){
        if(animal1.HP>30 && animal2.HP>30) {
            Random generator = new Random();
            int l = generator.nextInt(2); //0 - lewa. 1-prawa
            double a1Weight = 0.25;
            double a2Weight = 0.75;
            if(animal1.HP>=animal2.HP){
                a1Weight = 0.75;
                a2Weight = 0.25;
            }

            int a1Gens = (int)Math.round(a1Weight*animal1.animalGens.length);
            int a2Gens = (int)Math.round(a2Weight*animal2.animalGens.length);
            int n = a2Gens+a1Gens;
            int[] newGens = new int[n];

            if(animal1.HP>=animal2.HP) {
                System.arraycopy(animal1.animalGens, 0, newGens, 0, a1Gens);
                System.arraycopy(animal2.animalGens, a1Gens, newGens, a1Gens, a2Gens + 1);
            }
            else{
                System.arraycopy(animal2.animalGens, 0, newGens, 0, a2Gens);
                System.arraycopy(animal1.animalGens, a2Gens, newGens, a2Gens, a1Gens + 1);
            }

            int healthPoint = 50;
            animal1.loseHP(25);
            animal2.loseHP(25);
            Animal baby = new Animal(this.map, animal1.position, healthPoint);
            baby.genotypes.genotypes = newGens;
            return baby;
        }
        return null;
    }

}

