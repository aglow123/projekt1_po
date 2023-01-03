package GrupaAA;

import java.util.Random;

public class Genotypes {

    public static int maxGenLength;
    public int[] genotypes;

    public Genotypes(int genLength){
        this.maxGenLength = genLength;
        this.genotypes = new int [genLength];
        genGenerator();
    }
    public static int intGenerator(int max){
        Random generator = new Random();
        return generator.nextInt(max);
    }
    public void genGenerator(){
        for(int i = 0; i< genotypes.length; i++){
            genotypes[i] = intGenerator(8);
        }
    }
}
