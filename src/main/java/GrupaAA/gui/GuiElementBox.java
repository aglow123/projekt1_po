package GrupaAA.gui;

import GrupaAA.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    public VBox vbox = new VBox();
    public ProgressBar pb;

    public GuiElementBox(IMapElement element){
        String fileAddress = "src/main/resources/" + element.getElementName();
        Image image = null;
        try {
            image = new Image(new FileInputStream(fileAddress));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
//        Label label = new Label(element.getPosition().toString());
//        label.setMaxHeight(3);
        if(element.getElementName().equals("candy.png")){
            this.vbox = new VBox(imageView);
        }
        else{
            this.pb = new ProgressBar(1);
//            pb.setMaxHeight(5);
            this.vbox = new VBox(imageView, pb);
        }

        this.vbox.setAlignment(Pos.CENTER);
//        this.vbox = new VBox(imageView);
//        this.vbox.setAlignment(Pos.CENTER);
    }
}