package com.company;

import java.util.ArrayList;

public class Main {
    final int PLAYFIELDSIZE = 10;
    private Node[][] playField = new Node[10][10];
    private boolean playersTurn = true;

    public static void main(String[] args) {
        Main m = new Main ();
        m.addPlayField ( );
        m.printStacks ();
        m.startGame();
    }

    private void startGame () {
        Scan s = new Scan ();
        int i = 0;
        try {
            System.out.println ("Write 1 and enter to play");
            i = s.getInt ();
        }catch (NumberFormatException e){
            System.out.println ("a number!");
            startGame ();
        }
        if(i == 1){
            play();
        }
    }

    private void play () {
        int x =0;
        int y =0;
        Scan s = new Scan ();
        if(playersTurn) {
            //get the position
            System.out.print ("X:");
            x = s.getInt ();
            System.out.print ("Y: ");
            y = s.getInt ();
            playField[x][y].setStatus ( getWhosTurn () );
        }else{ //Computers turn to make a move
            System.out.println ("computerMake a Movee");
            //get the position
            System.out.print ("X:");
            x = s.getInt ();
            System.out.print ("Y: ");
            y = s.getInt ();
            playField[x][y].setStatus ( getWhosTurn () );
        }
        if (checkifWeHaveWinner ( x,y )) {
            System.out.println ( "finished" );
        } else {
            printStacks ();
            switchPlayer ();
            play ();
        }
    }

    private void switchPlayer(){
        if(playersTurn) playersTurn = false;
        else playersTurn = true;
    }

    private boolean checkifWeHaveWinner (int x, int y) {
        int xPos = 0;
        int xMin = 0;
        int yPos = 0;
        int yMin = 0;
        int rightUp = 0;
        int leftUp = 0;
        int rightDown = 0;
        int leftDown = 0;

        for(int i = 0 ; i < 5; i++){
                //Check all rows uppwards
                if (y+i < PLAYFIELDSIZE+1) {
                    if(playField[x][y+i].equals ( getWhosTurn ().value )) yPos++;
                    else yPos=0;
                }
                //Check all rows downWards
                if (y-i > -1) {
                    if(playField[x][y-i].equals ( getWhosTurn ().value )) yMin++;
                    else yMin=0;
                }

                if (x+i < PLAYFIELDSIZE+1) {
                    //Check all kolumns rightwards
                    if(playField[x+i][y].equals ( getWhosTurn ().value)) xPos++;
                    else xPos--;
                }

                if(x-i > -1) {
                    //Check all kolumns left wards
                    if (playField[x - i][y].equals ( getWhosTurn ().value )) xMin++;
                    else xMin = 0;
                }
                //Right upwards
                if (x+i < PLAYFIELDSIZE+1 && y+i < PLAYFIELDSIZE+1) {
                    if(playField[x+i][y+i].equals ( getWhosTurn ().value )) rightUp++;
                    else rightUp=0;
                }
                //Left upwards
                if (x-i > 0 && y + i < PLAYFIELDSIZE+1) {
                    if(playField[x-i][y+i].equals ( getWhosTurn ().value ))leftUp++;
                    else leftUp=0;
                }
                //RightDown
                if (x+i < PLAYFIELDSIZE+1 && y-i > -1) {
                    if (playField[x+i][y-i].equals ( getWhosTurn ().value )) rightDown++;
                    else rightDown =0;
                }
                //left down
                if (x-i > -1 && y-i > -1) {
                    if(playField[x-i][y-i].equals ( getWhosTurn ().value )) leftDown++;
                    leftDown=0;
                }


            if(xPos == 5 || xMin == 5 || yPos == 5 || yMin == 5 || rightUp == 5 || rightDown == 5 || leftDown == 5 ||leftUp == 5){
                return true;
            }
        }
        return false;
    }

    private Node.Brick getWhosTurn () {
        return playersTurn ? Node.Brick.PLAYER : Node.Brick.COMPUTER;
    }

    private void addPlayField () {
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                playField[i][j] = new Node ( Node.Brick.NOTPLAYED );
            }
        }
    }

    private void printStacks () {
        System.out.println ( "t"+"|" + 0 + "||" + 1 + "||" + 2 + "||" + 3 + "||" + 4 + "||" + 5 + "||" + 6 + "||" + 7 + "||" + 8 + "||" + 9 + "|");
        int column = 1;
        int row = 0;
        String s = row + "";
        for (Node[] n : playField) {
            System.out.print (row++);
            for (int i = 0; i < PLAYFIELDSIZE; i++) {
                System.out.print ( n[i] );
            }
            System.out.println ();
        }
    }
}
