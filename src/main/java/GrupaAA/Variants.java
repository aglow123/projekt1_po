package GrupaAA;

public class Variants {

    public static void mutation(Animal baby, String whichOne){
        int howManyGens=Genotypes.intGenerator(5)-1;
        System.out.println(howManyGens);
        if(whichOne.equals("Random")){
            for(int i =0; i<howManyGens; i++){
                int randomPosition = Genotypes.intGenerator(baby.animalGens.length)-1;
                baby.animalGens[randomPosition] = Genotypes.intGenerator(8);
            }
        }
        else{
            for(int i =0; i<howManyGens; i++){
                int randomPosition = Genotypes.intGenerator(baby.animalGens.length)-1;
                int change = 1;
                if (Genotypes.intGenerator(2)==2){
                    change = -1;
                }
                baby.animalGens[randomPosition] += change;
            }
        }
    }

    public static int animalBehavior(String whichBehaviour, int nextMove) {
        if (whichBehaviour.equals("Random")) {
            int random = Genotypes.intGenerator(10);
            if (random <= 2) {
                nextMove = Genotypes.intGenerator(Genotypes.maxGenLength);
            }
        }
        return nextMove;
    }
}
