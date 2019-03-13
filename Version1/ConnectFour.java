import java.util.Scanner;

public class ConnectFour{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char[][] t=new char[7][6];
        for(int i=0;i<7;i++){
            String s=in.nextLine();
            for(int j=0;j<6;j++)
                t[i][j]=s.charAt(j);
        }
        Table tb = new Table(t,'X',0);
        System.out.print(tb);
    }
}
