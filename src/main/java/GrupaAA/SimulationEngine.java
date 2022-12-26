package main.java.GrupaAA;

import java.util.ArrayList;

public class SimulationEngine implements IEngine, Runnable{
    protected MoveDirection[] moves;
    protected IWorldMap map;
    protected Vector2d[] positions;
    int newHP = 100;

    public SimulationEngine(MoveDirection[] moves, IWorldMap map, Vector2d[] positions){
        this.moves = moves;
        this.map = map;
        this.positions = positions;
        for (Vector2d position: positions) {
            new Animal(map, position);
        }
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positions){
        this.moves = new ArrayList<MoveDirection>().toArray(new MoveDirection[0]);
        this.map = map;
        this.positions = positions;
        for (Vector2d position: positions) {
            new Animal(map, position);
        }
    }

    @Override //tutaj taka moja propozycja
    public void run(){
        for(Animal animal : map.animals){ //jeżeli w mapie będzie lista zwierząt
            int gen = day % animal.animalGens.length; //zdefiniować gdzieś który dzień
            animal.move(gen);
        }
    }


//    @Override
//    public void run() {
//        int i = 0;
//        for (MoveDirection move : moves) {
//            Animal animal = (Animal) this.map.objectAt(this.positions[i%positions.length]);
//            animal.move(move);
//            this.positions[i% positions.length] = animal.getPosition();
//            i++;
//        }
//    }
}
