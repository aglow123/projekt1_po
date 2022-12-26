package main.java.GrupaAA;

import java.util.Random;

public class Genotypes {

    int n = intGenerator(15);
    public int[] genotypes;

    public Genotypes(){
        this.genotypes = new int[n];
    }
    public int intGenerator(int max){
        Random generator = new Random();
        return generator.nextInt(max);
    }
    public void GenGenerator(){
        for(int i = 0; i< genotypes.length; i++){
            genotypes[i] = intGenerator(8);
        }
    }
}
