package GrupaAA;

import java.util.*;

public class ToxicCorpses extends GrassField {

    public ToxicCorpses(int height, int width, int typeOfBounds, int numberOfGrass) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
        this.typeOfBounds = typeOfBounds;
        fillCorpsesMap(corpsesList);
        for (int i = 0; i < numberOfGrass; i++) {
            PlantGrass();
        }
    }

    public void PlantGrass() {
        //        int x, y;
//        Vector2d newPosition;
//        Random rand = new Random();
//        while(true){
//            x = rand.nextInt(width - 1);
//            y = rand.nextInt(height - 1);
//            newPosition = new Vector2d(x, y);
//            if (!isPlanted(newPosition)) {
//                if (corpsesList.containsKey(newPosition) && corpsesList.get(newPosition) != 0) {
//                    corpsesList.put(newPosition, corpsesList.get(newPosition) - 1);
//                    if (corpsesList.get(newPosition) == 0) {
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            }
//        }
//        Grass grass = new Grass(newPosition);
//        grasses.put(newPosition, grass);

        Map<Vector2d, Integer> sorted_map = valueSort(corpsesList);
        boolean val = new Random().nextDouble() < 0.2;
        Random rand = new Random();
        int random_index;
        int corpses_number80 = (int)(0.8*corpsesList.size());
        int corpses_number20 = (int)(0.2*corpsesList.size());
        Vector2d newPosition = null;
        Set<Map.Entry<Vector2d, Integer> > entrySet = sorted_map.entrySet();
        if(!val){
            do {
                random_index = rand.nextInt(corpses_number80);
                int index = 0;
                for (Map.Entry<Vector2d, Integer> currentEntry : entrySet) {
                    if (index == random_index) {
                        newPosition = currentEntry.getKey();
                    }
                    index++;
                }
            } while (isPlanted(newPosition));
        }
        else{
            do {
                random_index = rand.nextInt(corpses_number20) + corpses_number80;
                int index = 0;
                for (Map.Entry<Vector2d, Integer> currentEntry : entrySet) {
                    if (index == random_index) {
                        newPosition = currentEntry.getKey();
                    }
                    index++;
                }
            } while (isPlanted(newPosition));
        }

        Grass grass = new Grass(newPosition);
        grasses.put(newPosition, grass);
    }

    public void fillCorpsesMap(Map<Vector2d, Integer> corpsesList){
        for(int x = lowerLeft.x;x< upperRight.x; x++){
            for(int y=lowerLeft.y;y<upperRight.y;y++){
                corpsesList.put(new Vector2d(x,y), 0);
            }
        }
    }

    public static <K, V extends Comparable<V> > Map<K, V>

    valueSort(final Map<K, V> map)
    {
        Comparator<K> valueComparator = new Comparator<K>() {
            public int compare(K k1, K k2)
            {
                int comp = map.get(k1).compareTo(
                        map.get(k2));
                if (comp == 0)
                    return 1;
                else
                    return comp;
            }
        };
        Map<K, V> sorted = new TreeMap<K, V>(valueComparator);
        sorted.putAll(map);
        return sorted;
    }
}

