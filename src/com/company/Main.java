package com.company;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static final int ALPHA_START_VALUE = Integer.MIN_VALUE;
    private static final int BETA_START_VALUE = Integer.MAX_VALUE;
    final int PLAYFIELDSIZE = 5;
    private final int MAX_DEPTH = 10;
    private int ROW_TO_WIN = PLAYFIELDSIZE - 1;/// 2;
    private int depth = 0;
    private Node[][] playField = new Node[PLAYFIELDSIZE][PLAYFIELDSIZE];
    private boolean playersTurn = true;
    private Coordinate computerMove;
    private List<Coordinate> playedPositions = new ArrayList<> ();

    public static void main (String[] args) {
        Main m = new Main ();
        m.addPlayField ();
        // m.addPlay ();
      //  m.addPlay2 ();
        m.printStacks ();
        //m.testHorizontalScore ();
        //m.testVerticalScore ();
        m.startGame ();
        // int i = m.getPlayFieldScore ();
        //System.out.println (i);
        //m.leftTopToRightBottomAndRight(Node.Brick.PLAYER.value );
//        int i = m.horizontalScore ( Node.Brick.PLAYER.value );
//        int j = m.verticalScore ( Node.Brick.PLAYER.value );
//        int x = m.leftTopToRightBottomAndRight ( Node.Brick.PLAYER.value );
//        int z = m.leftTopToRightBottomAndLeft ( Node.Brick.PLAYER.value );
//        int y = m.rightTopToLeftBottomAndLeft ( Node.Brick.PLAYER.value );
//        int k = m.rightTopToLeftBottomAndRight ( Node.Brick.PLAYER.value );
    }

    private void addPlay2 () {
        for(int i = 0; i < PLAYFIELDSIZE; i++){
            if(i % 2 == 0){
                playField[i][3-i].setStatus ( Node.Brick.PLAYER );
                playField[i+1][3-i].setStatus ( Node.Brick.COMPUTER );
            }
        }
        playField[2][2].setStatus ( Node.Brick.PLAYER );
        playField[1][2].setStatus ( Node.Brick.PLAYER );
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
            isAccepted = isAccepted ( Node.Brick.PLAYER,y,x );
        }
        if (checkifWeHaveWinner ( y,x )) {
            System.out.println ( "Player is the winner" );
            return;
        }
        System.out.println ( "computer Make a Movee" );
        //get the position
        long start = System.currentTimeMillis ();
        int i = findCompMove ( 0,ALPHA_START_VALUE,BETA_START_VALUE );
        System.out.println ( (System.currentTimeMillis () - start) + "ms" );
        isAccepted = isAccepted ( Node.Brick.COMPUTER,computerMove.getY (),computerMove.getX () );
        printStacks ();

        if (checkifWeHaveWinner ( y,x )) {
            System.out.println ( "Computer is the winner" );
            return;
        } else {
            play ();
        }
    }

    private int findCompMove (int depth,int alfa,int beta) {
        int repsonseValue = 0;
        Coordinate position = null;
        int value = getPlayFieldScore (); //ett värde baserat på hur spelplanen ser ut för denna spelare
        if (depth == MAX_DEPTH) {
            return value;
        }
        if (fullBoard ()) {
            return value;
        } else {
            ArrayList<Coordinate> possiblePlays = possiblePlaysLimited ();
            for (int i = 0; i < possiblePlays.size (); i++) {
                position = possiblePlays.get ( i );
                boolean isPlaced = isAccepted ( Node.Brick.COMPUTER,position.getY (),position.getX () );
                value -= findHumanMove ( depth + 1,alfa,beta );
                unPlace ( position );
                if (checkifWeHaveWinner ( position.getY (),position.getX () )) {
                    return -value;
                }
                if (value < beta) {
                    beta = value;
                }
                if (alfa >= beta) {
                    break;
                }
            }
        }
        computerMove = position;
        return beta;
    }

    private int findHumanMove (int depth,int alfa,int beta) {
        Coordinate position = null;
        int value = getPlayFieldScore (); //ett värde baserat på hur spelplanen ser ut för denna spelare
        if (depth > MAX_DEPTH) {
            throw new IllegalArgumentException ();
        }
        if (depth == MAX_DEPTH) {
            return value;
        }
        if (fullBoard ()) {
            return value;
        } else {
            ArrayList<Coordinate> possiblePlays = possiblePlaysLimited ();
            for (int i = 0; i < possiblePlays.size (); i++) {
                position = possiblePlays.get ( i );
                boolean isPlaced = isAccepted ( Node.Brick.PLAYER,position.getY (),position.getX () );
                if (checkifWeHaveWinner ( position.getY (),position.getX () )) {
                    return value;
                }
                value += findCompMove ( depth + 1,alfa,beta );
                unPlace ( position );
                if (value > alfa) {
                    alfa = value;
                }
                if (alfa >= beta) {
                    break;
                }
            }
        }
        computerMove = position;
        return alfa;
    }

    private int getPlayFieldScore () {
        String player = Node.Brick.PLAYER.value;
        String computer = Node.Brick.COMPUTER.value;
        int playerScore = verticalScore ( player ) + horizontalScore ( player ) + diagonalScore ( player );
        int computerScore = verticalScore ( computer ) + horizontalScore ( computer ) + diagonalScore ( computer );
        return playerScore - computerScore;
    }

    private int horizontalScore (String player) {
        int score = 0;
        List<Coordinate> inARow = new LinkedList<> ();
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                if (playField[j][i].getStatus ().equals ( player )) {
                    inARow.add ( new Coordinate ( i,j ) );
                } else if (!inARow.isEmpty ()) {
                    //Get the coordinate before
                    Coordinate before = new Coordinate ( inARow.get ( 0 ).getY (),inARow.get ( 0 ).getX () - 1 );
                    //Get the coordinate after
                    Coordinate after = new Coordinate ( inARow.get ( inARow.size () - 1 ).getY (),inARow.get ( inARow.size () - 1 ).getX () - 1 );
                    score += calculateScore ( inARow,before,after );
                    inARow.clear ();
                }
            }
        }
        return score;
    }

    private int calculateScore (List<Coordinate> inARow,Coordinate beforeStart,Coordinate afterEnd) {
        int score = 0;
        boolean isBeforeStartEmpty;
        boolean isAfterEndEmpty;
        try {
            isBeforeStartEmpty = checkNode ( beforeStart );
        } catch (ArrayIndexOutOfBoundsException e) {
            isBeforeStartEmpty = false;
        }
        try {
            isAfterEndEmpty = checkNode ( afterEnd );
        } catch (ArrayIndexOutOfBoundsException e) {
            isAfterEndEmpty = false;
        }
        if (isBeforeStartEmpty || isAfterEndEmpty) {
            score += calculateListScore ( inARow );
            if (isBeforeStartEmpty && isAfterEndEmpty) {
                score += 10;
            }
        }
        return inARow.size ()+score;
    }

    private int calculateListScore (List<Coordinate> inARow) {
        int score = 1;
        for (Coordinate c : inARow)
            score *= 10;
        return score;
    }

    private boolean checkNode (Coordinate coordinate) throws ArrayIndexOutOfBoundsException {
        return playField[coordinate.getY ()][coordinate.getX ()].getStatus ().equals ( Node.Brick.NOTPLAYED.value );
    }

    private int verticalScore (String player) {
        int score = 0;
        List<Coordinate> inARow = new LinkedList<> ();
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            for (int j = 0; j < PLAYFIELDSIZE; j++) {
                if (playField[i][j].getStatus ().equals ( player )) {
                    inARow.add ( new Coordinate ( i,j ) );
                } else if (!inARow.isEmpty ()) {
                    //Get the coordinate before
                    Coordinate before = new Coordinate ( inARow.get ( 0 ).getY () - 1,inARow.get ( 0 ).getX () );
                    //Get the coordinate after
                    Coordinate after = new Coordinate ( inARow.get ( inARow.size () - 1 ).getY () - 1,inARow.get ( inARow.size () - 1 ).getX () );
                    score += calculateScore ( inARow,before,after );
                    inARow.clear ();
                }
            }
        }
        return score;
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

    private int diagonalScore (String player) {
        int rightTopLeftBottom = rightTopToLeftBottomAndRight ( player ) + rightTopToLeftBottomAndLeft ( player );
        int leftTopRightBottom = leftTopToRightBottomAndLeft ( player ) + leftTopToRightBottomAndRight ( player );
        return rightTopLeftBottom + leftTopRightBottom;
    }

    private int rightTopToLeftBottomAndRight (String player) {
        int x = 0;
        int y = 0;
        int score = 0;
        int count = 0;
        List<Coordinate> inARow = new LinkedList<> ();

        for (int i = 1; i <= PLAYFIELDSIZE - 1; i++) {   //Yvärdet börjar ifrån 0 och går neråt på kartan
            x = PLAYFIELDSIZE - 1;
            for (int j = PLAYFIELDSIZE; j > 0; j--) { //Xvärdet börjar ifrån storleken på kartan - i
                if (x > -1 && y < PLAYFIELDSIZE) {
                    score = getScore ( player,x,y,score,inARow );
                    //playField[y][x].setStatus ( Node.Brick.THISNODE );
                    y++;
                    x--;
                    count++;
                }
            }
            if (count < ROW_TO_WIN) {
                break;
            }
            y = i;
            count = 0;
        }
        return score;
    }

    private int getScore (String player,int x,int y,int score,List<Coordinate> inARow) {
        if (playField[y][x].getStatus ().equals ( player )) {
            inARow.add ( new Coordinate ( y,x ) );
        } else if (!inARow.isEmpty ()) {
            //Get the coordinate before
            Coordinate before = new Coordinate ( inARow.get ( 0 ).getY () - 1,inARow.get ( 0 ).getX () + 1 );
            //Get the coordinate after
            Coordinate after = new Coordinate ( inARow.get ( inARow.size () - 1 ).getY () + 1,inARow.get ( inARow.size () - 1 ).getX () );
            score += calculateScore ( inARow,before,after );
            inARow.clear ();
        }
        return score;
    }

    private int rightTopToLeftBottomAndLeft (String player) {
        int x = PLAYFIELDSIZE - 1;
        int y = 0;
        int score = 0;
        int count = 0;
        List<Coordinate> inARow = new LinkedList<> ();

        for (int i = 1; i <= PLAYFIELDSIZE - 1; i++) {   //Yvärdet börjar ifrån 0 och går neråt på kartan
            x = PLAYFIELDSIZE - i;
            for (int j = PLAYFIELDSIZE; j > 0; j--) { //Xvärdet börjar ifrån storleken på kartan - i
                if (x != -1) {
                    if (playField[y][x].getStatus ().equals ( player )) {
                        inARow.add ( new Coordinate ( y,x ) );
                    } else if (!inARow.isEmpty ()) {
                        //Get the coordinate before
                        Coordinate before = new Coordinate ( inARow.get ( 0 ).getY () - 1,inARow.get ( 0 ).getX ()+1 );
                        //Get the coordinate after
                        Coordinate after = new Coordinate ( inARow.get ( inARow.size () - 1 ).getY () + 1,inARow.get ( inARow.size () - 1 ).getX () );
                        score += calculateScore ( inARow,before,after );
                        inARow.clear ();
                    }
                    //playField[y][x].setStatus ( Node.Brick.THISNODE );
                    y++;
                    x--;
                    count++;
                }
            }
            y = 0;
            if (count < ROW_TO_WIN) {
                break;
            }
            count = 0;
        }
        return score;
    }

    private int leftTopToRightBottomAndLeft (String player) {
        int x = PLAYFIELDSIZE - 1;
        int y = 0;
        int score = 0;
        int count = 0;
        int topScore = 0;
        List<Coordinate> inARow = new LinkedList<> ();

        for (int i = 0; i <= PLAYFIELDSIZE - 1; i++) {   //Yvärdet börjar ifrån 0 och går neråt på kartan
            x = i;  //TODO KOLLA OM DENNA BEHÖVS
            for (int j = 0; j < PLAYFIELDSIZE; j++) { //Xvärdet börjar ifrån storleken på kartan - i
                if (x < PLAYFIELDSIZE) {
                    if (playField[y][x].getStatus ().equals ( player )) {
                        inARow.add ( new Coordinate ( y,x ) );
                    } else if (!inARow.isEmpty ()) {
                        //Get the coordinate before
                        Coordinate before = new Coordinate ( inARow.get ( 0 ).getY () - 1,inARow.get ( 0 ).getX ()+1 );
                        //Get the coordinate after
                        Coordinate after = new Coordinate ( inARow.get ( inARow.size () - 1 ).getY () + 1,inARow.get ( inARow.size () - 1 ).getX ()+1 );
                        score += calculateScore ( inARow,before,after );
                        inARow.clear ();
                    }
                    y++;
                    x++; //TODO KOLLA OM DENNA BEHÖVS
                    count++;
                } else break;
            }
            y = 0;
            if (count < ROW_TO_WIN) {
                break;
            }
            count = 0;
        }
        return score;
    }

    private int leftTopToRightBottomAndRight (String player) {
        int x = 0;
        int y = 0;
        int score = 0;
        int count = 0;
        int topScore = 0;
        List<Coordinate> inARow = new LinkedList<> ();

        for (int i = 0; i <= PLAYFIELDSIZE - 1; i++) {   //Yvärdet börjar ifrån 0 och går neråt på kartan
            y = i;  //TODO KOLLA OM DENNA BEHÖVS
            for (int j = 0; j < PLAYFIELDSIZE; j++) { //Xvärdet börjar ifrån storleken på kartan - i
                if (y < PLAYFIELDSIZE) {
                    if (playField[y][x].getStatus ().equals ( player )) {
                        inARow.add ( new Coordinate ( y,x ) );
                    } else if (!inARow.isEmpty ()) {
                        //Get the coordinate before
                        Coordinate before = new Coordinate ( inARow.get ( 0 ).getY () - 1,inARow.get ( 0 ).getX ()+1 );
                        //Get the coordinate after
                        Coordinate after = new Coordinate ( inARow.get ( inARow.size () - 1 ).getY () + 1,inARow.get ( inARow.size () - 1 ).getX ()+1 );
                        score += calculateScore ( inARow,before,after );
                        inARow.clear ();
                    }
                    y++;
                    x++; //TODO KOLLA OM DENNA BEHÖVS
                    count++;
                } else break;
            }
            x = 0;
            if (count < ROW_TO_WIN) {
                break;
            }
            count = 0;
        }
        return score;
    }

    private ArrayList<Coordinate> possiblePlaysLimited () {
        final String NOTPLAYED = Node.Brick.NOTPLAYED.value;
        ArrayList<Coordinate> possiblePlays = new ArrayList<> ();
        for (int i = 1; i < PLAYFIELDSIZE - 1; i++) {
            for (int j = 1; j < PLAYFIELDSIZE - 1; j++) {
                if (playField[i][j].getStatus ().equals ( NOTPLAYED ) && hasNeighbor ( i,j )) {
                    possiblePlays.add ( new Coordinate ( i,j ) );
                }
            }
        }
        return possiblePlays;
    }

    private boolean hasNeighbor (int i,int j) {
        return !playField[i - 1][j].getStatus ().equals ( Node.Brick.NOTPLAYED.value ) ||
                !playField[i + 1][j].getStatus ().equals ( Node.Brick.NOTPLAYED.value ) ||
                !playField[i][j - 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value ) ||
                !playField[i][j + 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value ) ||
                !playField[i + 1][j + 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value ) ||
                !playField[i - 1][j - 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value ) ||
                !playField[i + 1][j - 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value ) ||
                !playField[i - 1][j + 1].getStatus ().equals ( Node.Brick.NOTPLAYED.value );
    }


    /**
     * @param y
     * @param x
     * @return
     */
    private boolean isAccepted (Node.Brick player,int y,int x) {
        if (playField[y][x].toString ().equals ( "|" + Node.Brick.NOTPLAYED + "|" )) {
            playField[y][x].setStatus ( player );
            playedPositions.add ( new Coordinate ( y,x ) );
            return true;
        }
        return false;
    }

    private void unPlace (Coordinate position) {
        playField[position.getY ()][position.getX ()].setStatus ( Node.Brick.NOTPLAYED );
        playedPositions.remove ( position );
    }


    /**
     * Flips to the next persons turn
     */
    private void switchPlayer () {
        if (playersTurn) playersTurn = false;
        else playersTurn = true;
    }

    /**
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

        for (int i = 0; i < ROW_TO_WIN; i++) {
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

            if (xPos == ROW_TO_WIN || xMin == ROW_TO_WIN || yPos == ROW_TO_WIN || yMin == ROW_TO_WIN || rightUp == ROW_TO_WIN || rightDown == ROW_TO_WIN || leftDown == ROW_TO_WIN || leftUp == ROW_TO_WIN) {
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
        for (int i = 0; i < PLAYFIELDSIZE; i++) {
            if (i == 0) {
                x = "t|" + i + "||";
            } else
                x = i + "||";
            if (i == PLAYFIELDSIZE - 1) {
                x += "\n";
            }
            System.out.print ( x );
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
}
