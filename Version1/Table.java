
import java.util.LinkedList;

/*------------------------------------------------------------------------------
Color Pick
------------------------------------------------------------------------------*/
class ConsoleColors {
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
