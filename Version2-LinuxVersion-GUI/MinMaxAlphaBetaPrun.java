import java.util.LinkedList;
import java.util.Random;

class MinMaxAlphaBetaPrun{
    /* This algorithm is an improvement over the minMax search algorithm
     *
     * In a normal game, if our opponent is an optimal player, some branches of
     *    our possible plays will never be reached, hence we can prun them
     * In theory, the alpha-beta prunning can optimistically reduce the time
     *    complexity of a minMax algorithm in half the depth O(b^d)==>O(b^d/2)
     * The prunning can be made in a:
     *    a) Max level if we store the maximum utility so far, as well as the
     *           smalles utility value of the state
     *    b) Min level if we store the minimum utility so far, as well as the
     *           biggest utiyility of the state
     *    That is, when in a max lvel (AI turn) if this.alpha>=parent.beta => return
     *         when in a min level (Humam turn) if this.beta <=parent.alpha => return
     *
     * To support this method we will use the MinMaxNoPrun Class
     *
     * Note the pseudocode:
     *  minMaxAlphaBeta(state):
     *      return maxAlpha(state,Intger.MIN_VALUE,Integer.MAX_VALUE)
     *  maxAlpha(state,alpha,beta):
     *      if(gameOver(state)): return utility(state)
     *      v=Integer.MIN_VALUE //v is an utility
     *      for(s in state.descendents()):
     *          play = minBeta(s,alpha,beta)
     *          if(utility(play)>v): v=utiity(play)
     *          if(v>=beta): return v
     *          if(v>alpha): alpha=v
     *      return v
     *  minBeta(state,alpha,beta):
     *      if(gameOver(state)): return utility(state)
     *      v=Integer.MAX_VALUE //v is an utility
     *      for(s in state.descendents()):
     *          play= maxBeta(s,alpha,beta)
     *          if(utility(play)<v): v=utility(play)
     *          if(v<=alpha): return v
     *          if(v<beta): beta=v
     *
     * It is important to refer that we will never call the minBeta, for we are AI
    **/
    private int max_depth;    //the higher the depth, the harer the game will be
    private char player;      //Char thet defines the AI, Standart: 'O'
    private int nodes;        //Counter for nodes prunned
    private int nodes_minmax; //Counter for nodes
    private MinMaxNoPrun minmax;

    public MinMaxAlphaBetaPrun(int max_depth,char player){
        this.max_depth=max_depth;
        this.player=player;
        minmax=new MinMaxNoPrun(max_depth, player);
    }
/*------------------------------------------------------------------------------
                        MinMax Alpha-Beta Prun Algorithm
------------------------------------------------------------------------------*/
    public Play alphaBeta(Table tb){
            nodes=0; nodes_minmax=0;
            double start,end,total;
            start=System.nanoTime();
        Play p = max(new Table(tb),0,Integer.MIN_VALUE,Integer.MAX_VALUE);
            end=System.nanoTime();
            total=((double)(end-start)/1_000_000_000.0);
            System.out.println("Nodes Pruned: "+nodes+"; Nodes MinMax:"+nodes_minmax+"; Time: "+total+" seconds;");
        return p;
    }
    public Play max(Table tb,int depth,int alpha,int beta){
        Random rand_play=new Random();
        if(tb.isGameOver() || depth == max_depth)
            return new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());

        Play maxPlay = new Play(Integer.MIN_VALUE);
        for(Table son : tb.getDescendents('O')){
            minmax.setNodes(0);
            nodes_minmax++;
            //Equal to MinMaxNoPrun
                Play p = minmax.min(son,depth+1);
                nodes_minmax+=minmax.getNodes();
                if(p.getUtility() > maxPlay.getUtility()){
                    maxPlay.setRow(son.getPlay().getRow());
                    maxPlay.setCol(son.getPlay().getCol());
                    maxPlay.setUtility(p.getUtility());
                }
                else if(p.getUtility() == maxPlay.getUtility()){
                    if(rand_play.nextInt(2)==0){
                        maxPlay.setRow(son.getPlay().getRow());
                        maxPlay.setCol(son.getPlay().getCol());
                        maxPlay.setUtility(p.getUtility());
                    }
                }
            //Prunning
            if(maxPlay.getUtility() < alpha){nodes++; return maxPlay;}
            if(alpha < maxPlay.getUtility()) alpha=maxPlay.getUtility();
        }return maxPlay;
    }
}
