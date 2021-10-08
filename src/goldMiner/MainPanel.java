package goldMiner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainPanel extends JPanel {
    private BufferedImage background;

    public MainPanel(){
        this.setLayout(null);

        //缓冲图片
        try {
            background = ImageIO.read(getClass().getResource("splash.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void initButton(JButton soloButton, JButton doubleButton){
        soloButton.setBounds(150, 150, 250, 85);
        doubleButton.setBounds(150, 300, 250, 85);
        ImageIcon soloImage = new ImageIcon(getClass().getResource("solo.png"));
        soloButton.setIcon(soloImage);
        ImageIcon doubleImage = new ImageIcon(getClass().getResource("double.png"));
        doubleButton.setIcon(doubleImage);
        this.add(soloButton);
        this.add(doubleButton);
    }

    public void paint(Graphics g){
        super.paint(g);
        // 画背景图
        g.drawImage(background, 0, 0, Main.getWeight(), Main.getHeight(), null);
    }

}

