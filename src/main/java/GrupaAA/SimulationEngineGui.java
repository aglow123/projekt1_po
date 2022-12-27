package GrupaAA;

import javafx.application.Platform;

public class SimulationEngineGui extends SimulationEngine {
    private IGuiObserver observer;

    private final int moveDelay = 300;

    public SimulationEngineGui(MoveDirection[] moves, IWorldMap map, Vector2d[] positions, IGuiObserver observer){
        super(moves, map, positions);
        this.observer= observer;
    }

    public SimulationEngineGui(IWorldMap map, Vector2d[] positions, IGuiObserver observer){
        super(map,positions);
        this.observer = observer;
    }

    public void setDirections(MoveDirection[] newDirections) {
        this.moves = newDirections;
    }

    void reload(){
        this.observer.reload();
    }

    @Override
    public void run() {
        Platform.runLater(this::reload);
        if (((GrassField)this.map).animals().length() == 0) {
            System.out.println("animals empty");
            return;
        }
        int i = 0;
        for (MoveDirection move: moves) {
            try {
                Thread.sleep(moveDelay);
            } catch (InterruptedException ex) {
                System.out.println("interrupted exception on sleep");
            }
            Animal animal = (Animal) this.map.objectAt(this.positions[i%positions.length]);
            animal.move(move);
            this.positions[i% positions.length] = animal.getPosition();
            Platform.runLater(this::reload);
            i++;
        }

    }
}
