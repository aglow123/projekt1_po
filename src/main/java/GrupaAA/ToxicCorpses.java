package GrupaAA;

import java.util.Random;

public class ToxicCorpses extends GrassField {

    public ToxicCorpses() {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        for (int i = 0; i < numberOfGrass; i++) {
            PlantGrass();
        }
    }

    public void PlantGrass() {
        int x, y;
        Vector2d newPosition;
        Random rand = new Random();
        while(true){
            x = rand.nextInt(width - 1);
            y = rand.nextInt(height - 1);
            newPosition = new Vector2d(x, y);
            if (!isPlanted(newPosition)) {
                if (corpsesList.containsKey(newPosition) && corpsesList.get(newPosition) != 0) {
                    corpsesList.put(newPosition, corpsesList.get(newPosition) - 1);
                    if (corpsesList.get(newPosition) == 0) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        Grass grass = new Grass(newPosition);
        grasses.put(newPosition, grass);
    }
}

