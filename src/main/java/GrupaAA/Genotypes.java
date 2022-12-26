package main.java.GrupaAA;

import java.util.Random;

public class Genotypes {

    int maxGenLength;
    int n = intGenerator(maxGenLength);
    public int[] genotypes;

    public Genotypes(){

        this.genotypes = new int[n];
        genGenerator();
    }
    public int intGenerator(int max){
        Random generator = new Random();
        return generator.nextInt(max);
    }
    public void genGenerator(){
        for(int i = 0; i< genotypes.length; i++){
            genotypes[i] = intGenerator(8);
        }
    }
}
