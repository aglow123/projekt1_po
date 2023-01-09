package GrupaAA.gui;

import GrupaAA.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    public RadioButton stats_button;
    public ToggleGroup toggle_save;
    public Button start_button;

    private SimulationEngineGui engine;
    private GridPane grid;
    private GridPane stats;
    private GrassField map;
    private LineChart<Integer, Integer> animalChart;
    private LineChart<Integer, Integer> grassChart;
    private XYChart.Series<Integer, Integer> datasetAnimal = new XYChart.Series<>();
    private XYChart.Series<Integer, Integer> datasetGrass = new XYChart.Series<>();
    private int day = 1;
    private boolean save = false;

    List<Image> images = new ArrayList<>();

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
        for (int i = map.setBorders()[0].x; i < map.setBorders()[1].x; i++) {
            label = new Label(format("%d", i));
            grid.add(label, i - map.setBorders()[0].x + 1, 0);
            grid.getColumnConstraints().add(new ColumnConstraints(column_length));
            GridPane.setHalignment(label, HPos.CENTER);

        }
        for (int j = map.setBorders()[0].y; j < map.setBorders()[1].y; j++) {
            label = new Label(format("%d", j));
            grid.add(label, 0, map.setBorders()[1].y - j);
            grid.getRowConstraints().add(new RowConstraints(row_length));
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    void drawObjects(GrassField map, GridPane grid){
        for (int i = map.setBorders()[0].x; i < map.setBorders()[1].x; i++) {
            for (int j = map.setBorders()[0].y; j < map.setBorders()[1].y; j++) {
                Grass grassToAdd = this.map.grassAt(new Vector2d(i, j));
                if (grassToAdd != null) {
                    GuiElementBox box = new GuiElementBox(grassToAdd, images.get(8));
                    grid.add(box.vbox, i - map.setBorders()[0].x + 1, map.setBorders()[1].y - j);
                    GridPane.setHalignment(box.vbox, HPos.CENTER);
                }
                ArrayList<Animal> animalsToAdd = map.animalsAt(new Vector2d(i, j));
                if(animalsToAdd != null){
                    for(Animal animalToAdd: animalsToAdd){
                        GuiElementBox box = new GuiElementBox(animalToAdd, images.get(Integer.parseInt(animalToAdd.getElementName().substring(0,1))));
                        float pom = (float)1/initHP;
                        box.pb.setProgress(pom*animalToAdd.HP);
                        grid.add(box.vbox, i - map.setBorders()[0].x + 1, map.setBorders()[1].y - j);
                        GridPane.setHalignment(box.vbox, HPos.CENTER);
                    }
                }
            }
        }
    }

    public void createChart(){
        animalChart.setLegendVisible(false);
        animalChart.setTitle("Number of Animals");
        animalChart.setCreateSymbols(false);
        Node lineA = datasetAnimal.getNode();
        Color colorA = Color.MAGENTA;
        String rgbA = String.format("%d, %d, %d", (colorA.getRed() * 255), (colorA.getGreen() * 255), (colorA.getBlue() * 255));
        lineA.setStyle("-fx-stroke: rgba(" + rgbA + ", 1.0);");

        grassChart.setLegendVisible(false);
        grassChart.setTitle("Number of Grasses");
        grassChart.setCreateSymbols(false);
        Node lineG = datasetGrass.getNode();
        Color colorG = Color.GREEN;
        String rgbG = String.format("%d, %d, %d", (colorG.getRed() * 255), (colorG.getGreen() * 255), (colorG.getBlue() * 255));
        lineG.setStyle("-fx-stroke: rgba(" + rgbG + ", 1.0);");

        updateChart();
    }

    public void updateChart(){
        datasetAnimal.getData().add(new XYChart.Data<>(day, map.animals().size()));
        datasetGrass.getData().add(new XYChart.Data<>(day, map.grasses().size()));
    }

    public void createStats(){
        stats.setGridLinesVisible(true);
        stats.setPadding(new Insets(20, 20, 20, 20));
        Label label = new Label("\tNumber of animals: ");
        stats.add(label, 0, 0);
        stats.getColumnConstraints().add(new ColumnConstraints(200));
        stats.getRowConstraints().add(new RowConstraints(50));

        label = new Label(String.valueOf(map.animals().size()));
        stats.add(label, 1, 0);
        stats.getColumnConstraints().add(new ColumnConstraints(50));
        GridPane.setHalignment(label, HPos.CENTER);

        stats.add(new Label("\tNumber of grasses: "), 0, 1);
        stats.getRowConstraints().add(new RowConstraints(50));
        label = new Label(String.valueOf(map.grasses().size()));
        stats.add(label, 1, 1);
        GridPane.setHalignment(label, HPos.CENTER);

        stats.add(new Label("\tNumber of free fields: "), 0, 2);
        stats.getRowConstraints().add(new RowConstraints(50));
        label = new Label(String.valueOf(map.howManyFreeFields()));
        stats.add(label, 1, 2);
        GridPane.setHalignment(label, HPos.CENTER);

        stats.add(new Label("\tThe most popular genotype: "), 0, 3);
        stats.getRowConstraints().add(new RowConstraints(50));
        label = new Label(String.valueOf(map.popularGenotype()));
        stats.add(label, 1, 3);
        GridPane.setHalignment(label, HPos.CENTER);

        stats.add(new Label("\tAverage number of HP: "), 0, 4);
        stats.getRowConstraints().add(new RowConstraints(50));
        label = new Label(String.format("%.2f", map.averageHP()));
        stats.add(label, 1, 4);
        GridPane.setHalignment(label, HPos.CENTER);

        stats.add(new Label("\tAverage age of animals: "), 0, 5);
        stats.getRowConstraints().add(new RowConstraints(50));
        label = new Label(String.format("%.2f", map.averageAge()));
        stats.add(label, 1, 5);
        GridPane.setHalignment(label, HPos.CENTER);
    }

    public void createCSV(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Day").append(",").append("Number of animals").append(",").append("Number of grasses")
                        .append(",").append("Number of free fields").append(",").append("The most popular genotype")
                        .append(",").append("Average number of HP").append(",").append("Average age of animals")
                        .append("\n");

        stringBuilder.append(day).append(",").append(map.animals().size()).append(",")
                        .append(map.grasses().size()).append(",").append(map.howManyFreeFields()).append(",")
                        .append(map.popularGenotype()).append(",").append(map.averageHP()).append(",")
                        .append(map.averageAge()).append("\n");

        try ( FileWriter fileWriter = new FileWriter("C:\\TEMP\\stat.csv") ) {
            fileWriter.write(stringBuilder.toString());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateCSV(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(day).append(",").append(map.animals().size()).append(",")
                .append(map.grasses().size()).append(",").append(map.howManyFreeFields()).append(",")
                .append(map.popularGenotype()).append(",").append(map.averageHP()).append(",")
                .append(map.averageAge()).append("\n");

        try ( FileWriter fileWriter = new FileWriter("C:\\TEMP\\stat.csv", true) ) {
            fileWriter.write(stringBuilder.toString());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void reload(){
        day += 1;

        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        grid.getChildren().retainAll(grid.getChildren().get(0));
        createGridMap();

        updateChart();

        stats.getRowConstraints().clear();
        stats.getColumnConstraints().clear();
        stats.getChildren().retainAll(stats.getChildren().get(0));
        createStats();

        if(save){
            updateCSV();
        }
    }


    @FXML
    public void clickButton(ActionEvent event) throws Exception {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        reloadParameters();

        if(treeVariant.equals("Forested equators")) {
            this.map = new ForestedEquators(height, width, typeOfBounds, numberOfGrass);
        }
        else{
            this.map = new ToxicCorpses(height, width, typeOfBounds, numberOfGrass);
        }
        Vector2d[] positions = positions(initAnimalNumber, height, width);
        this.engine = new SimulationEngineGui(map, positions, initHP, birthCost, minHP, genLength, dailyNewGrass, animalBehaviour, minMutation, maxMutation, mutation, plantEnergy, this);

        HBox hbox = new HBox();
        VBox vbox = new VBox();
        VBox vbox2 = new VBox();
        grid = new GridPane();

        final NumberAxis xAxisAnimal = new NumberAxis();
        final NumberAxis yAxisAnimal = new NumberAxis();
        xAxisAnimal.setLabel("Day");
        animalChart = new LineChart(xAxisAnimal, yAxisAnimal);
        final NumberAxis xAxisGrass = new NumberAxis();
        final NumberAxis yAxisGrass = new NumberAxis();
        xAxisGrass.setLabel("Day");
        grassChart = new LineChart(xAxisGrass, yAxisGrass);
        stats = new GridPane();

        vbox.getChildren().add(0, animalChart);
        vbox.getChildren().add(1, grassChart);

        vbox2.getChildren().add(0, stats);

        hbox.getChildren().add(0, grid);
        hbox.getChildren().add(1, vbox);
        hbox.getChildren().add(2, vbox2);

        createGridMap();
        animalChart.getData().add(datasetAnimal);
        grassChart.getData().add(datasetGrass);
        createChart();
        createStats();

        if(save){
            createCSV();
        }

        Scene scene = new Scene(hbox, 1.3*row_length*width, 1.3*column_length*width);
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

    public void setSave(){
        RadioButton selectedRadio = (RadioButton) toggle_save.getSelectedToggle();
        if(Objects.equals(selectedRadio.getText(), "Save statistics")){
            save = true;
        }
        else if(Objects.equals(selectedRadio.getText(), "Do not save")){
            save = false;
        }
    }
    
    public void reloadParameters(){
        width();
        height();
        numberOfGrass();
        plantEnergy();
        dailyNewGrass();
        initHP();
        initAnimalNumber();
        minMutation();
        maxMutation();
        birthCost();
        genLength();
        minHP();
        getImages();
    }

    public void width() {
        width = Integer.parseInt(width1.getText());
    }

    public void height() {
        height = Integer.parseInt(height1.getText());
    }

    public void numberOfGrass() {
        numberOfGrass = Integer.parseInt(numberOfGrass1.getText());
    }

    public void plantEnergy() {
        plantEnergy = Integer.parseInt(plantEnergy1.getText());
    }

    public void dailyNewGrass() {
        dailyNewGrass = Integer.parseInt(dailyNewGrass1.getText());
    }

    public void initHP() {
        initHP = Integer.parseInt(initHP1.getText());
    }

    public void initAnimalNumber() {
        initAnimalNumber = Integer.parseInt(initAnimalNumber1.getText());
    }

    public void minMutation() {
        minMutation = Integer.parseInt(minMutation1.getText());
    }

    public void maxMutation() {
        maxMutation = Integer.parseInt(maxMutation1.getText());
    }

    public void birthCost() {
        birthCost = Integer.parseInt(birthCost1.getText());
    }

    public void genLength() {
        genLength = Integer.parseInt(genLength1.getText());
    }

    public void minHP() {
        minHP = Integer.parseInt(minHP1.getText());
    }

    private void getImages() {
        String fileAddress = "src/main/resources/";
        for(int i = 0; i<8; i++){
            try {
                images.add(new Image(new FileInputStream(fileAddress + i + ".png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            images.add(new Image(new FileInputStream(fileAddress + "candy.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}