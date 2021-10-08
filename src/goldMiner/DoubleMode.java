package goldMiner;

import javax.swing.*;

public class DoubleMode {
    private static int height = 800;
    private static int weight = 1200;

    public static void main(String[]  args) {
        new DoubleMode().start();
    }

    public void start(){
        // TODO Auto-generated method stub
        JFrame jf = new JFrame();
        jf.setBounds(300, 100, DoubleMode.weight, DoubleMode.height);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DoublePanel mp = new DoublePanel();
        jf.setContentPane(mp);
        jf.setVisible(true);
        mp.action();
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        DoubleMode.height = height;
    }

    public static int getWeight() {
        return weight;
    }

    public static void setWeight(int weight) {
        DoubleMode.weight = weight;
    }
}
