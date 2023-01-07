package GrupaAA.gui;

import GrupaAA.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class GuiElementBox {
    public VBox vbox = new VBox();
    public ProgressBar pb;

    public GuiElementBox(IMapElement element, Image image){

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        if(element.getElementName().equals("candy.png")){
            this.vbox = new VBox(imageView);
        }
        else{
            this.pb = new ProgressBar(1);
            this.vbox = new VBox(imageView, pb);
        }
        this.vbox.setAlignment(Pos.CENTER);
    }
}