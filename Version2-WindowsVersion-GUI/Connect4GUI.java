import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4GUI extends JFrame{
    private JButton minmax,alphabeta,montecarlo;

    public Connect4GUI(){
        super("Connect 4");
        createPicker(this);
        createConnect(this);
    }
    private void createPicker(Connect4GUI app){
        minmax=new JButton("Min Max Algorithm");
            minmax.setBounds(45,40,220,55);
            minmax.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e) {
                    app.setVisible(false);
                    new MinMaxGUI();
                }
            });
        alphabeta=new JButton("Min Max-Alpha Beta Algorithm");
            alphabeta.setBounds(45,115,220,55);
            alphabeta.addActionListener(new ActionListener(){            
                @Override
                public void actionPerformed(ActionEvent e) {
                    app.setVisible(false);
                    new AlphaBetaGUI();
                }
            });
        montecarlo=new JButton("Monte Carlo Tree Search Algorithm");
            montecarlo.setBounds(45,190,220,55);
            montecarlo.addActionListener(new ActionListener(){            
                @Override
                public void actionPerformed(ActionEvent e) {
                    app.setVisible(false);
                    new MonteCarloGUI();
                }
            });
        add(minmax);
        add(alphabeta);
        add(montecarlo);
    }
    private static void createConnect(Connect4GUI app){
        ImageIcon img = new ImageIcon("./Imgs/connect_icon.png");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setResizable(false);
        app.setLocationRelativeTo(null); //center
        app.setSize(225,325);
        app.setLayout(null);
        app.setIconImage(img.getImage());
        app.setVisible(true);
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Couldn't Open","Connect4",JOptionPane.OK_OPTION);
        }
        new Connect4GUI();
    }
}