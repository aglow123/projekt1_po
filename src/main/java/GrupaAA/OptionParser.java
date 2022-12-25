package GrupaAA;

import java.util.Arrays;

public class OptionParser {

    public static MoveDirection[] parse(String[] table) throws IllegalArgumentException {
        MoveDirection[] moves = new MoveDirection[table.length];
        int j = 0;
        for (String el: table){
            if (el.equals("f") || el.equals("forward")) {
                moves[j] = MoveDirection.FORWARD;
                j++;
            } else if (el.equals("b") || el.equals("backward")) {
                moves[j] = MoveDirection.BACKWARD;
                j++;
            } else if (el.equals("r") || el.equals("right")) {
                moves[j] = MoveDirection.RIGHT;
                j++;
            } else if (el.equals("l") || el.equals("left")) {
                moves[j] = MoveDirection.LEFT;
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