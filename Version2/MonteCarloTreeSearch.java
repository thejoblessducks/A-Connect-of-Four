import java.util.ArrayList; //to get a certain element requires O(1),
//if it were a linked Linst it would require O(n)
import java.util.Random;
/*------------------------------------------------------------------------------
MTCS Node
------------------------------------------------------------------------------*/
class Node{
    public Node parent;
    public Table table;
    private int num_visits;
    private double ai_wins;
    public Node[] descendents; //will store all 7 children

    public Node(Node parent, Table tb){
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
        this.num_visits++;
        return this.num_visits;
    }
    public double madeWin(double res){
        this.ai_wins+=res;
        return this.ai_wins;
    }
}

/*------------------------------------------------------------------------------
Monte Carlo tree Search
------------------------------------------------------------------------------*/
class MonteCarloTreeSearch{
    private Node root;
    private long time_to_exemine;

    public MonteCarloTreeSearch(Table tb, long time_max){
        this.time_to_exemine=time_max;
        root = new Node(null, new Table(tb));
    }

    public Play MCTS(){
        for(long time=System.nanoTime()+time_to_exemine; time>System.nanoTime(); ){
            //while we still have time
            Node node_selected = nodeSelection();
            if(node_selected==null) continue;

            Node node_expanded = nodeExpantion(node_selected);
            double result = nodeSimulation(node_expanded);
            nodeBackPropagation(node_expanded,result);
        }
        int child_max=-1; //will store wich node produced best result;
        for(int i=0;i<7;i++){
            if(root.descendents[i] != null){
                if(child_max==-1 || root.descendents[i].getVisits()>root.descendents[child_max].getVisits()){
                    child_max=1;
                }
            }
        }
        return new Play(root.table.getLastFreeRowPosition(child_max),child_max);
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

    public double nodeSimulation(Node node_expanded){
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
        return simulator.utility();
    }

    public void nodeBackPropagation(Node explored, double result){
        Node curr=explored;
        while(curr!=null){
            curr.madeVisit();
            curr.madeWin(result);
            curr=curr.parent;
        }
    }
}
