package goldMiner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class DoublePanel extends JPanel{
    public static final int xPosition_1 = DoubleMode.getWeight()/3;
    public static final int xPosition_2 = DoubleMode.getWeight()/3*2;
    /**
     * ����һ�����ϣ��洢���н���
     */
    static ArrayList<GoldData> goldSets = new ArrayList<GoldData>();
    /**
     * �������Լ���������ӣ��̳н��ӵ�����
     */
    GoldData mySelf_1 = new GoldData();
    GoldData mySelf_2 = new GoldData();
    //����ͼƬ
    BufferedImage goldImg;
    BufferedImage background;
    BufferedImage mySelfImg;
    BufferedImage clawImg;
    BufferedImage diamondImg;
    BufferedImage rockImg;

    // ����צ��λ��
    /**
     * ϸ�߳�ʼ�������
     */
    int clawX0_1;
    int clawX0_2;
    /**
     * ϸ�߳�ʼ��������
     */
    int clawY0_1;
    int clawY0_2;
    /**
     * ϸ�߽���������꣬��צ�Ӻ�����
     */
    double clawX_1;
    double clawX_2;
    /**
     * ϸ�߽����������꣬��צ��������
     */
    double clawY_1;
    double clawY_2;
    /**
     * ϸ����ת�ٶ�
     */
    int clawSpeed_1;
    int clawSpeed_2;
    /**
     * ϸ�������ٶ�
     */
    int stretchSpeed_1;
    int stretchSpeed_2;
    /**
     * צ���Ƿ�����ת׼��״̬��־
     */
    boolean clawSpin_1;
    boolean clawSpin_2;
    /**
     * צ���Ƿ����������־
     * true:����������
     * false:���������ջ�
     */
    boolean clawStretch_1;
    boolean clawStretch_2;
    /**
     * ϸ�ߵ�б��
     */
    double k_1;
    double k_2;
    double b_1;
    double b_2;
    /**
     * ���ջر�־
     * true: û��ץ�����������ջ�
     * false: ����״̬
     */
    boolean backclaw_1;
    boolean backclaw_2;
    // ��ť���
    JButton pauseJButton_1;
    JButton pauseJButton_2;

    /**
     * ��ץ��������
     */
    int beGrab_1;
    int beGrab_2;
    /**
     * �Ƿ�ץ������
     * true: ץ��������
     * flase: ûץ������
     */
    boolean grabSucceed_1;
    boolean grabSucceed_2;

    Setting setting = new Setting();

    //�޲ι��죬��ʼ��һЩ����
    public DoublePanel() {
        restoration(1);
        restoration(2);
        initData();
        setting.initSetting();

        //����ͼƬ
        try {
            goldImg = ImageIO.read(getClass().getResource("gold.png"));
            background = ImageIO.read(getClass().getResource("background.jpg"));
            mySelfImg = ImageIO.read(getClass().getResource("mySelf.png"));
            clawImg = ImageIO.read(getClass().getResource("claw1.png"));
            diamondImg = ImageIO.read(getClass().getResource("diamond1.png"));
            rockImg = ImageIO.read(getClass().getResource("rock.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void restoration(int myself) {
        if(myself == 1){
            //��ʼ��λ������
            clawX_1 = xPosition_1 - 20;
            clawY_1 = 250 - 20;
            clawX0_1 = xPosition_1 - 20;
            clawY0_1 = 150 - 20;
            //��ʼ��צ��ץȡ�ٶ�
            clawSpeed_1 = 1;
            //��ʼ��ץ����Ʒ֮��������ٶ�
            stretchSpeed_1 = 3;
            //��ʼ��צ��Ϊ��ת״̬
            clawSpin_1 = true;
            //��ʼ��צ������״̬Ϊ���쳤
            clawStretch_1 = true;
            //��ʼ�����ջر�־λ
            backclaw_1 = false;
            //��ʼ��ץ�����ӵı�־
            grabSucceed_1 = false;
        }else if(myself == 2){
            //��ʼ��λ������
            clawX_2 = xPosition_2 - 20;
            clawY_2 = 250 - 20;
            clawX0_2 = xPosition_2 - 20;
            clawY0_2 = 150 - 20;
            //��ʼ��צ��ץȡ�ٶ�
            clawSpeed_2 = 1;
            //��ʼ��ץ����Ʒ֮��������ٶ�
            stretchSpeed_2 = 3;
            //��ʼ��צ��Ϊ��ת״̬
            clawSpin_2 = true;
            //��ʼ��צ������״̬Ϊ���쳤
            clawStretch_2 = true;
            //��ʼ�����ջر�־λ
            backclaw_2 = false;
            //��ʼ��ץ�����ӵı�־
            grabSucceed_2 = false;
        }
    }

    public void initData() {
        goldSets.clear();
        // �������ӵ�����
        int goldCounts = 10;
        // ÿ��һ�أ��ഴ��һ������
        if (setting.getMyClass() > 1 && setting.getMyClass() < 5) {
            goldCounts ++;
        }
        // ��������
        for (int i = 0; i <= goldCounts; i++) {
            GoldData gold = new GoldData();
            goldSets.add(gold);
        }
        // ��ʼ�����Լ��Ĵ�С
        mySelf_1.goldSize = 100;
        mySelf_2.goldSize = 100;
        // ��ʼ�����˵�λ�ã�����1/3��2/3��
        mySelf_1.goldX = xPosition_1 - (mySelf_1.goldSize);
        mySelf_2.goldX = xPosition_2 - (mySelf_2.goldSize);
        mySelf_1.goldY = 50;
        mySelf_2.goldY = 50;
    }

    public void paint(Graphics g) {
        super.paint(g);
        // ������ͼ
        g.drawImage(background, 0, 0, SoloMode.getWeight(), SoloMode.getHeight(), null);
        // �����Լ�
        g.drawImage(mySelfImg, mySelf_1.goldX, mySelf_1.goldY, mySelf_1.goldSize + 100, mySelf_1.goldSize, null);
        g.drawImage(mySelfImg, mySelf_2.goldX, mySelf_2.goldY, mySelf_2.goldSize + 100, mySelf_2.goldSize, null);
        //�������н���
        for (int i = 0; i < goldSets.size(); i++) {
            GoldData golds = goldSets.get(i);
            if (golds.kind == 5){
                g.drawImage(rockImg, golds.goldX, golds.goldY, golds.goldSize, golds.goldSize, null);
            }
            else if (golds.kind == 4){
                g.drawImage(diamondImg, golds.goldX, golds.goldY, golds.goldSize, golds.goldSize, null);
            }
            else {
                g.drawImage(goldImg, golds.goldX, golds.goldY, golds.goldSize, golds.goldSize, null);
            }
        }
        // ����2D����
        Graphics2D g2 = (Graphics2D) g;
        //���û�����ɫ
        g2.setColor(Color.BLACK);
        //����������ϸ
        g2.setStroke(new BasicStroke(3.0f));
        // ��ϸ��
        g2.drawLine(clawX0_1, clawY0_2, (int) clawX_1, (int) clawY_1);
        g2.drawLine(clawX0_2, clawY0_2, (int) clawX_2, (int) clawY_2);
        // ��צ��
        g.drawImage(clawImg, (int) clawX_1 - 50, (int) clawY_1 - 40, 100, 100, null);
        g.drawImage(clawImg, (int) clawX_2 - 50, (int) clawY_2 - 40, 100, 100, null);
        // ������������
        g.setFont(new Font("����", Font.BOLD, 25));
        // ���÷�
        g.drawString("�÷�:" + setting.getGrade(), 950, 50);
        // ��Ŀ��÷�
        g.drawString("Ŀ��÷�:" + setting.getTargetScore(), 950, 110);
        // ��ʱ��
        g.drawString("ʱ��:" + setting.getTime() / 1000, 20, 110);
        // ���ؿ�
        g.drawString("��" + setting.getMyClass() + "��", 20, 50);
    }

    public void action() {
        //��ȡ����
        requestFocus();
        //���̵���¼�
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                super.keyReleased(e);
                int keyCode = e.getKeyCode();
                // ����ո�
                if (keyCode == KeyEvent.VK_S){
                    clawSpin_1 = false;
                    // ���б��k
                    k_1 = (clawY_1 - clawY0_1) / (clawX_1 - clawX0_1);
                    System.out.println("k_1:" + k_1);
                }else if (keyCode == KeyEvent.VK_DOWN) {
                    clawSpin_2 = false;
                    // ���б��k
                    k_2 = (clawY_2 - clawY0_2) / (clawX_2 - clawX0_2);
                    System.out.println("k_2:" + k_2);
                }
            }

        });

        new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mining(1);
                new MyThread(2).run();
                // ˢ��ʱ��
                setting.setTime(setting.getTime() + 20);
            }
        }).start();
    }

    public void mining(int myself){
        System.out.println("------------------------------------------------------------------------");
        // ��������ˣ�����һ��
        if(pass()) {
            nextClass();
        }
        if (gameOver()) {
            int result = JOptionPane.showConfirmDialog(null, "��Ϸʧ�ܣ�����һ�֣�", "��Ϸ���", JOptionPane.YES_NO_OPTION);
            if (result == 0) {// ����һ�䣬ˢ������
                restoration(1);
                restoration(2);
                initData();
                setting.initSetting();
                repaint();
            } else {// �˳�
                System.exit(0);
            }
        }

        if (myself == 1){
            //׼���׶Σ�צ�Ӽ����ڰ�Բ�����ƶ�
            if (clawSpin_1) {
                System.out.println("׼���׶Σ�צ�Ӽ����ڰ�Բ�����ƶ�");
                // Բ�ķ���
                clawY_1 = (int) (Math
                        .sqrt(10000 - ((clawX_1 - (xPosition_1 - 20)) * (clawX_1 - (xPosition_1 - 20)))))
                        + 150 - 20;
                //����x����仯
                clawX_1 -= clawSpeed_1;
                //�����Ｋ��ʱ,�����ߣ�Ҳ����ֻ�߰�Բ
                if (clawX_1 == xPosition_1 - 20 - 100) {
                    clawSpeed_1 *= -1;
                }
                if (clawX_1 == xPosition_1 - 20 + 100) {
                    clawSpeed_1 *= -1;
                }
            } else { //צ�������׶�
                System.out.println("צ�������׶�");
                // ���Խ�磬���ջ�צ��
                if (clawY_1 >= SoloMode.getHeight() || clawX_1 <= 0 || clawX_1 >= SoloMode.getWeight() + 100) {
                    System.out.println("Խ��");
                    clawStretch_1 = false;
                    backclaw_1 = true;
                }
                //�����������쳤ʱ
                if (clawStretch_1) {
                    System.out.println("צ�������쳤");
                    // �ͷ�צ��
                    releaseClaw(1);
                    // �ж���û��ץ������
                    for (int i = 0; i < goldSets.size(); i++) {
                        GoldData gold = goldSets.get(i);
                        // ���צ����������
                        if (clawX_1 >= gold.goldX && clawX_1 <= gold.goldX + gold.goldSize && clawY_1 >= gold.goldY
                                && clawY_1 <= gold.goldY + gold.goldSize) {
                            // ��������־=�ջ�
                            clawStretch_1 = false;
                            // ��¼��ץ���ӵ�����
                            beGrab_1 = i;
                            // ץ����־��true
                            grabSucceed_1 = true;
                            break;
                        }
                    }
                } else { //�������ջ�צ��ʱ
                    System.out.println("צ���ջ�");
                    // ���û��ץ�����������ջ�
                    if (backclaw_1) {
                        System.out.println("û��ץ�����������ջ�");
                        //�ջ�צ��
                        withdrawClaw(1);
                        // ���յ��Լ�λ��ʱ,��λ
                        if (clawX_1 >= clawX0_1 - 20 && clawX_1 <= clawX0_1 + 20 && clawY_1 <= clawY0_1 + 20) {
                            restoration(1);
                        }
                    }
                    // ���ץ�����ӣ����ջ�צ�ӣ���ץ���Ľ���
                    if (grabSucceed_1) {
                        System.out.println("ץ�����ӣ��ջ�צ�ӣ���ץ���Ľ���");
                        // ���ץ�����Ǹ�����
                        GoldData gold = goldSets.get(beGrab_1);
                        // �����Ʒ̫�������ٶȱ���
                        if (gold.kind == 4) {
                            stretchSpeed_1 = 4;
                        }
                        if (gold.goldSize >= 100) {
                            stretchSpeed_1 = 2;
                        }
                        if (gold.kind == 5) {
                            stretchSpeed_1 = 1;
                        }
                        // �ջ�צ��
                        withdrawClaw(1);
                        // �ջ���������Ʒ
                        gold.goldX = (int) clawX_1 - 25;
                        gold.goldY = (int) clawY_1;
                        // ���յ��Լ�λ��ʱ�������˽���
                        if (isWithdrawSucceed(1)) {
                            goldSets.remove(beGrab_1);
                            // ����צ��
                            restoration(1);
                            // ���ӵ÷�
                            updateGrade(gold);
                        }
                    }
                }
            }
        }else if(myself == 2){
            //׼���׶Σ�צ�Ӽ����ڰ�Բ�����ƶ�
            if (clawSpin_2) {
                System.out.println("׼���׶Σ�צ�Ӽ����ڰ�Բ�����ƶ�");
                // Բ�ķ���
                clawY_2 = (int) (Math
                        .sqrt(10000 - ((clawX_2 - (xPosition_2 - 20)) * (clawX_2 - (xPosition_2 - 20)))))
                        + 150 - 20;
                //����x����仯
                clawX_2 -= clawSpeed_2;
                //�����Ｋ��ʱ,�����ߣ�Ҳ����ֻ�߰�Բ
                if (clawX_2 == xPosition_2 - 20 - 100) {
                    clawSpeed_2 *= -1;
                }
                if (clawX_2 == xPosition_2 - 20 + 100) {
                    clawSpeed_2 *= -1;
                }
            } else { //צ�������׶�
                System.out.println("צ�������׶�");
                // ���Խ�磬���ջ�צ��
                if (clawY_2 >= SoloMode.getHeight() || clawX_2 <= 0 || clawX_2 >= SoloMode.getWeight() + 100) {
                    System.out.println("Խ��");
                    clawStretch_2 = false;
                    backclaw_2 = true;
                }
                //�����������쳤ʱ
                if (clawStretch_2) {
                    System.out.println("צ�������쳤");
                    // �ͷ�צ��
                    releaseClaw(2);
                    // �ж���û��ץ������
                    for (int i = 0; i < goldSets.size(); i++) {
                        GoldData gold = goldSets.get(i);
                        // ���צ����������
                        if (clawX_2 >= gold.goldX && clawX_2 <= gold.goldX + gold.goldSize && clawY_2 >= gold.goldY
                                && clawY_2 <= gold.goldY + gold.goldSize) {
                            // ��������־=�ջ�
                            clawStretch_2 = false;
                            // ��¼��ץ���ӵ�����
                            beGrab_2 = i;
                            // ץ����־��true
                            grabSucceed_2 = true;
                            break;
                        }
                    }
                } else { //�������ջ�צ��ʱ
                    System.out.println("צ���ջ�");
                    // ���û��ץ�����������ջ�
                    if (backclaw_2) {
                        System.out.println("û��ץ�����������ջ�");
                        //�ջ�צ��
                        withdrawClaw(2);
                        // ���յ��Լ�λ��ʱ,��λ
                        if (clawX_2 >= clawX0_2 - 20 && clawX_2 <= clawX0_2 + 20 && clawY_2 <= clawY0_2 + 20) {
                            restoration(2);
                        }
                    }
                    // ���ץ�����ӣ����ջ�צ�ӣ���ץ���Ľ���
                    if (grabSucceed_2) {
                        System.out.println("ץ�����ӣ��ջ�צ�ӣ���ץ���Ľ���");
                        // ���ץ�����Ǹ�����
                        GoldData gold = goldSets.get(beGrab_2);
                        // �����Ʒ̫�������ٶȱ���
                        if (gold.kind == 4) {
                            stretchSpeed_1 = 4;
                        }
                        if (gold.goldSize >= 100) {
                            stretchSpeed_1 = 2;
                        }
                        if (gold.kind == 5) {
                            stretchSpeed_1 = 1;
                        }
                        // �ջ�צ��
                        withdrawClaw(2);
                        // �ջ���������Ʒ
                        gold.goldX = (int) clawX_2 - 25;
                        gold.goldY = (int) clawY_2;
                        // ���յ��Լ�λ��ʱ�������˽���
                        if (isWithdrawSucceed(2)) {
                            goldSets.remove(beGrab_2);
                            // ����צ��
                            restoration(2);
                            // ���ӵ÷�
                            updateGrade(gold);
                        }
                    }
                }
            }
        }
        //ˢ�£����»�ͼ��
        repaint();
    }

    /**
     *
     * �������ͷ�צ��
     */
    public void releaseClaw(int myself) {
        if (myself == 1){
            if (clawX_1 <= clawX0_1) {
                clawX_1 -= 2;

            } else {
                clawX_1 += 2;
            }
            // ֱ�߷���
            clawY_1 = k_1 * (clawX_1 - clawX0_1) + clawY0_1;
        } else if (myself == 2){
            if (clawX_2 <= clawX0_2) {
                clawX_2 -= 2;

            } else {
                clawX_2 += 2;
            }
            // ֱ�߷���
            clawY_2 = k_2 * (clawX_2 - clawX0_2) + clawY0_2;
        }

    }
    /**
     *
     * �������ջ�צ��
     */
    public void withdrawClaw(int myself) {
        if (myself == 1){
            if (clawX_1 <= clawX0_1) {
                clawX_1 += stretchSpeed_1;
            }
            if (clawX_1 > clawX0_1) {
                clawX_1 -= stretchSpeed_1;
            }
            //ֱ�߷���
            clawY_1 = k_1 * (clawX_1 - clawX0_1) + clawY0_1;
        } else if (myself == 2){
            if (clawX_2 <= clawX0_2) {
                clawX_2 += stretchSpeed_2;
            }
            if (clawX_2 > clawX0_2) {
                clawX_2 -= stretchSpeed_2;
            }
            //ֱ�߷���
            clawY_2 = k_2 * (clawX_2 - clawX0_2) + clawY0_2;
        }
    }
    /**
     *
     * �������ж�צ���Ƿ��ջسɹ�
     * @return ���צ���ջسɹ�������true
     */
    public boolean isWithdrawSucceed(int myself) {
        if (myself == 1){
            if (clawX_1 >= clawX0_1 - 20 && clawX_1 <= clawX0_1 + 20 && clawY_1 <= clawY0_1 + 20) {
                return true;
            }
            return false;
        } else {
            if (clawX_2 >= clawX0_2 - 20 && clawX_2 <= clawX0_2 + 20 && clawY_2 <= clawY0_2 + 20) {
                return true;
            }
            return false;
        }

    }

    // ��һ��
    public void nextClass() {
        restoration(1);
        restoration(2);
        initData();
        setting.passSetting();
        setting.setMyClass(setting.getMyClass() + 1);
        setting.setTargetScore(setting.getTargetScore() + 200);
    }
    // ���µ÷�
    public void updateGrade(GoldData gold) {
        if (gold.kind == 1) {
            setting.setGrade(setting.getGrade() + 50);
        }
        if (gold.kind == 2) {
            setting.setGrade(setting.getGrade() + 80);
        }
        if (gold.kind == 3) {
            setting.setGrade(setting.getGrade() + 100);
        }
        if (gold.kind == 4) {
            setting.setGrade(setting.getGrade() + 200);
        }
        if (gold.kind == 5) {
            setting.setGrade(setting.getGrade() + 25);
        }
    }

    public boolean pass() {
        int count = 0;

        // �������ָ��������ֱ�ӹ���
        if (setting.getGrade() >= setting.getTargetScore()) {
            System.out.println(setting.getTargetScore());
            return true;
        }

        // �����Ϸ��ʱ�ˣ����ж���û�е���ָ������
        if (overtime(setting.getTime())) {
            if (setting.getGrade() >= setting.getTargetScore()) {
                System.out.println("ͨ��");
                return true;
            }
        }
        // ���û�г�ʱ��Ҳû�е���ָ��������Ҳû�н����ˣ�Ҳ����� ������

        //�����������м�������
        for (int i = 0; i < goldSets.size(); i++) {
            if (goldSets.get(i) != null) {
                count++;
            }
        }
        // ��û�н��ӵ�ʱ����Ϸ����
        if (count == 0) {
            System.out.println("û�н�����");
            return true;
        }
        return false;
    }
    public boolean gameOver() {

        if (overtime(setting.getTime())) {
            if (setting.getGrade() < setting.getTargetScore()) {
                System.out.println("ʧ��");
                return true;
            }
        }

        return false;

    }

    // ��Ϸ�����ֹʱ��
    public boolean overtime(int time) {
        // ÿ���ؿ����� 60 ��ʱ��
        if (time > setting.deadline) {
            return true;
        }
        return false;
    }

    public static ArrayList<GoldData> getGoldSets() {
        return goldSets;
    }

    class MyThread extends Thread{
        // ���1�Ż���2��
        private int player;

        public MyThread(int i){
            player = i;
        }

        @Override
        public void run() {
            mining(player);
        }
    }

}



