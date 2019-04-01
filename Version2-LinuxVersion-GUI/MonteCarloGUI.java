import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//Suported by http://web.mit.edu/6.005/www/sp14/psets/ps4/java-6-tutorial/components.html
public  class MonteCarloGUI extends JFrame{
    private static final JButton[][] slots = new JButton[6][7];
    //private static final JPanel panel;
    static ImageIcon human=new ImageIcon("./Imgs/GREEN.gif");
    static ImageIcon ai=new ImageIcon("./Imgs/RED.gif");
    static ImageIcon empty=new ImageIcon("./Imgs/BLACK.gif");

    private static int max_nodes=100;
    private static Table game;
    private static  MonteCarloTreeSearch AI;

    public MonteCarloGUI(){
        super("Monte Carlo Tree Search Connect4");
        game=new Table();
        openPicker(this);
    }

    public static void openPicker(MonteCarloGUI app){
        app.setVisible(false);
        JFrame picker = new JFrame("Chose Preference");
        picker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        picker.setResizable(false);
        picker.setLocationRelativeTo(null);
        picker.setSize(300,200);
        picker.setLayout(null);
        picker.setIconImage(new ImageIcon("./Imgs/connect_icon.png").getImage());
        picker.setVisible(true);

        game.setPlayer('O');
        //Add First Player Chooser
            JRadioButton ai_first,human_first;
            human_first=new JRadioButton("Human Plays First");
                human_first.setMnemonic(KeyEvent.VK_C);
                human_first.setActionCommand("Human Plays First");
                human_first.setSelected(true);
                human_first.setBounds(20,20,150,50);
            ai_first=new JRadioButton("AI Plays First");
                ai_first.setMnemonic(KeyEvent.VK_B);
                ai_first.setActionCommand("AI Plays First");
                ai_first.setBounds(20,80,115,50);
            ButtonGroup group=new ButtonGroup();
                group.add(human_first);
                group.add(ai_first);

            ActionListener sliceActionListener = new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    AbstractButton aButton = (AbstractButton) actionEvent.getSource();
                    System.out.println("Selected: " + aButton.getText());
                    if(aButton.getText().equals("AI Plays First")){game.setPlayer('X');}
                    else if(aButton.getText().equals("Human Plays First")){game.setPlayer('O');}
                }
            };
            human_first.addActionListener(sliceActionListener);
            ai_first.addActionListener(sliceActionListener);
            picker.add(ai_first);
            picker.add(human_first);
        //Add Depth Choser:
            JTextField nodes=new JTextField("Nodes",5);
                    nodes.setBounds(200,30,70,50);
                    nodes.setFont(new Font("Serif", Font.BOLD, 16));
            picker.add(nodes);
        //Add Button
            JButton ok=new JButton("OK");
                ok.setBounds(150,70,50,30);
                ok.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                           String s=nodes.getText();
                           if(!s.equals("Nodes")){
                               Scanner in=new Scanner(nodes.getText());
                               try{
                                   int i=in.nextInt();
                                   max_nodes=i;
                               }catch(Exception ex){
                                max_nodes=100;
                               }
                               in.close();
                           }else{max_nodes=100;}
                            System.out.println("Nodes: " + max_nodes);
                            picker.setVisible(false);
                            AI=new MonteCarloTreeSearch(max_nodes);
                            createMonteCarloGUI(app);
                       }
                   });
            picker.add(ok);
    }
    public static void createMonteCarloGUI(MonteCarloGUI app){
        ImageIcon img = new ImageIcon("./Imgs/connect_icon.png");
        try {app.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("./Imgs/Table.gif")))));}
        catch (IOException e) {e.printStackTrace();}
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                final int r=i;
                final int c=j;
                slots[r][c]=new JButton();
                slots[r][c].setIcon(empty);
                slots[r][c].setBounds(20+75*c,20+75*r,empty.getIconWidth(),empty.getIconHeight());
                slots[r][c].addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!game.isGameOver() && game.getPlayer()=='O'){
                            if(!game.isColumnFull(c)){
                                addMove('X', game.getLastFreeRowPosition(c), c);
                                game.makeNewPlay(c, 'X');
                                if(game.isGameOver()){
                                    if(game.getChampion()=='X'){
                                        //app.setVisible(false);
                                        JOptionPane.showMessageDialog(null,"You Won!!!","Game Over",JOptionPane.OK_OPTION);
                                        System.out.println(game);
                                        System.out.println(ConsoleColors.GREEN_BOLD+"You (X) WIN!!!"+ConsoleColors.RESET);
                                        System.exit(0);
                                    }else if(game.getChampion()=='O'){
                                        //app.setVisible(false);
                                        JOptionPane.showMessageDialog(null,"You lost :(","Game Over",JOptionPane.OK_OPTION);
                                        System.out.println(game);
                                        System.out.println(ConsoleColors.RED_BOLD+"You lose :("+ConsoleColors.RESET);
                                        System.exit(0);
                                    }
                                    else{
                                        //app.setVisible(false);
                                        JOptionPane.showMessageDialog(null,"Draw!","Game Over",JOptionPane.OK_OPTION);
                                        System.out.println(game);
                                        System.out.println(ConsoleColors.WHITE_BOLD+"It's a draw"+ConsoleColors.RESET);
                                        System.exit(0);
                                    }
                                }

                                game.setPlayer('O');
                                int AI_Play=AI.MCTS(game);
                                addMove('O', game.getLastFreeRowPosition(AI_Play), AI_Play);
                                game.makeNewPlay(AI_Play,'O');
                                if(game.isGameOver()){
                                    if(game.getChampion()=='X'){
                                        //app.setVisible(false);
                                        JOptionPane.showMessageDialog(null,"You Won!!!","Game Over",JOptionPane.OK_OPTION);
                                        System.out.println(game);
                                        System.out.println(ConsoleColors.GREEN_BOLD+"You (X) WIN!!!"+ConsoleColors.RESET);
                                        System.exit(0);
                                    }else if(game.getChampion()=='O'){
                                        //app.setVisible(false);
                                        JOptionPane.showMessageDialog(null,"You lost :(","Game Over",JOptionPane.OK_OPTION);
                                        System.out.println(game);
                                        System.out.println(ConsoleColors.RED_BOLD+"You lose :("+ConsoleColors.RESET);
                                        System.exit(0);
                                    }
                                    else{
                                        //app.setVisible(false);
                                        JOptionPane.showMessageDialog(null,"Draw!","Game Over",JOptionPane.OK_OPTION);
                                        System.out.println(game);
                                        System.out.println(ConsoleColors.WHITE_BOLD+"It's a draw"+ConsoleColors.RESET);
                                        System.exit(0);
                                    }
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(null,"Colum full","Monte Carlo Choice",JOptionPane.OK_OPTION);
                            }
                        }
                        else if(game.isGameOver()){
                            JOptionPane.showMessageDialog(null,"Game is Over","Monte Carlo Choice",JOptionPane.OK_OPTION);
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Not your Turn","Monte Carlo Choice",JOptionPane.OK_OPTION);
                            game.setPlayer('O');
                            int AI_Play=AI.MCTS(game);
                            addMove('O', game.getLastFreeRowPosition(AI_Play), AI_Play);
                            game.makeNewPlay(AI_Play,'O');
                            if(game.isGameOver()){
                                if(game.getChampion()=='X'){
                                    //app.setVisible(false);
                                    JOptionPane.showMessageDialog(null,"You Won!!!","Game Over",JOptionPane.OK_OPTION);
                                    System.out.println(game);
                                    System.out.println(ConsoleColors.GREEN_BOLD+"You (X) WIN!!!"+ConsoleColors.RESET);
                                    System.exit(0);
                                }else if(game.getChampion()=='O'){
                                    //app.setVisible(false);
                                    JOptionPane.showMessageDialog(null,"You lost :(","Game Over",JOptionPane.OK_OPTION);
                                    System.out.println(game);
                                    System.out.println(ConsoleColors.RED_BOLD+"You lose :("+ConsoleColors.RESET);
                                    System.exit(0);
                                }
                                else{
                                    //app.setVisible(false);
                                    JOptionPane.showMessageDialog(null,"Draw!","Game Over",JOptionPane.OK_OPTION);
                                    System.out.println(game);
                                    System.out.println(ConsoleColors.WHITE_BOLD+"It's a draw"+ConsoleColors.RESET);
                                    System.exit(0);
                                }
                            }
                        }
                    }
                });
                app.add(slots[i][j]);
            }
        }

        if(game.getPlayer()=='X'){
            game.setPlayer('O');
            int AI_Play=AI.MCTS(game);
            addMove('O', game.getLastFreeRowPosition(AI_Play), AI_Play);
            game.makeNewPlay(AI_Play,'O');
        }
        app.pack();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setResizable(false);
        app.setLocationRelativeTo(null); //center
        app.setSize(570,515);
        app.setLayout(null);
        app.setIconImage(img.getImage());
        app.setVisible(true);
        //initiateGame();
    }

    public static void addMove(char player,int row,int col){
        if(player=='X'){//Human
            slots[row][col].setIcon(human);
            slots[row][col].setBounds(20+75*col,20+75*row,human.getIconWidth(),human.getIconHeight());
        }
        else if(player=='O'){//AI
            slots[row][col].setIcon(ai);
            slots[row][col].setBounds(20+75*col,20+75*row,ai.getIconWidth(),ai.getIconHeight());
        }
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Couldn't Open","Connect4",JOptionPane.OK_OPTION);
        }
        new AlphaBetaGUI();
    }
}
