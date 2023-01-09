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

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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

    public static final String DATE_FORMAT_NOW = "yyyyMMddHHmmss";
    private String filename;
    private Animal animalToSpy;

    private SimulationEngineGui engine;
    private GridPane grid;
    private GridPane stats;
    private GrassField map;
    private LineChart<Integer, Integer> animalChart;
    private LineChart<Integer, Integer> grassChart;
    private XYChart.Series<Integer, Integer> datasetAnimal = new XYChart.Series<>();
    private XYChart.Series<Integer, Integer> datasetGrass = new XYChart.Series<>();
    private Integer day = 1;
    private boolean save = false;
    private boolean spy = false;
    private boolean highlight = false;
//    private String text = "Select animal to spy";

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
    public void start(Stage primaryStage) throws IllegalArgumentException, IOException {
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
                        box.pb.setProgress(pom*animalToAdd.getHP());
                        grid.add(box.vbox, i - map.setBorders()[0].x + 1, map.setBorders()[1].y - j);
                        GridPane.setHalignment(box.vbox, HPos.CENTER);
                        if(highlight && contains(animalToAdd.getAnimalGens(), map.popularGenotype())){
                            (box.vbox).setStyle("-fx-background-color:#ADD8E6;");
                        }
                        if(spy && animalToAdd == animalToSpy){
                            (box.vbox).setStyle("-fx-background-color:#f9f3c5;");
                        }

                        (box.vbox).setDisable(spy);
                        (box.vbox).setOnMouseClicked(e -> {
                            animalToSpy = animalToAdd;
                            (box.vbox).setStyle("-fx-background-color:#f9f3c5;");
                        });
                    }
                }
            }
        }
    }

    public boolean contains(final int[] array, final int key) {
        for (final int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
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
        stats.getColumnConstraints().add(new ColumnConstraints(100));
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

        if(spy){
            stats.add(new Label("\tPosition of animal to spy: "), 0, 6);
            stats.getRowConstraints().add(new RowConstraints(50));
            if(animalToSpy.isAlive) {
                label = new Label(animalToSpy.getPosition().x + ", " + animalToSpy.getPosition().y);
                stats.add(label, 1, 6);
                GridPane.setHalignment(label, HPos.CENTER);
            }

            stats.add(new Label("\tGenome: "), 0, 7);
            stats.getRowConstraints().add(new RowConstraints(50));
            label = new Label(Arrays.toString(animalToSpy.getAnimalGens()).replace(",", "")
                    .replace("[", "").replace("]", ""));
            stats.add(label, 1, 7);
            GridPane.setHalignment(label, HPos.CENTER);

            stats.add(new Label("\tActivated gen: "), 0, 8);
            stats.getRowConstraints().add(new RowConstraints(50));
            if(animalToSpy.isAlive) {
                label = new Label(String.valueOf(animalToSpy.getActivatedGen()));
                stats.add(label, 1, 8);
                GridPane.setHalignment(label, HPos.CENTER);
            }

            stats.add(new Label("\tHP: "), 0, 9);
            stats.getRowConstraints().add(new RowConstraints(50));
            label = new Label(String.valueOf(animalToSpy.getHP()));
            stats.add(label, 1, 9);
            GridPane.setHalignment(label, HPos.CENTER);

            stats.add(new Label("\tEaten plants: "), 0, 10);
            stats.getRowConstraints().add(new RowConstraints(50));
            label = new Label(String.valueOf(animalToSpy.getEatenPlants()));
            stats.add(label, 1, 10);
            GridPane.setHalignment(label, HPos.CENTER);

            stats.add(new Label("\tNumber of children: "), 0, 11);
            stats.getRowConstraints().add(new RowConstraints(50));
            label = new Label(String.valueOf(animalToSpy.getChildren()));
            stats.add(label, 1, 11);
            GridPane.setHalignment(label, HPos.CENTER);

            if(animalToSpy.isAlive) {
                stats.add(new Label("\tAge: "), 0, 12);
                stats.getRowConstraints().add(new RowConstraints(50));
                label = new Label(String.valueOf(animalToSpy.getAge()));
            }
            else{
                stats.add(new Label("\tDay of death: "), 0, 12);
                stats.getRowConstraints().add(new RowConstraints(50));
                label = new Label(String.valueOf(animalToSpy.getDayOfDeath()));
            }
            stats.add(label, 1, 12);
            GridPane.setHalignment(label, HPos.CENTER);
        }
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
        try ( FileWriter fileWriter = new FileWriter(filename) ) {
            fileWriter.write(stringBuilder.toString());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public void updateCSV(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(day).append(",").append(map.animals().size()).append(",")
                .append(map.grasses().size()).append(",").append(map.howManyFreeFields()).append(",")
                .append(map.popularGenotype()).append(",").append(map.averageHP()).append(",")
                .append(map.averageAge()).append("\n");

        try ( FileWriter fileWriter = new FileWriter(filename, true) ) {
            fileWriter.write(stringBuilder.toString());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void nextDay(){
        day += 1;
        map.nextDay();
    }
    @Override
    public void reload(){
        nextDay();

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
    public void clickButton(ActionEvent event){
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
        HBox hbox1 = new HBox();
        VBox vbox = new VBox();
        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();

        Button startButton = new Button();
        startButton.setText("Start");
        startButton.setDisable(true);

        Button stopButton = new Button();
        stopButton.setText("Stop");

        Button spyButton = new Button();
        spyButton.setText("Spy animal");
        spyButton.setDisable(true);

        Button stopSpyButton = new Button();
        stopSpyButton.setText("Stop spying animal");
        stopSpyButton.setDisable(true);

        Button showGensButton = new Button();
        showGensButton.setText("Highlight popular genotype");
        showGensButton.setDisable(true);

        Button stopShowGensButton = new Button();
        stopShowGensButton.setText("Stop highlighting");
        stopShowGensButton.setDisable(true);

        grid = new GridPane();

        startButton.setOnAction(e -> {
            engine.resume();
            startButton.setDisable(true);
            stopButton.setDisable(false);
            spyButton.setDisable(true);
        });

        stopButton.setOnAction(e -> {
            engine.suspend();
            startButton.setDisable(false);
            stopButton.setDisable(true);
            spyButton.setDisable(false);
            showGensButton.setDisable(false);
            showGensButton.setDisable(false);
        });

        spyButton.setOnAction(e -> {
            spy = true;
            if(animalToSpy != null){
                spyButton.setDisable(true);
                stopSpyButton.setDisable(false);
            }
        });

        stopSpyButton.setOnAction(e -> {
            spy = false;
            animalToSpy = null;
            spyButton.setDisable(false);
            stopSpyButton.setDisable(false);
            drawObjects(map,grid);
        });

        showGensButton.setOnAction(e -> {
            showGensButton.setDisable(true);
            stopShowGensButton.setDisable(false);
            highlight = true;
            drawObjects(map, grid);
        });

        stopShowGensButton.setOnAction(e -> {
            showGensButton.setDisable(false);
            stopShowGensButton.setDisable(true);
            highlight = false;
            grid.getRowConstraints().clear();
            grid.getColumnConstraints().clear();
            grid.getChildren().retainAll(grid.getChildren().get(0));
            createGridMap();
        });

        hbox1.getChildren().add(0, startButton);
        hbox1.getChildren().add(1, stopButton);
        hbox1.getChildren().add(2, spyButton);
        hbox1.getChildren().add(3, stopSpyButton);
        hbox1.getChildren().add(4, showGensButton);
        hbox1.getChildren().add(5, stopShowGensButton);
        hbox1.setPadding(new Insets(0,20,0,20));
        hbox1.setSpacing(15);

        vbox.getChildren().add(0, hbox1);
        vbox.getChildren().add(1, grid);


        final NumberAxis xAxisAnimal = new NumberAxis();
        final NumberAxis yAxisAnimal = new NumberAxis();
        xAxisAnimal.setLabel("Day");
        animalChart = new LineChart(xAxisAnimal, yAxisAnimal);
        final NumberAxis xAxisGrass = new NumberAxis();
        final NumberAxis yAxisGrass = new NumberAxis();
        xAxisGrass.setLabel("Day");
        grassChart = new LineChart(xAxisGrass, yAxisGrass);

        vbox1.getChildren().add(0, animalChart);
        vbox1.getChildren().add(1, grassChart);


        stats = new GridPane();

        vbox2.getChildren().add(0, stats);


        hbox.getChildren().add(0, vbox);
        hbox.getChildren().add(1, vbox1);
        hbox.getChildren().add(2, vbox2);

        createGridMap();
        animalChart.getData().add(datasetAnimal);
        grassChart.getData().add(datasetGrass);
        createChart();
        createStats();

        if(save){
            filename = "C:\\TEMP\\stat" + now() + ".csv";
            createCSV();
        }

        Scene scene = new Scene(hbox, 1.3*row_length*width, 1.3*column_length*width);
        Stage secondStage = new Stage();
        secondStage.setScene(scene);
        secondStage.show();

        new Thread(engine).start();
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