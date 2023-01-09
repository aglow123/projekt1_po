package GrupaAA;

import java.util.*;

public class ToxicCorpses extends GrassField {

    private final Map<Vector2d, Integer> filledList;

    public ToxicCorpses(int height, int width, int typeOfBounds, int numberOfGrass) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        this.typeOfBounds = typeOfBounds;
        this.day = 1;
        this.filledList = fillCorpsesMap(corpsesList);
        for (int i = 0; i < numberOfGrass; i++) {
            PlantGrass();
        }
    }

    public void PlantGrass() {
        Map<Vector2d, Integer> sorted_map = valueSort(filledList);
        Random rand = new Random();
        double val = rand.nextDouble();
        int random_index;
        int corpses_number80 = (int)(0.8*filledList.size());
        int corpses_number20 = (int)(0.2*filledList.size());
        Vector2d newPosition = null;
        Set<Map.Entry<Vector2d, Integer> > entrySet = sorted_map.entrySet();
        while (true){
            if(val >= 0.2){
                random_index = rand.nextInt(corpses_number80);
                int index = 0;
                for (Map.Entry<Vector2d, Integer> currentEntry : entrySet) {
                    if (index == random_index) {
                        newPosition = currentEntry.getKey();
                    }
                    index++;
                }
            }
            else {
                random_index = rand.nextInt(corpses_number20) + corpses_number80;
                int index = 0;
                for (Map.Entry<Vector2d, Integer> currentEntry : entrySet) {
                    if (index == random_index) {
                        newPosition = currentEntry.getKey();
                    }
                    index++;
                }
            }
            if(!isPlanted(newPosition)){
                break;
            }
            else{
                val = rand.nextDouble();
            }
        }
        Grass grass = new Grass(newPosition);
        grasses.put(newPosition, grass);
    }

    public Map<Vector2d, Integer> fillCorpsesMap(Map<Vector2d, ArrayList<Animal>> corpsesList){
        Map<Vector2d, Integer> filledList = new HashMap<>();
        for(int x = lowerLeft.x;x< upperRight.x; x++){
            for(int y=lowerLeft.y;y<upperRight.y;y++){
                if(!corpsesList.containsKey(new Vector2d(x, y))){
                    filledList.put(new Vector2d(x, y), 0);
                }
                else {
                    filledList.put(new Vector2d(x, y), (corpsesList.get(new Vector2d(x, y)).size()));
                }
            }
        }
        return filledList;
    }

    public static <K, V extends Comparable<V> > Map<K, V>

    valueSort(final Map<K, V> map)
    {
        Comparator<K> valueComparator = (k1, k2) -> {
            int comp = map.get(k1).compareTo(
                    map.get(k2));
            if (comp == 0)
                return 1;
            else
                return comp;
        };
        Map<K, V> sorted = new TreeMap<K, V>(valueComparator);
        sorted.putAll(map);
        return sorted;
    }
}

