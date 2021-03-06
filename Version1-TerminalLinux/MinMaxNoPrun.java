import java.util.LinkedList;

class MinMaxNoPrun{
    /*
     * This class is designed to apply the MinMax algorithm without prunning
     *      there is a possibility that we will never find a solution, or it
     *      will take too long, we don't want that, as such, we will, establish
     *      a resource cap, in this case, the depth os the search, the higher
     *      the depth the harder the game will be, and the longer it will take
     *      for the AI to chose
     * This class will store only the maximum depth of search as well as the
     *      nodes that were created in each AI decision
     */
    private int max_depth_pru;  //Defines the maximum depth of search
                                //The higher the depth the higher the difficulty
    private int nodes;          //counter for nodes explored

    public MinMaxNoPrun(int m_depth){
        //Here, player is the AI player
        this.max_depth_pru=m_depth;
    }

    public int getNodes(){return nodes;}
    public void setNodes(int n){nodes=n;}
/*------------------------------------------------------------------------------
                            Min Max Algorithm
------------------------------------------------------------------------------*/
//theory adaptation of https://www.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe_AI.html
    public Play minMax(Table tb){
            /*
             * The min max algorithm will call the min and max one after the
             *      other until a solution is produced.
             * Note that we start with the max, for we want to maximize the AI
             *      score and minimize the Human score
             * In this method we also count the time it takes to produce an
             *      answer as well as the number of nodes created in the search
             */
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
        }
        return maxPlay;
    }
    public Play min(Table tb,int depth){
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
        }
        return minPlay;
    }
}
