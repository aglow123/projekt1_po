package GrupaAA;

import java.util.Random;

public class Genotypes {

    static int maxGenLength;
    public int[] genotypes;

    public Genotypes(){
        this.genotypes = new int[maxGenLength];
        genGenerator();
    }
    public static int intGenerator(int max){
        Random generator = new Random();
        return generator.nextInt(max) + 1;
    }
    public void genGenerator(){
        for(int i = 0; i< genotypes.length; i++){
            genotypes[i] = intGenerator(8);
        }
    }
}
