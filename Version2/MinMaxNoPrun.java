import java.util.LinkedList;
import java.util.Random;
class MinMaxNoPrun{
    private int max_depth_pru;//Defines the maximum depth of search
    //The higher the depth the higher the difficulty
    private char player;

    public MinMaxNoPrun(int m_depth,char player){
        //Here, player is the AI player
        this.max_depth_pru=m_depth;
        this.player=player;
    }

    public int getMaxDepth(){return max_depth_pru;}
    public char getPlayer(){return player;}

    public void setMaxDepth(int d){this.max_depth_pru=d;}
    public void setPlayer(char p){this.player=p;}

    public Play minMax(Table tb){
        if(getPlayer()=='X'){//Maximize Value of x
            Play p= max(new Table(tb),0);
            System.out.println(p.getUtility());
            return p;
        }
        else{//Minimize value of O
            Play p= min(new Table(tb),0);
            System.out.println(p.getUtility());
            return p;
        }
    }
    public Play max(Table tb,int depth){
        Random rand_play = new Random();
        if(tb.isGameOver() || depth>max_depth_pru){
            Play p = new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());
            //if(tb.isGameOver())System.out.println(p.getUtility());
            return p;
        }
        Play maxPlay = new Play(Integer.MIN_VALUE);//we nwant to maximize X value
        for(Table son : tb.getDescendents('X')){
            Play p = min(son,depth+1);
            //System.out.println(p.getUtility());
            if(p.getUtility()>maxPlay.getUtility()){
                maxPlay.setRow(son.getPlay().getRow());
                maxPlay.setCol(son.getPlay().getCol());
                maxPlay.setUtility(p.getUtility());
            }
            else if(p.getUtility()==maxPlay.getUtility()){
                if(rand_play.nextInt(2)==0){
                    maxPlay.setRow(son.getPlay().getRow());
                    maxPlay.setCol(son.getPlay().getCol());
                    maxPlay.setUtility(p.getUtility());
                }
            }
        }
        return maxPlay;
    }
    public Play min(Table tb,int depth){
        Random rand_play = new Random();

        if(tb.isGameOver() || depth==max_depth_pru){
            Play p = new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());
            return p;
        }
        Play minPlay = new Play(Integer.MAX_VALUE);
        //System.out.println(p.getUtility());
        for(Table son : tb.getDescendents('O')){
            Play p = max(son,depth+1);
            if(p.getUtility()<minPlay.getUtility()){
                minPlay.setRow(son.getPlay().getRow());
                minPlay.setCol(son.getPlay().getCol());
                minPlay.setUtility(p.getUtility());
            }
            else if(p.getUtility()==minPlay.getUtility()){
                if(rand_play.nextInt(2)==0){
                    minPlay.setRow(son.getPlay().getRow());
                    minPlay.setCol(son.getPlay().getCol());
                    minPlay.setUtility(p.getUtility());
                }
            }
        }
        return minPlay;
    }
}
