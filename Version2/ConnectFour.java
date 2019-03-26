import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ConnectFour{
    /*For this version, player X is the first to start, AI is O
      We must define difficulty of MinMax depth, the higher the harder
      We give choice of MinMax with no pruning/with pruning or MCTS
    */

/*------------------------Auxiliar Functions----------------------------------*/
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


/*------------------------------------------------------------------------------
                            MinMax no Prun
------------------------------------------------------------------------------*/
    public static void minMaxNoPrun(Scanner in){
        int difficulty,player_column=0,player=0;

        clearScreen();
            System.out.println("*******MinMax No Prun*******");
            System.out.print("Depth/Difficulty: ");
            difficulty=in.nextInt();

        MinMaxNoPrun AI = new MinMaxNoPrun(difficulty,'O');
        Table game = new Table();

        clearScreen();
            System.out.println("*******MinMax No Prun*******");
            System.out.println("Mode:\n1)Player 1->You\n2)Player 1->AI\n3)Exit");
            player=in.nextInt();
        switch(player){
            case 1://X is Human AI is O, X play first
                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.println("Player 1 is Human");
                    game.setPlayer('O');//defines previous player as O so that X can start first
                wait(1000);
                clearScreen();
            break;
            case 2: //O is AI Human is X, O plays first
                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.println("Player 1 is AI");
                    game.setPlayer('X');//defines previous player as X so that O(AI) can start first
                wait(1000);
                clearScreen();
            break;
            case 3: System.exit(0); break;
            default:
                System.out.println("Wrong choice");
                wait(1000);
                minMaxNoPrun(in);
            break;
        }
        System.out.println("*******MinMax No Prun********");
        System.out.println("Depth/Difficulty: "+difficulty+"\n");
        if(player==1){
            System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
            System.out.println("-----------------------------");
            System.out.println(game);
        }
        int i=1; //counter for number of plays
        while(!game.isGameOver()){
            System.out.println("\n*****************************");
            System.out.print("Play: "+i);
            switch(game.getPlayer()){//retrieves the last player
                case 'X'://AI to move
                    System.out.println("  O turn (AI)");
                    game.setPlayer('O');
                    Play AI_Play=AI.minMax(game);
                    game.makeNewPlay(AI_Play.getCol(),'O');
                break;
                case 'O'://Player to play
                    System.out.println("  X turn (Human)");
                    System.out.print("Your move ");
                    try{
                        do{
                            System.out.print("(Column 1-7): ");
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
            System.out.println("-----------------------------");
            System.out.println(game);
            i++;
        }

        //Game has Ended with win/lose or draw for player
        if(game.getChampion()=='X') System.out.println(ConsoleColors.GREEN_BOLD+"You (X) WIN!!!"+ConsoleColors.RESET);
        else if(game.getChampion()=='O') System.out.println(ConsoleColors.RED_BOLD+"You lose :("+ConsoleColors.RESET);
        else System.out.println(ConsoleColors.WHITE_BOLD+"It's a draw"+ConsoleColors.RESET);
        System.out.println("Game Over");
        System.exit(0);
    }


/*------------------------------------------------------------------------------
                    MinMax Alpha-Beta Prun
------------------------------------------------------------------------------*/
    public static void minMaxAlphBeta(Scanner in){
        int difficulty,player_column=0,player=0;

        clearScreen();
            System.out.println("*******MinMax No Prun*******");
            System.out.print("Depth/Difficulty: ");
            difficulty=in.nextInt();

        MinMaxAlphaBetaPrun AI = new MinMaxAlphaBetaPrun(difficulty,'O');
        Table game = new Table();

        clearScreen();
            System.out.println("*******MinMax No Prun*******");
            System.out.println("Mode:\n1)Player 1->You\n2)Player 1->AI\n3)Exit");
            player=in.nextInt();
        switch(player){
            case 1://X is Human AI is O, X play first
                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.println("Player 1 is Human");
                    game.setPlayer('O');//defines previous player as O so that X can start first
                wait(1000);
                clearScreen();
            break;
            case 2: //O is AI Human is X, O plays first
                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.println("Player 1 is AI");
                    game.setPlayer('X');//defines previous player as X so that O(AI) can start first
                wait(1000);
                clearScreen();
            break;
            case 3: System.exit(0); break;
            default:
                System.out.println("Wrong choice");
                wait(1000);
                minMaxNoPrun(in);
            break;
        }
        System.out.println("*******MinMax No Prun********");
        System.out.println("Depth/Difficulty: "+difficulty+"\n");
        if(player==1){
            System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
            System.out.println("-----------------------------");
            System.out.println(game);
        }
        int i=1; //counter for number of plays
        while(!game.isGameOver()){
            System.out.println("\n*****************************");
            System.out.print("Play: "+i);
            switch(game.getPlayer()){//retrieves the last player
                case 'X'://AI to move
                    System.out.println("  O turn (AI)");
                    game.setPlayer('O');
                    Play AI_Play=AI.alphaBeta(game);
                    game.makeNewPlay(AI_Play.getCol(),'O');
                break;
                case 'O'://Player to play
                    System.out.println("  X turn (Human)");
                    System.out.print("Your move ");
                    try{
                        do{
                            System.out.print("(Column 1-7): ");
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
            System.out.println("-----------------------------");
            System.out.println(game);
            i++;
        }

        //Game has Ended with win/lose or draw for player
        if(game.getChampion()=='X') System.out.println(ConsoleColors.GREEN_BOLD+"You (X) WIN!!!"+ConsoleColors.RESET);
        else if(game.getChampion()=='O') System.out.println(ConsoleColors.RED_BOLD+"You lose :("+ConsoleColors.RESET);
        else System.out.println(ConsoleColors.WHITE_BOLD+"It's a draw"+ConsoleColors.RESET);
        System.out.println("Game Over");
        System.exit(0);
    }



/*------------------------------------------------------------------------------
                    Monte Carlo MonteCarloTreeSearch
------------------------------------------------------------------------------*/
    public static void monteCarlo(Scanner in){
        int player_column=0,player=0;
        long difficulty;

        clearScreen();
            System.out.println("*******MinMax No Prun*******");
            System.out.print("Depth/Difficulty (100-1000000): ");
            difficulty=/*TimeUnit.SECONDS.toNanos(*/in.nextInt();

        Table game = new Table();
        MonteCarloTreeSearch AI = new MonteCarloTreeSearch(difficulty);

        clearScreen();
            System.out.println("*******MinMax No Prun*******");
            System.out.println("Mode:\n1)Player 1->You\n2)Player 1->AI\n3)Exit");
            player=in.nextInt();
        switch(player){
            case 1://X is Human AI is O, X play first
                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.println("Player 1 is Human");
                    game.setPlayer('O');//defines previous player as O so that X can start first
                wait(1000);
                clearScreen();
            break;
            case 2: //O is AI Human is X, O plays first
                clearScreen();
                    System.out.println("*******MinMax No Prun*******");
                    System.out.println("Player 1 is AI");
                    game.setPlayer('X');//defines previous player as X so that O(AI) can start first
                wait(1000);
                clearScreen();
            break;
            case 3: System.exit(0); break;
            default:
                System.out.println("Wrong choice");
                wait(1000);
                minMaxNoPrun(in);
            break;
        }
        System.out.println("*******MinMax No Prun********");
        System.out.println("Difficulty: "+difficulty+"\n");
        if(player==1){
            System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
            System.out.println("-----------------------------");
            System.out.println(game);
        }
        int i=1; //counter for number of plays
        while(!game.isGameOver()){
            System.out.println("\n*****************************");
            System.out.print("Play: "+i);
            switch(game.getPlayer()){//retrieves the last player
                case 'X'://AI to move
                    System.out.println("  O turn (AI)");
                    /*game.setPlayer('O');
                    AI.root.table=new Table(game);
                    */
                    int AI_Play=AI.MCTS(game);
                    game.makeNewPlay(AI_Play,'O');
                break;
                case 'O'://Player to play
                    System.out.println("  X turn (Human)");
                    System.out.print("Your move ");
                    try{
                        do{
                            System.out.print("(Column 1-7): ");
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
            System.out.println("-----------------------------");
            System.out.println(game);
            i++;
        }

        //Game has Ended with win/lose or draw for player
        if(game.getChampion()=='X') System.out.println(ConsoleColors.GREEN_BOLD+"You (X) WIN!!!"+ConsoleColors.RESET);
        else if(game.getChampion()=='O') System.out.println(ConsoleColors.RED_BOLD+"You lose :("+ConsoleColors.RESET);
        else System.out.println(ConsoleColors.WHITE_BOLD+"It's a draw"+ConsoleColors.RESET);
        System.out.println("Game Over");
        System.exit(0);
    }

/*------------------------------------------------------------------------------
                                Main
------------------------------------------------------------------------------*/

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
            case 3:
                monteCarlo(in);
            break;
            case 4: System.exit(0);
            //Will only be implementi 1 for now
            default: System.out.println("Invalid choice"); break;
        }
    }
}
