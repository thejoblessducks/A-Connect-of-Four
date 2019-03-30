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
     * 
    **/
    private int max_depth;    //the higher the depth, the harer the game will be
    private int nodes;        //Counter for nodes prunned
    private int nodes_pruned; //Counter for nodes
    //private MinMaxNoPrun minmax;

    public MinMaxAlphaBetaPrun(int max_depth){
        this.max_depth=max_depth;
        
    }
/*------------------------------------------------------------------------------
                        MinMax Alpha-Beta Prun Algorithm
------------------------------------------------------------------------------*/
//adaptation of https://www.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe_AI.html
    public Play alphaBeta(Table tb){
            nodes=0; nodes_pruned=0;
            double start,end,total;
            start=System.nanoTime();
       // Play p = max(new Table(tb),0,Integer.MIN_VALUE,Integer.MAX_VALUE);
            Play p=Alpha(new Table(tb),0,Integer.MIN_VALUE,Integer.MAX_VALUE);        
            end=System.nanoTime();
            total=((double)(end-start)/1_000_000_000.0);
            System.out.println("Nodes Pruned: "+nodes_pruned+"; Nodes :"+nodes+"; Time: "+total+" seconds;");
            return p;
    }
    public Play Alpha(Table tb,int depth, int alpha,int beta){
        if(tb.isGameOver() || depth == max_depth)
            return new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());
        
        Play maxPlay = new Play(Integer.MIN_VALUE);        
        for(Table son : tb.getDescendents('O')){
            nodes++;
            Play p= Beta(son,depth+1,alpha,beta);
            if(p.getUtility() > maxPlay.getUtility()){
                maxPlay.setRow(son.getPlay().getRow());
                maxPlay.setCol(son.getPlay().getCol());
                maxPlay.setUtility(p.getUtility());
            }

            if(alpha >= beta){nodes_pruned++; return maxPlay;}
            if(maxPlay.getUtility() > alpha) alpha=maxPlay.getUtility();
        } return maxPlay;
    }
    public Play Beta(Table tb,int depth, int alpha,int beta){
        if(tb.isGameOver() || depth == max_depth)
            return new Play(tb.getPlay().getRow(),tb.getPlay().getCol(),tb.utility());

            Play minPlay = new Play(Integer.MAX_VALUE);        
            for(Table son : tb.getDescendents('O')){
                nodes++;
                Play p= Beta(son,depth+1,alpha,beta);
                if(p.getUtility() < minPlay.getUtility()){
                    minPlay.setRow(son.getPlay().getRow());
                    minPlay.setCol(son.getPlay().getCol());
                    minPlay.setUtility(p.getUtility());
                }
    
                if(alpha >= beta){nodes_pruned++; return minPlay;}
                if(minPlay.getUtility() < beta) beta=minPlay.getUtility();
            } return minPlay;
    }
}
