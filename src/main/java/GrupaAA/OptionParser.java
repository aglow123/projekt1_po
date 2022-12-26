package main.java.GrupaAA;

import java.util.Arrays;

public class OptionParser {

    public static MoveDirection[] parse(String[] table) throws IllegalArgumentException {
        MoveDirection[] moves = new MoveDirection[table.length];
        int j = 0;
        for (String el: table){
            if (el.equals("0")) {
                moves[j] = MoveDirection.FORWARD;
                j++;
            } else if (el.equals("1")) {
                moves[j] = MoveDirection.FORWARD_RIGHT;
                j++;
            } else if (el.equals("2")) {
                moves[j] = MoveDirection.RIGHT;
                j++;
            } else if (el.equals("3")) {
                moves[j] = MoveDirection.BACKWARD_RIGHT;
                j++;
            } else if (el.equals("4")) {
                moves[j] = MoveDirection.BACKWARD;
                j++;
            } else if (el.equals("5")){
                moves[j] = MoveDirection.BACKWARD_LEFT;
                j++;
            } else if (el.equals("6")){
                moves[j] = MoveDirection.LEFT;
                j++;
            } else if (el.equals("7")){
                moves[j] = MoveDirection.FORWARD_LEFT;
                j++;
            } else {
                throw new IllegalArgumentException(el + " is not legal move specification");
            }
        }
        if (j == table.length-1){
            return moves;
        }
        else {
            return Arrays.copyOfRange(moves,0,j);
        }
    }
}