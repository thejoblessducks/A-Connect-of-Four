import java.util.ArrayList; 
    //to get a certain element requires O(1),
    //if it were a linked Linst it would require O(n)
import java.util.Random;
/*------------------------------------------------------------------------------
MTCS Node
------------------------------------------------------------------------------*/
class Node{
    /*
     * When we apply the monte carlo method, we will have to store the tree of
     *      expanded nodes, this class, represents a single tree node
     * A tree Node is defined by its table configuration, a "pointer" to the
     *      node that generated this node(parent), the number of times that this
     *      node was visited, the number of AI victories and finally and array
     *      containing this node's children, a child is made by application of 
     *      1 of 7 moves, all the sets of children for all the expandade nodes
     *      define the tree;
     * Note that the array of descendents might not have been entirely initialized
     */
    public Node parent;
    public Table table;
    private int num_visits;
    private int ai_wins;
    public Node[] descendents; //will store all 7 children

    public Node(Node parent, Table tb){
        /*
         * Given a node that generated tb and the table tb, we greate a new node
         *      both its wins and visits are 0 for it was just born, we allocate
         *      space for its children but, by default they are yet null 
         */
        this.parent=parent;
        this.table=tb;
        this.num_visits=0;
        this.ai_wins=0;
        this.descendents = new Node[7];
    }

    public Node getParent(){
        return this.parent;
    }
    public int getVisits(){
        return num_visits;
    }
    public double getWins(){
        //returns the ai wins in backtrack
        return ai_wins;
    }

    public int madeVisit(){
        //Made one more visit to node=>increment
        this.num_visits++;
        return this.num_visits;
    }
    public double madeWin(int res){
        //This node was involved in another win=>increment
        this.ai_wins+=res;
        return this.ai_wins;
    }
}

/*------------------------------------------------------------------------------
Monte Carlo tree Search
------------------------------------------------------------------------------*/
class MonteCarloTreeSearch{
    /*
     * The Monte Carlo Tree Search is particularly interesting, it comes from
     *      the necessity of finding better search algorithms, both the minmax
     *      algorithm and its improvement do well in a small search tree, however
     *      when we expand too much the tree, they will become too slow and
     *      consume an enormous ammount of memmory, that is where the MTCS shines
     * It also stores the tree, however, it doesn't store all the tree, only the
     *      expanded nodes, at first, it will almost always store all the root's
     *      children, however, through time, it will learn and chose/save the 
     *      best nodes, that is, the nodes more visited, to ensure that the node
     *      visitation is fair, we use the UCT value, influenced by the victories
     *      of a node and its visitations as well as its parent visitations' 
     * The MCTS algorithm is composed of 4 parts:
     *      ->Selection, were it will chose a node, based on the best UCT value,
     *          until it is not leaf
     *      ->Expansion, once a leaf node is reached in the selection phase, 
     *          the algorithm will randomly chose one of its children, thats 
     *          when a new node is added to the tree
     *      ->Simulation, after having expanded the tree, it will simulate a game
     *          from that expanded node, during this phase, it will randomly 
     *          chose a play until a win/lose or draw state is reached, during
     *          this process, no node is stored;
     *      ->BackPropagation, having simulated a whole game and reached a
     *          terminal state, it will increment all the visits of its parents
     *          untill the root, and will update the parents victories, +1 if
     *          AI won, -1 if AI lost, 0 if reached Draw;
     * All this phases are in a while loop, in order to explore the game tree, 
     *      however, if we do not impose a limit the program will never stop,
     *      as such, a resource cap must e introduced, it can be a time usage
     *      limit or a node/memmory usage, we will use the node limit, that 
     *      limit can be given by the user (between 100 and 1000000), if no
     *      value is given we will use 100 as our limit
     */
    public Node root;
    private long nodes_to_expand;   //Resource cap of nodes/memmory

    public MonteCarloTreeSearch(long nodes_to_expand){
        this.nodes_to_expand=nodes_to_expand;
    }
    public MonteCarloTreeSearch(){
        this.nodes_to_expand=100;
    }

    public int MCTS(Table tb){
        double start,end,total;
        start=System.nanoTime();
        this.root= new Node(null, tb);
        int n=1;
        while(n<=this.nodes_to_expand){
            //while we still have time
            Node node_selected = nodeSelection();
            if(node_selected==null){n++; continue;}

            Node node_expanded = nodeExpantion(node_selected); //new node created
            int result = nodeSimulation(node_expanded);
            nodeBackPropagation(node_expanded,result);
            n++;
        }
        int child_max=-1; //will store wich node produced best result;
        for(int i=0;i<7;i++){
            if(root.descendents[i] != null){
                if(child_max==-1 || root.descendents[i].getVisits()>root.descendents[child_max].getVisits()){
                    child_max=i;
                }
            }
        }
        end=System.nanoTime();
        total=((double)(end-start)/1_000_000_000.0);
        System.out.println("Nodes Explored: "+n+"; Time: "+total+" seconds;");
        return child_max;
    }

    public Node nodeSelection(){
        return nodeSelection(root);
    }
    private Node nodeSelection(Node parent){
        for(int i=0;i<7;i++){
            if(parent.descendents[i]==null && !parent.table.isColumnFull(i))
                return parent;
        }
        double max_uct=-1;
        int max_uct_index=-1;
        for(int i=0;i<7;i++){
            if(parent.table.isColumnFull(i)) continue;

            Node curr = parent.descendents[i];
            double wins;
            if(parent.table.getPlayer()=='X'){
                //next player is AI
                wins=curr.getWins();
            }
            else{
                wins=curr.getVisits()-curr.getWins();
            }
            double uct= (wins/curr.getVisits()) + Math.sqrt(2)*Math.sqrt(Math.log(parent.getVisits())/curr.getVisits());
            //vall = uct where c=sqrt(2)--wikipedia

            //choose best uct
            if(uct>max_uct){
                max_uct=uct;
                max_uct_index=i;
            }
        }
        if(max_uct_index==-1) return null; //no child
        return nodeSelection(parent.descendents[max_uct_index]);
    }

    public Node nodeExpantion(Node to_expand){
        ArrayList<Integer> to_Visit = new ArrayList<>(7);
        for(int i=0;i<7;i++){
            if(to_expand.descendents[i]==null && !to_expand.table.isColumnFull(i)){
                //Can generate child, collum free
                to_Visit.add(i);
            }
        }
        int index = to_Visit.get(new Random().nextInt(to_Visit.size()));

        char player=to_expand.table.getPlayer();
        Table copy = new Table(to_expand.table);

        if(player=='X') player='O';
        else player='X';
        copy.makeNewPlay(index,player);
        to_expand.descendents[index]= new Node(to_expand,copy);
        return to_expand.descendents[index];
    }

    public int nodeSimulation(Node node_expanded){
        Table simulator = new Table(node_expanded.table);
        int i = 0;
        char player=simulator.getPlayer();
        while(!simulator.isGameOver()){
            i= new Random().nextInt(7);
            if(simulator.isColumnFull(i)) continue;
            if(player=='X') player='O';
            else player='X';
            simulator.makeNewPlay(i,player);
        }

        if(simulator.getChampion()=='O')
            return 1;
        else if(simulator.getChampion()=='X')
            return -1;
        return 0;
        //return simulator.utility();
    }

    public void nodeBackPropagation(Node explored, int result){
        Node curr=explored;
        while(curr!=null){
            curr.madeVisit();
            curr.madeWin(result);
            curr=curr.parent;
        }
    }

    public int sizeTree(){
        return sizeTree(root,0);
    }
    public int sizeTree(Node node,int s){
        for(int i=0;i<7;i++){
            if(node.descendents[i]!=null){
                s=sizeTree(node.descendents[i],s+1);
            }
        }
        return s;
    }
}
