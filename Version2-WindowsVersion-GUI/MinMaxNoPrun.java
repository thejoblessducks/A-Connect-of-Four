import java.util.LinkedList;
import java.util.Random;

class MinMaxNoPrun{
    private int max_depth_pru;  //Defines the maximum depth of search
                                //The higher the depth the higher the difficulty
    private char player;
    private int nodes;          //counter for nodes explored

    public MinMaxNoPrun(int m_depth,char player){
        //Here, player is the AI player
        this.max_depth_pru=m_depth;
        this.player=player;
    }

    public int getNodes(){return nodes;}
    public void setNodes(int n){nodes=n;}
/*------------------------------------------------------------------------------
                            Min Max Algorithm
------------------------------------------------------------------------------*/
    public Play minMax(Table tb){
            nodes=0;
            double start,end,total;
            start=System.nanoTime();
        Play p= max(new Table(tb),0);
            end=System.nanoTime();
            total=((double)(end-start)/1_000_000_000.0);
            System.out.println("Nodes Explored: "+nodes+"; Time: "+total+" seconds;");
        return p;
    }
    public Play max(Table tb,int depth){
        Random rand_play = new Random();
        if(tb.isGameOver() || depth == max_depth_pru)
           return new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());

        Play maxPlay = new Play(Integer.MIN_VALUE);//we nwant to maximize X value
        for(Table son : tb.getDescendents('O')){
            nodes++;
            Play p = min(son,depth+1);
            if(p.getUtility() > maxPlay.getUtility()){
                maxPlay.setRow(son.getPlay().getRow());
                maxPlay.setCol(son.getPlay().getCol());
                maxPlay.setUtility(p.getUtility());
            }
            else if(p.getUtility() == maxPlay.getUtility()){
                //Found a move with the same utility
                //Random pick
                if(rand_play.nextInt(2) == 0){
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
        if(tb.isGameOver() || depth == max_depth_pru){
            Play p = new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());
            return p;
        }
        Play minPlay = new Play(Integer.MAX_VALUE);
        for(Table son : tb.getDescendents('X')){
            nodes++;
            Play p = max(son,depth+1);
            if(p.getUtility() < minPlay.getUtility()){
                minPlay.setRow(son.getPlay().getRow());
                minPlay.setCol(son.getPlay().getCol());
                minPlay.setUtility(p.getUtility());
            }
            else if(p.getUtility() == minPlay.getUtility()){
                //Found a move with the same utility
                //Random pick
                if(rand_play.nextInt(2) == 0){
                    minPlay.setRow(son.getPlay().getRow());
                    minPlay.setCol(son.getPlay().getCol());
                    minPlay.setUtility(p.getUtility());
                }
            }
        }
        return minPlay;
    }
}
