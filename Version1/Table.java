
class Table{
    private char[][] table = new char[7][6];
    private char player;
    private int points;

    public int getPoints(){return points;}
    private void setPoints(int p){points=p; return;}
    public char getPlayer(){
        return player;
    }
    public boolean isWin(){
        if(getPlayer()=='X')points+=16;
        else points-=16;
        return isWinHorizontal() || isWinVertical();
    }
        private boolean isWinHorizontal(){
            int ply=0,x=0,o=0;
            for(int i=0;i<6;i++){
                for(int j=0;j<7;j++){
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
            for(int i=0;i<4;i++){
                for(int j=0;j<3;i++){
                    
                }
            }
        }
        private boolean isWinDiagonalLeft(){

        }
}
