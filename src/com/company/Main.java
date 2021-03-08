package com.company;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static final int ALPHA_START_VALUE = Integer.MIN_VALUE;
    private static final int BETA_START_VALUE = Integer.MAX_VALUE;
    final int PLAYFIELDSIZE = 3;
    private final int MAX_DEPTH = 10;
    private int depth = 0;
    private Node[][] playField = new Node[PLAYFIELDSIZE][PLAYFIELDSIZE];
    private boolean playersTurn = true;

    public static void main (String[] args) {
        Main m = new Main ();
        m.addPlayField ();
        //m.addPlay ();
        m.printStacks ();
        //m.testHorizontalScore ();
        //m.testVerticalScore ();
        m.startGame ();
    }

    private void testVerticalScore () {
        ArrayList<Coordinate> x = getPossiblePlays ();
    }

    private void testHorizontalScore () {
        ArrayList<Coordinate> x = getPossiblePlays ();
        int rank = calcHScores ( Node.Brick.PLAYER.value,x );
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
            System.out.println ( "computer Make a Movee" );
            //get the position
            MoveInfo m = findCompMove ( 0,ALPHA_START_VALUE,BETA_START_VALUE );
            isAccepted = isAccepted ( y,x );
        }
        if (checkifWeHaveWinner ( y,x )) {
            System.out.println ( "Winner" );
        } else {
            printStacks ();
            play ();
        }
    }

    private MoveInfo findCompMove (int depth,int alfa,int beta) {
        int x = 0, repsonseValue = 0;
        int value = 0;
        Coordinate bestMove = null;

        if (depth == MAX_DEPTH) {
            return new MoveInfo ( bestMove,value );
        }
        ArrayList<Coordinate> possiblePlays = getPossiblePlays ();
        if (fullBoard ()) {
            return new MoveInfo ( bestMove,0 );
        }/* else if ((quickWin = immediateComWin ()) != null) {
            return quickWin;
        }*/ else {
            value = -1;
            for (int i = 0; i < possiblePlays.size (); i++) {
                Coordinate position = possiblePlays.get ( i );
                boolean isPlaced = isAccepted ( position.getY (),position.getX () );
                repsonseValue = findHumanMove ( depth + 1,alfa,beta ).value;
                unPlace ( position );

                if (repsonseValue < beta) {
                    beta = repsonseValue;
                    bestMove = position;
                }
            }
        }
        return new MoveInfo ( bestMove,beta );
    }

    private boolean fullBoard () {
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                if (playField[i][j].getStatus ().equalsIgnoreCase ( "-" )) {
                    return false;
                }
            }
        }
        return true;
    }

    private MoveInfo findHumanMove (int depth,int alfa,int beta) {
        int x = 0, responseValue;
        int value = 0;
        Coordinate bestMove = null;
        ArrayList<Coordinate> possiblePlays = getPossiblePlays ();
        if (depth > MAX_DEPTH) {
            throw new IllegalArgumentException ();
        }
        if (depth == MAX_DEPTH) {
            return new MoveInfo ( bestMove,value );
        }
        if (fullBoard ()) {
            return new MoveInfo ( bestMove,0 );
        }/* else if ((quickWin = immediateComWin ()) != null) {
            return quickWin;
        }*/ else {
            value = 1;
            for (int i = 0; i < possiblePlays.size (); i++) {
                Coordinate position = possiblePlays.get ( i );
                boolean isPlaced = isAccepted ( position.getY (),position.getX () );

                responseValue = findCompMove ( depth + 1,alfa,beta ).value;
                unPlace ( position );

                if (responseValue > alfa) {
                    alfa = responseValue;
                    bestMove = position;
                }

            }
        }
        return new MoveInfo ( bestMove,alfa );
    }

    private void unPlace (Coordinate position) {
        playField[position.getY ()][position.getX ()].setStatus ( Node.Brick.NOTPLAYED );
        printStacks ();
    }

    private int calcScore (ArrayList<Coordinate> possiblePlays) {
        String player = Node.Brick.PLAYER.value;
        String computer = Node.Brick.COMPUTER.value;
        //countTheVerticalScore
        //countTheHorizontalScore
        //countTheDiagonalScore
        int h = calcHScores ( player,possiblePlays ) + diagonalScore ( player,possiblePlays );// + horizontalScore ( player, possiblePlays ) + verticalScore ( player, possiblePlays );
        // int c = diagonalScore ( computer,possiblePlays ) + horizontalScore ( computer,possiblePlays ) + verticalScore ( computer, possiblePlays );
        return 0;
    }


    /**
     * @param findWho       is the character to find either the players tokens or the computers.
     * @param possiblePlays
     * @return
     */
    private int calcHScores (String findWho,ArrayList<Coordinate> possiblePlays) {
        List<Coordinate> inRow = new LinkedList<> ();
        int score = 0;
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                if (playField[i][j].getStatus ().equals ( findWho )) {
                    inRow.add ( new Coordinate ( i,j ) );
                    score++;
                } else {
                    if (inRow.isEmpty ()) {
                        inRow.add ( new Coordinate ( i,j ) );
                    } else {
                        //Add the coordinate before the streak and after the streak
                        if (inRow.get ( 0 ).getX () > 0 && score != 0) {
                            if (playField[i][inRow.get ( 0 ).getX () - 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value )) {
                                Coordinate before = new Coordinate ( inRow.get ( 0 ).getX () - 1,i,score );
                                //rankedPlays.add ( before );
                            }
                        }
                        //Get the coordinate after the streak.
                        if (inRow.get ( inRow.size () - 1 ).getX () + 1 < PLAYFIELDSIZE) {
                            if (score != 0 && playField[i][inRow.get ( inRow.size () - 1 ).getX () + 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value )) {
                                Coordinate after = new Coordinate ( inRow.get ( inRow.size () - 1 ).getX () + 1,i,score );
                                //rankedPlays.add ( after );
                            }
                            //score = 0;
                        }
                        inRow.clear ();
                    }
                }
            }
        }
        return score;
    }

    /**
     * @param findWho       is the character to find either the players tokens or the computers.
     * @param possiblePlays
     * @return
     */
    private int calcVScores (String findWho,ArrayList<Coordinate> possiblePlays) {
        List<Coordinate> inRow = new LinkedList<> ();
        //TreeSet<Coordinate> rankedPlays = new TreeSet<> ();
        int score = 0;
        for (int i = 0; i < PLAYFIELDSIZE; i++) { // Columns
            for (int j = 0; j < PLAYFIELDSIZE; j++) { //The rows
                if (playField[j][i].getStatus ().equals ( findWho )) {
                    inRow.add ( new Coordinate ( j,i ) );
                    score++;
                } else {
                    if (inRow.isEmpty ()) {
                        inRow.add ( new Coordinate ( j,i ) );
                        //rankedPlays.add ( new Coordinate ( j,i,score ) );
                    } else {
                        //Add the coordinate above the streak if it's not less than 0
                        if (inRow.get ( 0 ).getY () > 0 && score != 0) {
                            if (playField[inRow.get ( 0 ).getY () - 1][i].getStatus ().equals ( Node.Brick.NOTPLAYED.value )) {
                                Coordinate before = new Coordinate ( i,inRow.get ( 0 ).getY () - 1,score );
                                //rankedPlays.add ( before );
                            }
                        }
                        //Add the coordinate below the streak if it's not greater than 10
                        if (inRow.get ( inRow.size () - 1 ).getY () + 1 < PLAYFIELDSIZE) {
                            if (score != 0 && playField[inRow.get ( inRow.size () - 1 ).getY () + 1][j].getStatus ().equals ( Node.Brick.NOTPLAYED.value )) {
                                Coordinate after = new Coordinate ( i,inRow.get ( inRow.size () - 1 ).getY () + 1,score );
                                //rankedPlays.add ( after );
                            }
                            score = 0;
                        }
                        inRow.clear ();
                    }
                }
            }
        }
        return score;
    }

    private int diagonalScore (String findWho,ArrayList<Coordinate> possiblePlays) {
        return 0;
    }

    /**
     *
     * @return
     */
    private ArrayList<Coordinate> getPossiblePlays () {
        ArrayList<Coordinate> possiblePlays = new ArrayList<> ();
        final String NOTPLAYED = Node.Brick.NOTPLAYED.value;
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                if (playField[i][j].getStatus ().equals ( NOTPLAYED )) {
                    possiblePlays.add ( new Coordinate ( i,j ) );
                }
            }
        }
        return possiblePlays;
    }

    /**
     *
     * @param y
     * @param x
     * @return
     */
    private boolean isAccepted (int y,int x) {
        if (playField[y][x].toString ().equals ( "|" + Node.Brick.NOTPLAYED + "|" )) {
            playField[y][x].setStatus ( getWhosTurn () );
            printStacks ();
            switchPlayer ();
            return true;
        }
        return false;
    }

    /**
     * Flips to the next persons turn
     */
    private void switchPlayer () {
        if (playersTurn) playersTurn = false;
        else playersTurn = true;
    }

    /**
     *
     * @param y
     * @param x
     * @return
     */
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
        String x = "";
        for(int i = 0; i < PLAYFIELDSIZE; i++){
            if(i==0){
                x = "t" + "|";
            }
            x = i + "||";
            System.out.print (x);
        }
        //System.out.println ( "t" + "|" + 0 + "||" + 1 + "||" + 2 + "||" + 3 + "||" + 4 + "||" + 5 + "||" + 6 + "||" + 7 + "||" + 8 + "||" + 9 + "|" );
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

    private class MoveInfo {
        private Coordinate move;
        private int value;

        public MoveInfo (Coordinate move,int value) {
            this.move = move;
            this.value = value;
        }
    }
}
