package aa.gui;

import aa.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import static java.lang.String.format;


public class App extends javafx.application.Application implements IGuiObserver {
    private SimulationEngineGui engine;
    private GridPane grid;
    private AbstractWorldMap map;
    private final int width = 500;
    private final int height = 500;


    public void init(){
        this.map = new GrassField(10);
        Vector2d[] positions = {new Vector2d(2, 3), new Vector2d(3, 8)};
        this.engine = new SimulationEngineGui(map, positions, this);
    }

    @Override
    public void start(Stage primaryStage) throws IllegalArgumentException {
        HBox upperPart = new HBox();
        Button startButton = new Button();
        TextField userInput = new TextField();
        startButton.setText("Start");
        startButton.setOnAction(e -> {
            if (userInput.getText().isEmpty())
                return;
            String[] newMoves = userInput.getText().split(" ");
            MoveDirection[] newDirection = OptionParser.parse(newMoves);
            engine.setDirections(newDirection);
            new Thread(engine).start();
        });

        upperPart.getChildren().add(0, startButton);
        upperPart.getChildren().add(1, userInput);

        VBox layout = new VBox();
        grid = new GridPane();

        layout.getChildren().add(0, upperPart);
        layout.getChildren().add(1, grid);

        createGridMap();
        Scene scene = new Scene(layout, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void createGridMap(){
        grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(20, 20, 20, 20));
        drawHeader(this.map, this.grid);
        drawObjects(this.map, this.grid);
    }

    void drawHeader(AbstractWorldMap map, GridPane grid){
        Label label = new Label("y\\x");
        grid.add(label, 0, 0);
        grid.getColumnConstraints().add(new ColumnConstraints(30));
        grid.getRowConstraints().add(new RowConstraints(30));
        GridPane.setHalignment(label, HPos.CENTER);
        for (int i = map.setBorders()[0].x; i <= map.setBorders()[1].x; i++) {
            label = new Label(format("%d", i));
            grid.add(label, i - map.setBorders()[0].x + 1, 0);
            grid.getColumnConstraints().add(new ColumnConstraints(30));
            GridPane.setHalignment(label, HPos.CENTER);

        }
        for (int j = map.setBorders()[0].y; j <= map.setBorders()[1].y; j++) {
            label = new Label(format("%d", j));
            grid.add(label, 0, map.setBorders()[1].y - j + 1);
            grid.getRowConstraints().add(new RowConstraints(30));
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    void drawObjects(AbstractWorldMap map, GridPane grid){
        for (int i = map.setBorders()[0].x; i <= map.setBorders()[1].x; i++) {
            for (int j = map.setBorders()[0].y; j <= map.setBorders()[1].y; j++) {
                Object toAdd = map.objectAt(new Vector2d(i, j));
                if (toAdd == null) {
                    continue;
                }
                IMapElement elementToAdd = (IMapElement) toAdd;
                GuiElementBox box = new GuiElementBox(elementToAdd);
                grid.add(box.vbox, i - map.setBorders()[0].x + 1, map.setBorders()[1].y - j + 1);
                GridPane.setHalignment(box.vbox, HPos.CENTER);
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
