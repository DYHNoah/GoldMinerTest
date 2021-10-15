package goldMiner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static int weight = 1200;
    private static int height = 800;

    public static void setHeight(int height) {
        Main.height = height;
    }

    public static void setWeight(int weight) {
        Main.weight = weight;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWeight() {
        return weight;
    }

    public static void main(String[] args) {

        JFrame jf = new JFrame();
        jf.setBounds(300, 100, Main.weight, Main.height);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton soloButton = new JButton();
        JButton doubleButton = new JButton();

        MainPanel mp = new MainPanel();
        mp.initButton(soloButton, doubleButton);

        jf.setContentPane(mp);
        jf.setVisible(true);

        soloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                new SoloMode().start();
            }
        });
        doubleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                new DoubleMode().start();
            }
        });




    }
}

