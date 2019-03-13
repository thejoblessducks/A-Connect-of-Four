

/*------------------------------------------------------------------------------
Table class
------------------------------------------------------------------------------*/
class Table{
    /*  Saves the Last Move Performed (saves the row and col of move plus the winner of move)
        Saves the last Player
        Saves the winner(for minmax backtracking)
        Saves the game Board
        Saves if game has ended
    */
    public final char X='X',O='O';

    private char[][] table;
    private char last_player;
    private Move last_move;
    private boolean is_over;
    private char winner;

    //Constructor
    public Table(){//For initial table
        last_move=new Move();
        last_player='-';
        winner=0;//no one has won, it will be kept like this until reach end
        table=new char[6][7];
        for(int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                table[i][j]='-';
            }
        }
    }
    public Table(Table t){//Copy the game board
        last_move=t.getLastMove();
        last_player=t.getLastPlayer();
        winner=t.getWinner();
        table=new char[6][7];
        for(int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                table[i][j]=t.table[i][j];
            }
        }
    }

    //Functions
    public Move getLastMove(){return last_move;}
    public char getLastPlayer(){return last_player;}
    public char[][] getTable(){
        char t[][]=new char[6][7];
        for(int i=0;i<6;i++){
            for(int j=0;j<7;j++)
                t[i][j]=table[i][j];
        }
        return t;
    }
    public char getWinner(){return winner;}
    public int getLastFreeRow(int col){
        //we only check for the cols, player only puts tile in a col
        int r=-1; //if returns -1 there is no free position in row
        for(int i=0;i<6;i++){
            if(table[i][col]=='-') r=i;
        }
        return r;
    }
    public boolean isGameOver(){return is_over;}

    private void setLastMove(Move move){
        last_move.setRow(move.getRow());
        last_move.setCol(move.getCol());
        last_move.setValue(move.getValue());
        return;
    }
    private void setLastPlayer(char player){
        last_player=player;
        return;
    }
    private void setTable(char[][] t){
        for(int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                table[i][j]=t[i][j];
            }
        }
    }
    private void setWinner(char win_player){
        winner=win_player;
        return;
    }
    private void setGameOver(){
        is_over=true;
        return;
    }

    private boolean isColFull(int col){return (getLastFreeRow(col)==-1);}

    public int getUtility(){
        //if player= X, lat_move.value+=16 otherwise -=16

    }
    /*private char player;
    private int points;

    Table(char player){
        for(int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                this.table[i][j]='-';
            }
        }
        this.player=player;
        this.points=points;
    }
    Table(char[][]table,char player,int points){
        for(int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                this.table[i][j]=table[i][j];
            }
        }
        if(player=='X') this.player='O';
        else this.player='X';
        this.points=points;
    }

    public int getPoints(){return points;}
    private void setPoints(int p){points=p; return;}
    public char getPlayer(){return player;}
    public boolean isWin(){
        if(getPlayer()=='X')points+=16;
        else points-=16;
        if(isWinHorizontal() || isWinVertical() || isWinDiagonalRight() || isWinDiagonalLeft()){
            if(getPlayer()=='X')
                points=512;
            else points=-512;
            return true;
        }
        return false;
    }
        private boolean isWinHorizontal(){
            int ply=0,x=0,o=0;
            for(int i=0;i<6;i++){
                for(int j : this.table[i]){
                    if(ply==4){//tests for win
                        if(getPlayer()=='X')
                            setPoints(512);
                        else setPoints(-512);
                        return true;
                    }
                    if(table[i][j]==player) ply++;
                    if(table[i][j]=='X') x++;
                    if(table[i][j]=='O') o++;
                }
                if(o==3&&x==0)points-=50;
                else if(o==2&&x==0)points-=10;
                else if(o==1&&x==0)points-=1;
                else if(o==0&&x==1)points+=1;
                else if(x==2&&o==0)points+=10;
                else if(x==3&&0==0)points+=50;
            }
            return false;
        }
        private boolean isWinVertical(){
            int ply=0,x=0,o=0;
            for(int i=0;i<7;i++){
                for(int j=0;j<6;j++){
                    if(ply==4){//tests for win
                        if(getPlayer()=='X')
                            setPoints(512);
                        else setPoints(-512);
                        return true;
                    }
                    if(table[i][j]==player) ply++;
                    if(table[i][j]=='X') x++;
                    if(table[i][j]=='O') o++;
                }
                if(o==3&&x==0)points-=50;
                else if(o==2&&x==0)points-=10;
                else if(o==1&&x==0)points-=1;
                else if(o==0&&x==1)points+=1;
                else if(x==2&&o==0)points+=10;
                else if(x==3&&0==0)points+=50;
            }
            return false;
        }
        private boolean isWinDiagonalRight(){
            for(int i=0;i<4;i++){//col movement
                for(int j=0;j<3;j++){//line movement
                    int k=j;
                    while(k<3){
                        int ply=0,x=0,o=0;
                        for(int l=0;l<4;l++){
                            if(ply==4){//tests for win
                                if(getPlayer()=='X')
                                    setPoints(512);
                                else
                                    setPoints(-512);
                                return true;
                            }
                            if(table[j+l][i+l]==player) ply++;
                            if(table[j+l][i+l]=='X') x++;
                            if(table[j+l][i+l]=='O') o++;
                        }
                        if(o==3&&x==0)points-=50;
                        else if(o==2&&x==0)points-=10;
                        else if(o==1&&x==0)points-=1;
                        else if(o==0&&x==1)points+=1;
                        else if(x==2&&o==0)points+=10;
                        else if(x==3&&0==0)points+=50;
                        k++;
                    }
                }
            }
            return false;
        }
        private boolean isWinDiagonalLeft(){
            for(int i=6;i>2;i--){//col movement
                for(int j=0;j<3;j++){//line movement
                    int k=j;
                    while(k<3){
                        int ply=0,x=0,o=0;
                        for(int l=0;l<4;l++){
                            if(ply==4){//tests for win
                                if(getPlayer()=='X')
                                    setPoints(512);
                                else
                                    setPoints(-512);
                                return true;
                            }
                            if(table[j-l][i+l]==player) ply++;
                            if(table[j-l][i+l]=='X') x++;
                            if(table[j-l][i+l]=='O') o++;
                        }
                        if(o==3&&x==0)points-=50;
                        else if(o==2&&x==0)points-=10;
                        else if(o==1&&x==0)points-=1;
                        else if(o==0&&x==1)points+=1;
                        else if(x==2&&o==0)points+=10;
                        else if(x==3&&0==0)points+=50;
                        k++;
                    }
                }
            }
            return false;
        }
    public void printTable(){
        for(char i[] : this.table){
            for(char j : i)
                System.out.print(j);
            System.out.println();
        }
        return;
    }*/
}
