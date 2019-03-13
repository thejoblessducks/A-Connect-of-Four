import java.util.Scanner;

public class ConnectFour{
    /*For this version, player X is the first to start, AI is O
      We must define difficulty of MinMax depth, the higher the harder
      We give choice of MinMax with no pruning/with pruning or MCTS
    */

    public static void minMaxNoPrun(Scanner in){
        //ClearScreen
            System.out.print("\033[H\033[2J");
            System.out.flush();
        System.out.println("*******MinMax No Prun*******");
        System.out.println("Mode:\n1)Player 1->You\n2)Player 1->AI\n3)Exit");
        switch(in.nextInt()){
            case 3: System.exit(0); break;
            default:
                System.out.println("Wrong choice");
                try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}
                minMaxNoPrun(in);
            break;
        }
    }
    public static void minMaxAlphBeta(Scanner in){
        //ClearScreen
            System.out.print("\033[H\033[2J");
            System.out.flush();
        System.out.println("******MinMax Alpha-Beta******");
        System.out.println("Mode:\n1)Player 1->You\n2)Player 1->AI\n3)Exit");
        switch(in.nextInt()){
            case 3: System.exit(0); break;
            default:
                System.out.println("Wrong choice");
                try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}
                minMaxAlphBeta(in);
            break;
        }
    }
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.println("Test:");
        System.out.println("1)MinMax no pruning");
        System.out.println("2)MinMax alpha-bet pruning");
        System.out.println("3)Monte Carlo Tree Search");
        System.out.println("4)Exit");
        switch(in.nextInt()){
            case 1://Min Max No pruning
                minMaxNoPrun(in);
            break;
            case 2:
                minMaxAlphBeta(in);
            break;
            case 4: System.exit(0);
            //Will only be implementi 1 for now
            default: System.out.println("Invalid choice"); break;
        }
    }
}
