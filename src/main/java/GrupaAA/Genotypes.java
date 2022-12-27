package main.java.GrupaAA;

import java.util.Random;

public class Genotypes {

    int maxGenLength;
    int genotypesLength = intGenerator(maxGenLength);
    public int[] genotypes;

    public Genotypes(){

        this.genotypes = new int[genotypesLength];
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
