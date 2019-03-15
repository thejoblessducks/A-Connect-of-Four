import java.util.LinkedList;
class MinMaxNoPrun{
    private int max_depth_pru;//Defines the maximum depth of search
    //The higher the depth the higher the difficulty
    private char player;

    public MinMaxNoPrun(int m_depth,char player){
        this.max_depth_pru=m_depth;
        this.player=player;
    }

    public int getMaxDepth(){return max_depth_pru;}
    public char getPlayer(){return player;}

    public void setMaxDepth(int d){this.max_depth_pru=d;}
    public void setPlayer(char p){this.player=p;}

    public Play minMax(Table tb){
        if(tb.getPlayer()=='X')//Maximize
            return max(new Table(tb),0);
        else//Minimize
            return min(new Table(tb),0);
    }
    public Play max(Table tb,int depth){
        if(tb.isGameOver() || depth==max_depth_pru){
            Play p = new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());
            return p;
        }
        Play maxPlay = new Play(Integer.MIN_VALUE);
        for(Table son : tb.getDescendents('X')){
            Play p = min(son,depth+1);
            if(p.getUtility()>maxPlay.getUtility()){
                maxPlay.setRow(son.getPlay().getRow());
                maxPlay.setCol(son.getPlay().getCol());
                maxPlay.setUtility(son.getPlay().getUtility());
            }
            else if(p.getUtility()==maxPlay.getUtility()){
                maxPlay.setRow(son.getPlay().getRow());
                maxPlay.setCol(son.getPlay().getCol());
                maxPlay.setUtility(son.getPlay().getUtility());
            }
        }
        return maxPlay;
    }
    public Play min(Table tb,int depth){
        if(tb.isGameOver() || depth==max_depth_pru){
            Play p = new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());
            return p;
        }
        Play minPlay = new Play(Integer.MAX_VALUE);
        for(Table son : tb.getDescendents('O')){
            Play p = max(son,depth+1);
            if(p.getUtility()<minPlay.getUtility()){
                minPlay.setRow(son.getPlay().getRow());
                minPlay.setCol(son.getPlay().getCol());
                minPlay.setUtility(son.getPlay().getUtility());
            }
            else if(p.getUtility()==minPlay.getUtility()){
                minPlay.setRow(son.getPlay().getRow());
                minPlay.setCol(son.getPlay().getCol());
                minPlay.setUtility(son.getPlay().getUtility());
            }
        }
        return minPlay;
    }
}
