package com.company;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class Main {
    private static final int ALPHA_START_VALUE = Integer.MIN_VALUE;
    private static final int BETA_START_VALUE = Integer.MAX_VALUE;
    final int PLAYFIELDSIZE = 10;
    private final int MAX_DEPTH = 3;
    private int depth = 0;
    private Node[][] playField = new Node[10][10];
    private boolean playersTurn = true;

    public static void main (String[] args) {
        Main m = new Main ();
        m.addPlayField ();
        m.addPlay ();
        m.printStacks ();
        //m.testHorizontalScore ();
        m.testVerticalScore ();
        //m.startGame ();
    }

    private void testVerticalScore () {
        ArrayList<Coordinate> x = getPossiblePlays ();
        TreeSet<Coordinate> rank = calcVScores ( Node.Brick.COMPUTER.value,x );
    }

    private void testHorizontalScore () {
        ArrayList<Coordinate> x = getPossiblePlays ();
        TreeSet<Coordinate> rank = calcHScores ( Node.Brick.PLAYER.value,x );
    }

    private void addPlay () {
        for (int i = 0; i < 4; i++) {
            playField[2 + i][i + 1].setStatus ( Node.Brick.COMPUTER );
            playField[3 + i][i + 1].setStatus ( Node.Brick.PLAYER );
        }

        for (int i = 0; i < 4; i++) {
            playField[4 + i][0].setStatus ( Node.Brick.COMPUTER );
            playField[4 + i][9].setStatus ( Node.Brick.PLAYER );
        }

        for (int i = 0; i < 4; i++) {
            playField[9][3 + i].setStatus ( Node.Brick.COMPUTER );
            playField[8][3 + i].setStatus ( Node.Brick.PLAYER );
        }

        for (int i = 0; i < 4; i++) {
            playField[2 + i][8 - i].setStatus ( Node.Brick.COMPUTER );
            playField[2 + i][7 - i].setStatus ( Node.Brick.PLAYER );
        }
    }


    private void startGame () {
        Scan s = new Scan ();
        int i = 0;
        try {
            System.out.println ( "Write 1 and enter to play" );
            i = s.getInt ();
        } catch (NumberFormatException e) {
            System.out.println ( "a number!" );
            startGame ();
        }
        if (i == 1) {
            play ();
        }
    }

    private void play () {
        int x = 0;
        int y = 0;
        Scan s = new Scan ();
        boolean isAccepted = false;
        if (playersTurn) {
            while (!isAccepted) {
                System.out.println ( "person" );
                //get the position
                try {
                    System.out.print ( "X: " );
                    x = s.getInt ();
                    System.out.print ( "Y: " );
                    y = s.getInt ();
                } catch (NumberFormatException e) {
                    e.printStackTrace ();
                }
                isAccepted = isAccepted ( y,x );
            }
        } else { //Computers turn to make a move
            while (!isAccepted) {
                System.out.println ( "computer Make a Movee" );
                //get the position
                TreeSet<Coordinate> l = minMax ();
                Coordinate computersChoice = l.first ();
                isAccepted = isAccepted ( y,x );
            }
        }
        if (checkifWeHaveWinner ( y,x )) {
            System.out.println ( "Winner" );
        } else {
            printStacks ();
            switchPlayer ();
            play ();
        }
    }

    private TreeSet<Coordinate> minMax () {
        Coordinate bestMove;
        //Gets all the possible plays
        ArrayList<Coordinate> x = getPossiblePlays ();

        if (x.isEmpty ()) {
            return null;
        }
        if (depth == MAX_DEPTH) {
            return calcScore ( x );
        }

        //Trying to minimize
        if (playersTurn) {
            //Loops through all possible plays
            for (Coordinate current : x) {
                //Play the brick
                isAccepted ( current.getX (),current.getY () );


            }
        } else {

        }
        return null;
    }

    private TreeSet<Coordinate> calcScore (ArrayList<Coordinate> possiblePlays) {
        String player = Node.Brick.PLAYER.value;
        String computer = Node.Brick.COMPUTER.value;
        //countTheVerticalScore
        //countTheHorizontalScore
        //countTheDiagonalScore
        TreeSet<Coordinate> x = calcHScores ( player,possiblePlays );//diagonalScore ( player, possiblePlays );// + horizontalScore ( player, possiblePlays ) + verticalScore ( player, possiblePlays );
        // int c = diagonalScore ( computer,possiblePlays ) + horizontalScore ( computer,possiblePlays ) + verticalScore ( computer, possiblePlays );
        return x;
    }


    /**
     * @param findWho       is the character to find either the players tokens or the computers.
     * @param possiblePlays
     * @return
     */
    private TreeSet<Coordinate> calcHScores (String findWho,ArrayList<Coordinate> possiblePlays) {
        List<Coordinate> inRow = new LinkedList<> ();
        TreeSet<Coordinate> rankedPlays = new TreeSet<> ();
        int score = 0;
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                if (playField[i][j].getStatus ().equals ( findWho )) {
                    inRow.add ( new Coordinate ( i,j ) );
                    score++;
                } else {
                    if (inRow.isEmpty ()) {
                        inRow.add ( new Coordinate ( i,j ) );
                        rankedPlays.add ( new Coordinate ( i,j,score ) );
                    } else {
                        //Add the coordinate before the streak and after the streak
                        if (inRow.get ( 0 ).getX () > 0 && score != 0) {
                            if (playField[i][inRow.get ( 0 ).getX () - 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value )) {
                                Coordinate before = new Coordinate ( inRow.get ( 0 ).getX () - 1,i,score );
                                rankedPlays.add ( before );
                            }
                        }
                        //Get the coordinate after the streak.
                        if (inRow.get ( inRow.size () - 1 ).getX () + 1 < PLAYFIELDSIZE) {
                            if (score != 0 && playField[i][inRow.get ( inRow.size () - 1 ).getX () + 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value )) {
                                Coordinate after = new Coordinate ( inRow.get ( inRow.size () - 1 ).getX () + 1,i,score );
                                rankedPlays.add ( after );
                            }
                            score = 0;
                        }
                        inRow.clear ();
                    }
                }
            }
            //Reset the score for eachLap
            score = 0;
        }
        return rankedPlays;
    }

    /**
     * @param findWho       is the character to find either the players tokens or the computers.
     * @param possiblePlays
     * @return
     */
    private TreeSet<Coordinate> calcVScores (String findWho,ArrayList<Coordinate> possiblePlays) {
        List<Coordinate> inRow = new LinkedList<> ();
        TreeSet<Coordinate> rankedPlays = new TreeSet<> ();
        int score = 0;
        for (int i = 0; i < PLAYFIELDSIZE; i++) { // Columns
            for(int j = 0 ; j < PLAYFIELDSIZE; j++){ //The rows

            }

        }
        return rankedPlays;
    }

    private int diagonalScore (String findWho,ArrayList<Coordinate> possiblePlays) {
        return 0;
    }

    private ArrayList<Coordinate> getPossiblePlays () {
        ArrayList<Coordinate> possiblePlays = new ArrayList<> ();
        final String NOTPLAYED = Node.Brick.NOTPLAYED.value;
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                if (playField[i][j].toString ().equals ( NOTPLAYED )) {
                    possiblePlays.add ( new Coordinate ( i,j ) );
                }
            }
        }
        return possiblePlays;
    }

    private boolean isAccepted (int y,int x) {
        if (playField[y][x].toString ().equals ( "|" + Node.Brick.NOTPLAYED + "|" )) {
            playField[y][x].setStatus ( getWhosTurn () );
            return true;
        }
        return false;
    }

    private void switchPlayer () {
        if (playersTurn) playersTurn = false;
        else playersTurn = true;
    }

    private boolean checkifWeHaveWinner (int y,int x) {
        int xPos = 0;
        int xMin = 0;
        int yPos = 0;
        int yMin = 0;
        int rightUp = 0;
        int leftUp = 0;
        int rightDown = 0;
        int leftDown = 0;
        String player = "|" + getWhosTurn ().value + "|";

        for (int i = 0; i < 5; i++) {
            //Check all rows uppwards
            if (y - i >= 0) {
                //testWhichNodeAreWeLookingAt(playField[y-i][x]);
                if (playField[y - i][x].toString ().equals ( player ))
                    yPos++;
                else yPos = 0;
            }
            //Check all rows downWards
            if (y + i < PLAYFIELDSIZE) {
                //testWhichNodeAreWeLookingAt(playField[y+i][x]);
                if (playField[y + i][x].toString ().equals ( player ))
                    yMin++;
                else yMin = 0;
            }

            if (x + i < PLAYFIELDSIZE) {
                //Check all kolumns rightwards
                //testWhichNodeAreWeLookingAt ( playField[y][x + i] );
                if (playField[y][x + i].toString ().equals ( player ))
                    xPos++;
                else xPos = 0;
            }

            if (x - i >= 0) {
                //Check all kolumns left wards
                int s = x - i;
                //testWhichNodeAreWeLookingAt(playField[y][s]);
                if (playField[y][s].toString ().equals ( player ))
                    xMin++;
                else xMin = 0;
            }
            //Right upwards
            if (x + i < PLAYFIELDSIZE && y - i >= 0) {
                //testWhichNodeAreWeLookingAt(playField[y-i][x+i]);
                if (playField[y - i][x + i].toString ().equals ( player ))
                    rightUp++;
                else rightUp = 0;
            }
            //Left upwards
            if (x - i >= 0 && y - i >= 0) {
                // testWhichNodeAreWeLookingAt(playField[y-i][x-i]);
                if (playField[y - i][x - i].toString ().equals ( player )) leftUp++;
                else leftUp = 0;
            }
            //RightDown
            if (x + i < PLAYFIELDSIZE && y + i < PLAYFIELDSIZE) {
                //testWhichNodeAreWeLookingAt(playField[y+i][x+i]);
                if (playField[y + i][x + i].toString ().equals ( player ))
                    rightDown++;
                else rightDown = 0;
            }
            //left down
            if (x - i >= 0 && y + i < PLAYFIELDSIZE) {
                if (playField[y + i][x - i].toString ().equals ( player ))
                    leftDown++;
                else leftDown = 0;
            }


            if (xPos == 5 || xMin == 5 || yPos == 5 || yMin == 5 || rightUp == 5 || rightDown == 5 || leftDown == 5 || leftUp == 5) {
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
        System.out.println ( "t" + "|" + 0 + "||" + 1 + "||" + 2 + "||" + 3 + "||" + 4 + "||" + 5 + "||" + 6 + "||" + 7 + "||" + 8 + "||" + 9 + "|" );
        int column = 1;
        int row = 0;
        String s = row + "";
        for (Node[] n : playField) {
            System.out.print ( row++ );
            for (int i = 0; i < PLAYFIELDSIZE; i++) {
                System.out.print ( n[i] );
            }
            System.out.println ();
        }
    }
}
