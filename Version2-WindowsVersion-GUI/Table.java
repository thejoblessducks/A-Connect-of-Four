import java.util.LinkedList;

/*------------------------------------------------------------------------------
Color Pick
------------------------------------------------------------------------------*/
class ConsoleColors {
    /*
     * This Class has some instructions designed to change the linux(ubuntu) 
     *      character colors, it won't work on Windows and we can't guarantee
     *      that it will work on other Linux OS
     * Note that it is important to RESET the colour of the terminal characters
     *      otherwise, you will set all chars to that color
     * For our class Table we will mostly use RED(for AI), GREEN_BOLD(for Human)
     *      and WHITE(for draw) 
     */
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
}


/*------------------------------------------------------------------------------
Play-Has the Row/Col of a play + its utility value
------------------------------------------------------------------------------*/
class Play{
    /*
     * This class was designed to store the main values of a play, independently
     *      of the class Table, as to enable the backtracking process
     * It is composed of 3 Integer values:row,col and utility, the row will
     *      store the row in the class Table that was changed for this move, col
     *      will store the col in the class Table that was changed in this move
     *      and utility will serve as a storage for the utility of a TERMINAL 
     *      state, therefore, it will only have a real value(!=-1) when, in our
     *      search method we either reach a terminal state(game over) or a 
     *      resource cap(we will establish a node cap, but a time cap is equally
     *      effective)
     * In order to fully use this class we will implement 3 constructors, each
     *      with its own purpose, the first will take all 3 values, for when
     *      we want to update the table as well as the utility, the second will
     *      take only the row and col of the move that was made either by Human
     *      or AI, this constructor will be used mostly during the expansion
     *      phases of the algorithms, the last will only take the utility, 
     *      and thus, will only be used for backtracking information
     */
    private int row,col;//row and col where play was made
    private int utility;//utility, will change in minmax backtrack

    //Constructor
        public Play(){
            //Empty play, when start game
            this.row=this.col=-1;
            utility=0;
        }
        public Play(int row,int col,int utility){
            //make move from know utility
            this.row=row; this.col=col; this.utility=utility;
        }
        public Play(int row,int col){
            //make move when expanding minmax or for player
            this.row=row;this.col=col; this.utility=-1;
        }
        public Play(int utility){
            //make move in backtrack, to save utility
            this.row=this.col=-1; this.utility=utility;
        }
    //Getters
        public int getRow(){return row;}
        public int getCol(){return col;}
        public int getUtility(){return utility;}
    //Setters
        public void setRow(int r){this.row=r;}
        public void setCol(int c){this.col=c;}
        public void setUtility(int u){this.utility=u;}
}


/*------------------------------------------------------------------------------
Table-Represents the Game Configuration
------------------------------------------------------------------------------*/
public class Table{
    /*
     * This class was designed to represent the game, therefore it must contain
     *      vital information about the game state that it represents, such as
     *      its configuration and the player that last played, arriving at this
     *      configuration.
     * It is composed of 6 variables:
     *      ->a variable play, that stores the play that produced this 
     *          configuration;
     *      ->a variable player, that stores the player that made the last play;
     *      ->a variables champion, that will only be changed in terminal state,
     *          and will store the player that managed to win the game in a
     *          certain branch of search
     *      ->a matrix/table of char, with the purpose of storing the physical
     *          positions of each cell(a cell can be empt,'-', a cell of Human
     *          player,'X' or a cell of AI player,'O');
     *      ->a variable gameover, that returns true if we finished the game
     *      ->a variable out_of_bonds, will be true if player tries to access,
     *          an invalid position, this will come in handy during in the
     *          Version1 of this game
     * We then implement 2 types of constructors, one that has no parameters,
     *      and so, is the initiating state, and a second constructor that takes
     *      an existing copy and independently clones it, in order for us to
     *      operate on it without affecting the previous one.
     * 
     * As for functions, we have the standart getters and setters, and then have
     *      11 functions:
     *      ->A toString method, to print the colored table;
     *      ->A getLastFreeRowPosition that, provided a column c, it searches 
     *          that sepecific column in the table untill it finds the last free
     *          cell, counting from the bottom;
     *      ->A isColumnFull that, given a column c, it will check it thet is
     *          no free cell in that column, for that it will only have to see if
     *          the first row in that column if equal or not to the empty cell
     *          when we use the getLastFreePosition, we will alwas invoke this
     *          method first, in order to protect the program;
     *      ->A HasWin method, that will transverse the table searching for any
     *          consecutive non empty equal cells, when it finds 4 in any line 
     *          it sets the champion as the player that as those cells
     *      ->A isGameOver method, that applies the HasWin method, it gets any
     *          champion returns true for one player managed to make 4 in line
     *          if has no win, then it will transverse the table in search of an 
     *          empty cell, for if there are no winners but there is at least 
     *          one empty cell, then there is one more play to be made, if all
     *          fails, then there are no winners and no more possible plays, as 
     *          such the game is over;
     *      ->An Utility function, designed to attest the value of a state, if 
     *          the state is terminal it returns +-512 depending on the winning
     *          player, if it's not a terminal state, then it evaluates the 
     *          table based on the following rules (note that the utility will 
     *          give positive values for the AI player,'O', and negative for the
     *          Human player,'X'):
     *              »+50 if AI has 3 cells and Human none;
     *              »+10 if AI has 2 cells and Human none;
     *              »+1 if AI has 1 cell and Human none;
     *              »the symmetric values for the opposite conditions;
     *          This utility function is composed of 4 child functions, each for
     *          the respective direction;
     *      ->A getDescendents method that generates a list of all the table 
     *          states, produced by application of an action in the parent table
     *          (an action as a limit of 6, where each value,0-6, is the column
     *          where a new cell will be placed);
     *      ->A makePlay method, that given a player, and a desired column, will
     *          apply the move to the table and update the player to the given one
     * 
     */
    private Play play;             //saves the choice of move to reach this play
    private char player;           //saves the previous player
    private char champion;         //saves the winning player
    private char[][] table;
    private boolean game_over;
    private boolean out_of_bounds=false;

    private String str; //for MTCS

    //Constructor
        public Table(){//constructor for starting game
            this.play=new Play();        //empty move
            this.player='-';             //no player
            this.champion='-';           //no champion
            this.table=new char[6][7];
            this.str="";
            for(int i=0;i<6;i++){   //Form empty table
                for(int j=0;j<7;j++){
                    this.table[i][j]='-';
                    str+='-';
                }
            }
        }
        public Table(Table copy){//Copy tables
            this.play=copy.getPlay();
            this.player=copy.getPlayer();
            this.champion=copy.getChampion();
            this.table=copy.getTable();
            this.str=copy.getString();
        }
    //Getters
        public String getString(){
            return this.str;
        }
        public Play getPlay(){
            //Last play, to reach conf
            return play;
        }
        public char getPlayer(){
            //Player that made the last play
            return player;
        }
        public char getChampion(){
            //The player that won in this branch
            return champion;
        }
        public char[][] getTable(){
            char t[][]=new char[6][7];
            for(int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    t[i][j]=table[i][j];
                }
            }
            return t;
        }
        public boolean isOutOfBounds(){
            return out_of_bounds;
        }
        public int getLastFreeRowPosition(int col){
            //Finds the last free position in one table, the top of the collum
            int r=-1;
            for(int i=0;i<6;i++){
                if(table[i][col]=='-') r=i;
            }
            return r;
        }
        public boolean isColumnFull(int col){
            //if a column is full, the row 0 in the col is != -
            return !(table[0][col]=='-');
        }
        public boolean gameOver(){return game_over;}
    //Setters
        public void setPlay(Play new_play){
            this.play.setRow(new_play.getRow());
            this.play.setCol(new_play.getCol());
            this.play.setUtility(new_play.getUtility());
        }
        public void setPlayer(char p){
            this.player=p;
        }
        public void setOutOfBounds(boolean ob){this.out_of_bounds=ob;}
        public void setTable(char[][] table){
            this.str="";
            for(int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    this.table[i][j]=table[i][j];
                    this.str+=table[i][j];
                }
            }
        }
        public void setChampion(char c){
            champion=c;
        }
        public void setGameOver(boolean go){this.game_over=go;}
    //Functions
        //Plays
        public void makeNewPlay(int col,char player){
            /*
             * Provided a column and the player letter, makes the moves
             * To do so, we update the last player to make move/play
             * We make a Play where we define the move as the column and the
             *    last free row in that column
             */
            //we use try-catch to protect method in case player enters invalid column
            try{
                this.setPlayer(player);
                //searches the table in column for free Row
                this.setPlay(new Play(getLastFreeRowPosition(col),col));
                this.table[getLastFreeRowPosition(col)][col]=player;
            }catch(ArrayIndexOutOfBoundsException e){
                //Move to make is not possible, either column is full os is out of table
                this.setOutOfBounds(true);
            }
        }
        public LinkedList<Table> getDescendents(char player){
            //Given a player(required for generalization of MinMax) we generate the possible moves
            //For any table/game state we only have a maximum of 7 possible moves (one for each column)
            LinkedList<Table> descendents = new LinkedList<>();
            for(int i=0;i<7;i++){
                if(!isColumnFull(i)){
                    //There is at least one free position in this board
                    //As such we will now find the position, by making a new play to that column
                    Table son=new Table(); //copies table
                    son.table=this.getTable();
                    son.makeNewPlay(i,player);
                    son.str="";
                    //Update string
                    for(int j=0;j<6;j++){
                        for(int k=0;k<7;k++){
                            son.str+=son.table[j][k];
                        }
                    }
                    if(!son.isOutOfBounds())
                        descendents.addFirst(son);
                }
            }
            return descendents;
        }
        //Check game state
        public boolean hasWin(){
            //will search for the whole game to see if there is a connected four
            //Check Horizontally
                for(int i=0;i<6;i++){//r
                    for(int j=0;j<4;j++){//c
                        if(table[i][j]!='-'){
                            if(table[i][j]==table[i][j+1]&&table[i][j]==table[i][j+2]&&table[i][j]==table[i][j+3]){
                                setChampion(table[i][j]); return true;
                            }
                        }
                    }
                }
            //Check Vertically
                for(int j=0;j<7;j++){//col
                    for(int i=0;i<3;i++){//col
                        if(table[i][j]!='-'){
                            if(table[i][j]==table[i+1][j]&&table[i][j]==table[i+2][j]&&table[i][j]==table[i+3][j]){
                                setChampion(table[i][j]); return true;
                            }
                        }
                    }
                }
            /*Check Diagonal Right
                x
                 x
                  x
                   x
            */
                for(int i=0;i<3;i++){
                    for(int j=0;j<4;j++){
                        if(table[i][j]!='-'){
                            if(table[i][j]==table[i+1][j+1]&&table[i][j]==table[i+2][j+2]&&table[i][j]==table[i+3][j+3]){
                                setChampion(table[i][j]); return true;
                            }
                        }
                    }
                }
            /*Check Diagonal Left
                   x
                  x
                 x
                x
            */
                for(int i=3;i<6;i++){
                    for(int j=0;j<4;j++){
                        if(table[i][j]!='-'){
                            if(table[i][j]==table[i-1][j+1]&&table[i][j]==table[i-2][j+2]&&table[i][j]==table[i-3][j+3]){
                                setChampion(table[i][j]); return true;
                            }
                        }
                    }
                }
            setChampion('-');
            return false;
        }
        public boolean isGameOver(){
            //Game is over if X wins or O wins or there is no more possible moves=>draw
            if(hasWin())//Either
                return true;
            for(int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    if(table[i][j]=='-')//There is at least one more move to go
                        return false;
                }
            }
            //No one has won and there is no more Plays=> Draw
            return true;
        }
        //Calculate utility
        public int utility(){
            int u=0;
            /*if(getPlayer()=='X') points+=16;
            else if(getPlayer()=='O')points-=16;*/
            if(hasWin()){
                if(getChampion()=='X')
                    return -512;
                else if(getChampion()=='O')
                    return 512;
            }
            if(isGameOver()) return 0;
            u+=utilHorizontal();
            u+=utilVertical();
            u+=utilDiagonalRight();
            u+=utilDiagonalLeft();

            if(this.getPlayer()=='X') u-=16;
            else if(this.getPlayer()=='O')u+=16;
            return u;
        }
        public int utilHorizontal(){
            int u=0;
            int x=0,o=0;
            for(int i=0;i<6;i++){//r
                for(int j=0;j<4;j++){//c
                    x=0;o=0;
                    for(int k=0;k<4;k++){
                        if(table[i][j+k]=='X')
                            x++;
                        else if(table[i][j+k]=='O')
                            o++;
                    }
                    if(o==3 && x==0) u+=50;
                    else if(o==2 && x==0) u+=10;
                    else if(o==1 && x==0) u+=1;
                    else if(o==0 && x==1) u-=1;
                    else if(o==0 && x==2) u-=10;
                    else if(o==0 && x==3) u-=50;
                }
            }
            return u;
        }
        public int utilVertical(){
            int u=0;
            int x=0,o=0;
            for(int j=0;j<7;j++){//col
                for(int i=0;i<3;i++){//row
                    x=0;o=0;
                    for(int k=0;k<4;k++){
                        if(table[i+k][j]=='X')
                            x++;
                        else if(table[i+k][j]=='O')
                            o++;
                    }
                    if(o==3 && x==0) u+=50;
                    else if(o==2 && x==0) u+=10;
                    else if(o==1 && x==0) u+=1;
                    else if(o==0 && x==1) u-=1;
                    else if(o==0 && x==2) u-=10;
                    else if(o==0 && x==3) u-=50;
                }
            }
            return u;
        }
        public int utilDiagonalRight(){
            int u=0;
            int x=0,o=0;
            for(int i=0;i<3;i++){
                for(int j=0;j<4;j++){
                    x=0;o=0;
                    for(int k=0;k<4;k++){
                        if(table[i+k][j+k]=='X')
                            x++;
                        else if(table[i+k][j+k]=='O')
                            o++;
                    }
                    if(o==3 && x==0) u+=50;
                    else if(o==2 && x==0) u+=10;
                    else if(o==1 && x==0) u+=1;
                    else if(o==0 && x==1) u-=1;
                    else if(o==0 && x==2) u-=10;
                    else if(o==0 && x==3) u-=50;
                }
            }
            return u;
        }
        public int utilDiagonalLeft(){
            int u=0;
            int x=0,o=0;
            for(int i=3;i<6;i++){
                for(int j=0;j<4;j++){
                    x=0;o=0;
                    for(int k=0;k<4;k++){
                        if(table[i-k][j+k] == 'X')
                            x++;
                        else if(table[i-k][j+k] == 'O')
                            o++;
                    }
                    if(o==3 && x==0) u+=50;
                    else if(o==2 && x==0) u+=10;
                    else if(o==1 && x==0) u+=1;
                    else if(o==0 && x==1) u-=1;
                    else if(o==0 && x==2) u-=10;
                    else if(o==0 && x==3) u-=50;
                }
            }
            return u;
        }
        //print table
        public String toString(){
            //Overide
            String s="";
            for(int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    if(j!=6){//if it's not last column
                        if(table[i][j]=='X'){//Human
                            s+="| "+ConsoleColors.GREEN_BOLD+table[i][j]+ConsoleColors.RESET+" ";
                        }
                        else if(table[i][j]=='O'){//AI
                            s+="| "+ConsoleColors.RED+table[i][j]+ConsoleColors.RESET+" ";
                        }
                        else{
                            s+="| - ";
                        }
                        //s+="| "+table[i][j]+" ";
                    }
                    else{
                        if(table[i][j]=='X'){//Human
                            s+="| "+ConsoleColors.GREEN_BOLD+table[i][j]+ConsoleColors.RESET+" |\n";
                        }
                        else if(table[i][j]=='O'){//AI
                            s+="| "+ConsoleColors.RED+table[i][j]+ConsoleColors.RESET+" |\n";
                        }
                        else{
                            s+="| - |\n";
                        }
                    }
                }
            }
            return s;
        }
}
