package GrupaAA.gui;

import GrupaAA.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.String.format;


public class App extends javafx.application.Application implements IGuiObserver {
    private SimulationEngineGui engine;
    private GridPane grid;
    private GrassField map;
    private boolean treeVariant = true;   //true - forestedEquators, false - toxicCorpses
    private int initAnimalNumber = 5;
    private int height = 20;
    private int width = 20;
    private int typeOfBounds = 1;
    private int numberOfGrass = 50;
    private int genLength = 10;
    private int initHP = 10;
    private int birthCost = 5;
    private int minHP = 10;
    private int dailyNewGrass = 10;
    private String animalBehaviour = "Random";
    private int minMutation = 3;
    private int maxMutation = 5;
    private String mutation = "Random";
    private int plantEnergy = 2;


    public void init(){
        //trzeba bedzie zrobic okno wyboru dla wszystkich parametrow
        /*


         */

        if(treeVariant) {
            this.map = new ForestedEquators(height, width, typeOfBounds, numberOfGrass);
        }
        else{
            this.map = new ToxicCorpses(height, width, typeOfBounds, numberOfGrass);
        }
        Vector2d[] positions = positions(initAnimalNumber, height, width);
        this.engine = new SimulationEngineGui(map, positions, initHP, birthCost, minHP, genLength, dailyNewGrass, animalBehaviour, minMutation, maxMutation, mutation, plantEnergy, this);
    }

    public Vector2d[] positions(int initAnimalNumber, int height, int width){
        Vector2d[] positions = new Vector2d[initAnimalNumber];
        int x, y;
        Vector2d newPosition;
        Random rand = new Random();
        for(int i=0; i<initAnimalNumber; i++){
            x = rand.nextInt(width);
            y = rand.nextInt(height);
            newPosition = new Vector2d(x, y);
            positions[i] = newPosition;
        }
        return positions;
    }

    @Override
    public void start(Stage primaryStage) throws IllegalArgumentException, InterruptedException {
        HBox upperPart = new HBox();
        System.out.println("start");

        VBox layout = new VBox();
        grid = new GridPane();

        layout.getChildren().add(0, upperPart);
        layout.getChildren().add(1, grid);
        createGridMap();
        Scene scene = new Scene(layout, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
        if(this.map.animals().size() > 0) {
            new Thread(engine).start();
//            Thread.sleep(2000);
        }
    }

    void createGridMap(){
        grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(20, 20, 20, 20));
        drawHeader(this.map, this.grid);
        drawObjects(this.map, this.grid);
    }

    void drawHeader(GrassField map, GridPane grid){
        Label label = new Label("y\\x");
        grid.add(label, 0, 0);
        grid.getColumnConstraints().add(new ColumnConstraints(20));
        grid.getRowConstraints().add(new RowConstraints(20));
        GridPane.setHalignment(label, HPos.CENTER);
        for (int i = map.setBorders()[0].x; i <= map.setBorders()[1].x; i++) {
            label = new Label(format("%d", i));
            grid.add(label, i - map.setBorders()[0].x + 1, 0);
            grid.getColumnConstraints().add(new ColumnConstraints(20));
            GridPane.setHalignment(label, HPos.CENTER);

        }
        for (int j = map.setBorders()[0].y; j <= map.setBorders()[1].y; j++) {
            label = new Label(format("%d", j));
            grid.add(label, 0, map.setBorders()[1].y - j + 1);
            grid.getRowConstraints().add(new RowConstraints(20));
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    //czasami znika czesc cukierkow - DO SPRAWDZENIA
    void drawObjects(GrassField map, GridPane grid){
        for (int i = map.setBorders()[0].x; i <= map.setBorders()[1].x; i++) {
            for (int j = map.setBorders()[0].y; j <= map.setBorders()[1].y; j++) {
                Grass grassToAdd = this.map.grassAt(new Vector2d(i, j));
                if (grassToAdd != null) {
                    GuiElementBox box = new GuiElementBox(grassToAdd);
                    grid.add(box.vbox, i - map.setBorders()[0].x + 1, map.setBorders()[1].y - j + 1);
                    GridPane.setHalignment(box.vbox, HPos.CENTER);
                }
                ArrayList<Animal> animalsToAdd = map.animalsAt(new Vector2d(i, j));
                if(animalsToAdd != null){
                    for(Animal animalToAdd: animalsToAdd){
                        GuiElementBox box = new GuiElementBox(animalToAdd);
                        grid.add(box.vbox, i - map.setBorders()[0].x + 1, map.setBorders()[1].y - j + 1);
                        GridPane.setHalignment(box.vbox, HPos.CENTER);
                    }
                }
            }
        }

    }

    @Override
    public void reload(){
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.getChildren().retainAll(grid.getChildren().get(0));
        createGridMap();
    }

}