package aa;

import static java.lang.System.out;

import aa.gui.App;
import javafx.application.Application;

public class World {
    public static void main(String[] args) {
        try {
            Application.launch(App.class, args);
        } catch(Exception ex){
            out.println(ex.getMessage());
            out.println(ex.getClass());
        }
    }
}
