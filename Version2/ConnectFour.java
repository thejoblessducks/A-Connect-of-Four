import java.util.Scanner;

public class ConnectFour{
    /*For this version, player X is the first to start, AI is O
      We must define difficulty of MinMax depth, the higher the harder
      We give choice of MinMax with no pruning/with pruning or MCTS
    */
    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void wait(int sec){
        try{
            Thread.sleep(sec);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public static void minMaxNoPrun(Scanner in){
        clearScreen();
        System.out.println("*******MinMax No Prun*******");
        System.out.println("Mode:\n1)Player 1->You\n2)Player 1->AI\n3)Exit");
        switch(in.nextInt()){
            case 1://X is Human
                int difficulty,player_column=0;

                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.println("Player 1 is Human");;
                    wait(1000);
                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.print("Depth/Difficulty: ");
                    difficulty=in.nextInt();
                clearScreen();

                System.out.println("*******MinMax No Prun*******");
                System.out.println("Dificulty "+difficulty+"\n\n");

                MinMaxNoPrun AI = new MinMaxNoPrun(difficulty,'O');
                Table game = new Table();
                System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
                System.out.println(game);
                game.setPlayer('O');//defines previous player as O so that X can start first
                while(!game.isGameOver()){
                    System.out.println("****************************");
                    switch(game.getPlayer()){//retrieves the last player
                        case 'X'://AI to move
                            game.setPlayer('O');
                            Play AI_Play=AI.minMax(game);
                            game.makeNewPlay(AI_Play.getCol(),'O');
                        break;
                        case 'O'://Player to play
                            System.out.print("Your move, ");
                            try{
                                do{
                                    System.out.print("Column 1-7: ");
                                    player_column=in.nextInt();
                                }while(game.isColumnFull(player_column-1));
                            }catch(Exception e){
                                System.out.println("Error");
                                break; //Found error, breaks switch returns to upper while, and back to case 'O'
                            }
                            game.makeNewPlay(player_column-1,'X');
                        break;
                        default: break;
                    }
                    System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
                    System.out.println(game);

                }
                //Game has Ended with win/lose or draw for player
                if(game.getChampion()=='X') System.out.println("You (X) WIN!!!");
                else if(game.getChampion()=='O') System.out.println("You lose :(");
                else System.out.println("It's a draw");
            break;
            case 2://X is AI

            break;
            case 3: System.exit(0); break;
            default:
                System.out.println("Wrong choice");
                wait(1000);
                minMaxNoPrun(in);
            break;
        }
        System.out.println("Game Over");
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
