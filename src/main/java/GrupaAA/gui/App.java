package GrupaAA.gui;

import GrupaAA.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static java.lang.String.format;


public class App extends javafx.application.Application implements IGuiObserver {
    public TextField width1;
    public TextField initAnimalNumber1;
    public TextField minHP1;
    public TextField genLength1;
    public TextField birthCost1;
    public TextField maxMutation1;
    public TextField minMutation1;
    public TextField initHP1;
    public TextField dailyNewGrass1;
    public TextField plantEnergy1;
    public TextField numberOfGrass1;
    public TextField height1;
    public ToggleGroup toggle_grass;
    public ToggleGroup toggle_mutation;
    public ToggleGroup toggle_behavior;
    public ToggleGroup toggle_map;

    private SimulationEngineGui engine;
    private GridPane grid;
    private GrassField map;

    private int typeOfBounds = 1;   //1-glob, 2-hell portal
    private String treeVariant = "Forested equators";   //true - forestedEquators, false - toxicCorpses
    private String mutation = "Random";
    private String animalBehaviour = "Random";

    private int height = 20;
    private int width = 20;
    private int numberOfGrass = 20;
    private int plantEnergy = 2;
    private int dailyNewGrass = 10;
    private int initHP = 10;
    private int initAnimalNumber = 5;
    private int minMutation = 0;
    private int maxMutation = 5;
    private int birthCost = 5;
    private int genLength = 10;
    private int minHP = 10;


    public int row_length = 30;
    public int column_length = 30;

    public void init(){
//        if(treeVariant) {
//            this.map = new ForestedEquators(height, width, typeOfBounds, numberOfGrass);
//        }
//        else{
//            this.map = new ToxicCorpses(height, width, typeOfBounds, numberOfGrass);
//        }
//        Vector2d[] positions = positions(initAnimalNumber, height, width);
//        this.engine = new SimulationEngineGui(map, positions, initHP, birthCost, minHP, genLength, dailyNewGrass, animalBehaviour, minMutation, maxMutation, mutation, plantEnergy, this);
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
    public void start(Stage primaryStage) throws IllegalArgumentException, InterruptedException, IOException {

        URL url = new File("src/main/resources/user_window.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.show();

//        HBox upperPart = new HBox();
//        System.out.println("start");
//
//        VBox layout = new VBox();
//        grid = new GridPane();
//
//        layout.getChildren().add(0, upperPart);
//        layout.getChildren().add(1, grid);
//        createGridMap();
//        Scene scene = new Scene(layout, width, height);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        if(this.map.animals().size() > 0) {
//            new Thread(engine).start();
////            Thread.sleep(2000);
//        }
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
        grid.getColumnConstraints().add(new ColumnConstraints(50));
        grid.getRowConstraints().add(new RowConstraints(50));
        GridPane.setHalignment(label, HPos.CENTER);
        for (int i = map.setBorders()[0].x; i <= map.setBorders()[1].x; i++) {
            label = new Label(format("%d", i));
            grid.add(label, i - map.setBorders()[0].x + 1, 0);
            grid.getColumnConstraints().add(new ColumnConstraints(column_length));
            GridPane.setHalignment(label, HPos.CENTER);

        }
        for (int j = map.setBorders()[0].y; j <= map.setBorders()[1].y; j++) {
            label = new Label(format("%d", j));
            grid.add(label, 0, map.setBorders()[1].y - j + 1);
            grid.getRowConstraints().add(new RowConstraints(row_length));
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

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
                        float pom = (float)1/initHP;
                        box.pb.setProgress(pom*animalToAdd.HP);
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


    @FXML
    public void clickButton(ActionEvent event) throws Exception {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

        if(treeVariant.equals("Forested equators")) {
            this.map = new ForestedEquators(height, width, typeOfBounds, numberOfGrass);
        }
        else{
            this.map = new ToxicCorpses(height, width, typeOfBounds, numberOfGrass);
        }
        Vector2d[] positions = positions(initAnimalNumber, height, width);
        this.engine = new SimulationEngineGui(map, positions, initHP, birthCost, minHP, genLength, dailyNewGrass, animalBehaviour, minMutation, maxMutation, mutation, plantEnergy, this);

        HBox upperPart = new HBox();
        System.out.println("start");

        VBox layout = new VBox();
        grid = new GridPane();

        layout.getChildren().add(0, upperPart);
        layout.getChildren().add(1, grid);
        createGridMap();
        Scene scene = new Scene(layout, 1.3*row_length*width, 1.3*column_length*width);
        Stage secondStage = new Stage();
        secondStage.setScene(scene);
        secondStage.show();
        if(this.map.animals().size() > 0) {
            new Thread(engine).start();
            Thread.sleep(300);
        }
    }


    public void setVariants() {
        RadioButton selectedRadioButton3 = (RadioButton) toggle_behavior.getSelectedToggle();
        mutation = selectedRadioButton3.getText();
    }

    public void setMap(){
        RadioButton selectedRadioButton1 = (RadioButton) toggle_map.getSelectedToggle();
        if(Objects.equals(selectedRadioButton1.getText(), "Glob")){
            typeOfBounds = 1;
        }
        else if(Objects.equals(selectedRadioButton1.getText(), "Hell portal")){
            typeOfBounds = 2;
        }
    }

    public void setGrass(){
        RadioButton selectedRadioButton2 = (RadioButton) toggle_grass.getSelectedToggle();
        treeVariant = selectedRadioButton2.getText();
    }

    public void setMutationVar(){
        RadioButton selectedRadioButton = (RadioButton) toggle_mutation.getSelectedToggle();
        animalBehaviour = selectedRadioButton.getText();
    }

    public void width(ActionEvent actionEvent) {
        width = Integer.parseInt(width1.getText());
    }

    public void height(ActionEvent actionEvent) {
        height = Integer.parseInt(height1.getText());
    }

    public void numberOfGrass(ActionEvent actionEvent) {
        numberOfGrass = Integer.parseInt(numberOfGrass1.getText());
    }

    public void plantEnergy(ActionEvent actionEvent) {
        plantEnergy = Integer.parseInt(plantEnergy1.getText());
    }

    public void dailyNewGrass(ActionEvent actionEvent) {
        dailyNewGrass = Integer.parseInt(dailyNewGrass1.getText());
    }

    public void initHP(ActionEvent actionEvent) {
        initHP = Integer.parseInt(initHP1.getText());
    }

    public void initAnimalNumber(ActionEvent actionEvent) {
        initAnimalNumber = Integer.parseInt(initAnimalNumber1.getText());
    }

    public void minMutation(ActionEvent actionEvent) {
        minMutation = Integer.parseInt(minMutation1.getText());
    }

    public void maxMutation(ActionEvent actionEvent) {
        maxMutation = Integer.parseInt(maxMutation1.getText());
    }

    public void birthCost(ActionEvent actionEvent) {
        birthCost = Integer.parseInt(birthCost1.getText());
    }

    public void genLength(ActionEvent actionEvent) {
        genLength = Integer.parseInt(genLength1.getText());
    }

    public void minHP(ActionEvent actionEvent) {
        minHP = Integer.parseInt(minHP1.getText());
    }
}