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
     * 创建一个集合，存储所有金子
     */
    static ArrayList<GoldData> goldSets = new ArrayList<GoldData>();
    /**
     * 创建我自己，比拟金子，继承金子的属性
     */
    GoldData mySelf_1 = new GoldData();
    GoldData mySelf_2 = new GoldData();
    //定义图片
    BufferedImage goldImg;
    BufferedImage background;
    BufferedImage mySelfImg;
    BufferedImage clawImg;
    BufferedImage diamondImg;
    BufferedImage rockImg;

    // 定义爪子位置
    /**
     * 细线初始点横坐标
     */
    int clawX0_1;
    int clawX0_2;
    /**
     * 细线初始点纵坐标
     */
    int clawY0_1;
    int clawY0_2;
    /**
     * 细线结束点横坐标，及爪子横坐标
     */
    double clawX_1;
    double clawX_2;
    /**
     * 细线结束点纵坐标，及爪子纵坐标
     */
    double clawY_1;
    double clawY_2;
    /**
     * 细线旋转速度
     */
    int clawSpeed_1;
    int clawSpeed_2;
    /**
     * 细线伸缩速度
     */
    int stretchSpeed_1;
    int stretchSpeed_2;
    /**
     * 爪子是否处于旋转准备状态标志
     */
    boolean clawSpin_1;
    boolean clawSpin_2;
    /**
     * 爪子是否可以伸缩标志
     * true:可以向下伸
     * false:可以向上收回
     */
    boolean clawStretch_1;
    boolean clawStretch_2;
    /**
     * 细线的斜率
     */
    double k_1;
    double k_2;
    double b_1;
    double b_2;
    /**
     * 空收回标志
     * true: 没有抓到东西，空收回
     * false: 其它状态
     */
    boolean backclaw_1;
    boolean backclaw_2;
    // 按钮组件
    JButton pauseJButton_1;
    JButton pauseJButton_2;

    /**
     * 被抓金子索引
     */
    int beGrab_1;
    int beGrab_2;
    /**
     * 是否抓到金子
     * true: 抓到金子了
     * flase: 没抓到金子
     */
    boolean grabSucceed_1;
    boolean grabSucceed_2;

    Setting setting = new Setting();

    //无参构造，初始化一些数据
    public DoublePanel() {
        restoration(1);
        restoration(2);
        initData();
        setting.initSetting();

        //缓冲图片
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
            //初始化位置坐标
            clawX_1 = xPosition_1 - 20;
            clawY_1 = 250 - 20;
            clawX0_1 = xPosition_1 - 20;
            clawY0_1 = 150 - 20;
            //初始化爪子抓取速度
            clawSpeed_1 = 1;
            //初始化抓到物品之后的伸缩速度
            stretchSpeed_1 = 3;
            //初始化爪子为旋转状态
            clawSpin_1 = true;
            //初始化爪子伸缩状态为可伸长
            clawStretch_1 = true;
            //初始化空收回标志位
            backclaw_1 = false;
            //初始化抓到金子的标志
            grabSucceed_1 = false;
        }else if(myself == 2){
            //初始化位置坐标
            clawX_2 = xPosition_2 - 20;
            clawY_2 = 250 - 20;
            clawX0_2 = xPosition_2 - 20;
            clawY0_2 = 150 - 20;
            //初始化爪子抓取速度
            clawSpeed_2 = 1;
            //初始化抓到物品之后的伸缩速度
            stretchSpeed_2 = 3;
            //初始化爪子为旋转状态
            clawSpin_2 = true;
            //初始化爪子伸缩状态为可伸长
            clawStretch_2 = true;
            //初始化空收回标志位
            backclaw_2 = false;
            //初始化抓到金子的标志
            grabSucceed_2 = false;
        }
    }

    public void initData() {
        goldSets.clear();
        // 创建金子的数量
        int goldCounts = 10;
        // 每过一关，多创建一个金子
        if (setting.getMyClass() > 1 && setting.getMyClass() < 5) {
            goldCounts ++;
        }
        // 创建金子
        for (int i = 0; i <= goldCounts; i++) {
            GoldData gold = new GoldData();
            goldSets.add(gold);
        }
        // 初始化我自己的大小
        mySelf_1.goldSize = 100;
        mySelf_2.goldSize = 100;
        // 初始化两人的位置，放在1/3和2/3处
        mySelf_1.goldX = xPosition_1 - (mySelf_1.goldSize);
        mySelf_2.goldX = xPosition_2 - (mySelf_2.goldSize);
        mySelf_1.goldY = 50;
        mySelf_2.goldY = 50;
    }

    public void paint(Graphics g) {
        super.paint(g);
        // 画背景图
        g.drawImage(background, 0, 0, SoloMode.getWeight(), SoloMode.getHeight(), null);
        // 画我自己
        g.drawImage(mySelfImg, mySelf_1.goldX, mySelf_1.goldY, mySelf_1.goldSize + 100, mySelf_1.goldSize, null);
        g.drawImage(mySelfImg, mySelf_2.goldX, mySelf_2.goldY, mySelf_2.goldSize + 100, mySelf_2.goldSize, null);
        //画出所有金子
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
        // 设置2D画笔
        Graphics2D g2 = (Graphics2D) g;
        //设置画笔颜色
        g2.setColor(Color.BLACK);
        //设置线条粗细
        g2.setStroke(new BasicStroke(3.0f));
        // 画细线
        g2.drawLine(clawX0_1, clawY0_2, (int) clawX_1, (int) clawY_1);
        g2.drawLine(clawX0_2, clawY0_2, (int) clawX_2, (int) clawY_2);
        // 画爪子
        g.drawImage(clawImg, (int) clawX_1 - 50, (int) clawY_1 - 40, 100, 100, null);
        g.drawImage(clawImg, (int) clawX_2 - 50, (int) clawY_2 - 40, 100, 100, null);
        // 设置字体属性
        g.setFont(new Font("宋体", Font.BOLD, 25));
        // 画得分
        g.drawString("得分:" + setting.getGrade(), 950, 50);
        // 画目标得分
        g.drawString("目标得分:" + setting.getTargetScore(), 950, 110);
        // 画时间
        g.drawString("时间:" + setting.getTime() / 1000, 20, 110);
        // 画关卡
        g.drawString("第" + setting.getMyClass() + "关", 20, 50);
    }

    public void action() {
        //获取焦点
        requestFocus();
        //键盘点击事件
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                super.keyReleased(e);
                int keyCode = e.getKeyCode();
                // 点击空格
                if (keyCode == KeyEvent.VK_S){
                    clawSpin_1 = false;
                    // 求出斜率k
                    k_1 = (clawY_1 - clawY0_1) / (clawX_1 - clawX0_1);
                    System.out.println("k_1:" + k_1);
                }else if (keyCode == KeyEvent.VK_DOWN) {
                    clawSpin_2 = false;
                    // 求出斜率k
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
                // 刷新时间
                setting.setTime(setting.getTime() + 20);
            }
        }).start();
    }

    public void mining(int myself){
        System.out.println("------------------------------------------------------------------------");
        // 如果过关了，就下一关
        if(pass()) {
            nextClass();
        }
        if (gameOver()) {
            int result = JOptionPane.showConfirmDialog(null, "游戏失败，再来一局？", "游戏结果", JOptionPane.YES_NO_OPTION);
            if (result == 0) {// 再来一句，刷新数据
                restoration(1);
                restoration(2);
                initData();
                setting.initSetting();
                repaint();
            } else {// 退出
                System.exit(0);
            }
        }

        if (myself == 1){
            //准备阶段，爪子及线在半圆弧上移动
            if (clawSpin_1) {
                System.out.println("准备阶段，爪子及线在半圆弧上移动");
                // 圆的方程
                clawY_1 = (int) (Math
                        .sqrt(10000 - ((clawX_1 - (xPosition_1 - 20)) * (clawX_1 - (xPosition_1 - 20)))))
                        + 150 - 20;
                //随着x坐标变化
                clawX_1 -= clawSpeed_1;
                //当到达极限时,变向走，也就是只走半圆
                if (clawX_1 == xPosition_1 - 20 - 100) {
                    clawSpeed_1 *= -1;
                }
                if (clawX_1 == xPosition_1 - 20 + 100) {
                    clawSpeed_1 *= -1;
                }
            } else { //爪子伸缩阶段
                System.out.println("爪子伸缩阶段");
                // 如果越界，则收回爪子
                if (clawY_1 >= SoloMode.getHeight() || clawX_1 <= 0 || clawX_1 >= SoloMode.getWeight() + 100) {
                    System.out.println("越界");
                    clawStretch_1 = false;
                    backclaw_1 = true;
                }
                //当可以向下伸长时
                if (clawStretch_1) {
                    System.out.println("爪子向下伸长");
                    // 释放爪子
                    releaseClaw(1);
                    // 判断有没有抓到金子
                    for (int i = 0; i < goldSets.size(); i++) {
                        GoldData gold = goldSets.get(i);
                        // 如果爪子碰到金子
                        if (clawX_1 >= gold.goldX && clawX_1 <= gold.goldX + gold.goldSize && clawY_1 >= gold.goldY
                                && clawY_1 <= gold.goldY + gold.goldSize) {
                            // 则伸缩标志=收回
                            clawStretch_1 = false;
                            // 记录被抓金子的索引
                            beGrab_1 = i;
                            // 抓到标志置true
                            grabSucceed_1 = true;
                            break;
                        }
                    }
                } else { //当可以收回爪子时
                    System.out.println("爪子收回");
                    // 如果没有抓到东西，空收回
                    if (backclaw_1) {
                        System.out.println("没有抓到东西，空收回");
                        //收回爪子
                        withdrawClaw(1);
                        // 当收到自己位置时,复位
                        if (clawX_1 >= clawX0_1 - 20 && clawX_1 <= clawX0_1 + 20 && clawY_1 <= clawY0_1 + 20) {
                            restoration(1);
                        }
                    }
                    // 如果抓到金子，则收回爪子，及抓到的金子
                    if (grabSucceed_1) {
                        System.out.println("抓到金子，收回爪子，及抓到的金子");
                        // 获得抓到的那个金子
                        GoldData gold = goldSets.get(beGrab_1);
                        // 如果物品太大，收线速度变慢
                        if (gold.kind == 4) {
                            stretchSpeed_1 = 4;
                        }
                        if (gold.goldSize >= 100) {
                            stretchSpeed_1 = 2;
                        }
                        if (gold.kind == 5) {
                            stretchSpeed_1 = 1;
                        }
                        // 收回爪子
                        withdrawClaw(1);
                        // 收回碰到的物品
                        gold.goldX = (int) clawX_1 - 25;
                        gold.goldY = (int) clawY_1;
                        // 当收到自己位置时，消除此金子
                        if (isWithdrawSucceed(1)) {
                            goldSets.remove(beGrab_1);
                            // 重置爪子
                            restoration(1);
                            // 增加得分
                            updateGrade(gold);
                        }
                    }
                }
            }
        }else if(myself == 2){
            //准备阶段，爪子及线在半圆弧上移动
            if (clawSpin_2) {
                System.out.println("准备阶段，爪子及线在半圆弧上移动");
                // 圆的方程
                clawY_2 = (int) (Math
                        .sqrt(10000 - ((clawX_2 - (xPosition_2 - 20)) * (clawX_2 - (xPosition_2 - 20)))))
                        + 150 - 20;
                //随着x坐标变化
                clawX_2 -= clawSpeed_2;
                //当到达极限时,变向走，也就是只走半圆
                if (clawX_2 == xPosition_2 - 20 - 100) {
                    clawSpeed_2 *= -1;
                }
                if (clawX_2 == xPosition_2 - 20 + 100) {
                    clawSpeed_2 *= -1;
                }
            } else { //爪子伸缩阶段
                System.out.println("爪子伸缩阶段");
                // 如果越界，则收回爪子
                if (clawY_2 >= SoloMode.getHeight() || clawX_2 <= 0 || clawX_2 >= SoloMode.getWeight() + 100) {
                    System.out.println("越界");
                    clawStretch_2 = false;
                    backclaw_2 = true;
                }
                //当可以向下伸长时
                if (clawStretch_2) {
                    System.out.println("爪子向下伸长");
                    // 释放爪子
                    releaseClaw(2);
                    // 判断有没有抓到金子
                    for (int i = 0; i < goldSets.size(); i++) {
                        GoldData gold = goldSets.get(i);
                        // 如果爪子碰到金子
                        if (clawX_2 >= gold.goldX && clawX_2 <= gold.goldX + gold.goldSize && clawY_2 >= gold.goldY
                                && clawY_2 <= gold.goldY + gold.goldSize) {
                            // 则伸缩标志=收回
                            clawStretch_2 = false;
                            // 记录被抓金子的索引
                            beGrab_2 = i;
                            // 抓到标志置true
                            grabSucceed_2 = true;
                            break;
                        }
                    }
                } else { //当可以收回爪子时
                    System.out.println("爪子收回");
                    // 如果没有抓到东西，空收回
                    if (backclaw_2) {
                        System.out.println("没有抓到东西，空收回");
                        //收回爪子
                        withdrawClaw(2);
                        // 当收到自己位置时,复位
                        if (clawX_2 >= clawX0_2 - 20 && clawX_2 <= clawX0_2 + 20 && clawY_2 <= clawY0_2 + 20) {
                            restoration(2);
                        }
                    }
                    // 如果抓到金子，则收回爪子，及抓到的金子
                    if (grabSucceed_2) {
                        System.out.println("抓到金子，收回爪子，及抓到的金子");
                        // 获得抓到的那个金子
                        GoldData gold = goldSets.get(beGrab_2);
                        // 如果物品太大，收线速度变慢
                        if (gold.kind == 4) {
                            stretchSpeed_1 = 4;
                        }
                        if (gold.goldSize >= 100) {
                            stretchSpeed_1 = 2;
                        }
                        if (gold.kind == 5) {
                            stretchSpeed_1 = 1;
                        }
                        // 收回爪子
                        withdrawClaw(2);
                        // 收回碰到的物品
                        gold.goldX = (int) clawX_2 - 25;
                        gold.goldY = (int) clawY_2;
                        // 当收到自己位置时，消除此金子
                        if (isWithdrawSucceed(2)) {
                            goldSets.remove(beGrab_2);
                            // 重置爪子
                            restoration(2);
                            // 增加得分
                            updateGrade(gold);
                        }
                    }
                }
            }
        }
        //刷新（重新绘图）
        repaint();
    }

    /**
     *
     * 描述：释放爪子
     */
    public void releaseClaw(int myself) {
        if (myself == 1){
            if (clawX_1 <= clawX0_1) {
                clawX_1 -= 2;

            } else {
                clawX_1 += 2;
            }
            // 直线方程
            clawY_1 = k_1 * (clawX_1 - clawX0_1) + clawY0_1;
        } else if (myself == 2){
            if (clawX_2 <= clawX0_2) {
                clawX_2 -= 2;

            } else {
                clawX_2 += 2;
            }
            // 直线方程
            clawY_2 = k_2 * (clawX_2 - clawX0_2) + clawY0_2;
        }

    }
    /**
     *
     * 描述：收回爪子
     */
    public void withdrawClaw(int myself) {
        if (myself == 1){
            if (clawX_1 <= clawX0_1) {
                clawX_1 += stretchSpeed_1;
            }
            if (clawX_1 > clawX0_1) {
                clawX_1 -= stretchSpeed_1;
            }
            //直线方程
            clawY_1 = k_1 * (clawX_1 - clawX0_1) + clawY0_1;
        } else if (myself == 2){
            if (clawX_2 <= clawX0_2) {
                clawX_2 += stretchSpeed_2;
            }
            if (clawX_2 > clawX0_2) {
                clawX_2 -= stretchSpeed_2;
            }
            //直线方程
            clawY_2 = k_2 * (clawX_2 - clawX0_2) + clawY0_2;
        }
    }
    /**
     *
     * 描述：判断爪子是否收回成功
     * @return 如果爪子收回成功，返回true
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

    // 下一关
    public void nextClass() {
        restoration(1);
        restoration(2);
        initData();
        setting.passSetting();
        setting.setMyClass(setting.getMyClass() + 1);
        setting.setTargetScore(setting.getTargetScore() + 200);
    }
    // 更新得分
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

        // 如果到达指定分数，直接过关
        if (setting.getGrade() >= setting.getTargetScore()) {
            System.out.println(setting.getTargetScore());
            return true;
        }

        // 如果游戏超时了，再判断有没有到达指定分数
        if (overtime(setting.getTime())) {
            if (setting.getGrade() >= setting.getTargetScore()) {
                System.out.println("通关");
                return true;
            }
        }
        // 如果没有超时，也没有到达指定分数，也没有金子了，也算过关 ！！！

        //遍历集合中有几个金子
        for (int i = 0; i < goldSets.size(); i++) {
            if (goldSets.get(i) != null) {
                count++;
            }
        }
        // 当没有金子的时候，游戏结束
        if (count == 0) {
            System.out.println("没有金子了");
            return true;
        }
        return false;
    }
    public boolean gameOver() {

        if (overtime(setting.getTime())) {
            if (setting.getGrade() < setting.getTargetScore()) {
                System.out.println("失败");
                return true;
            }
        }

        return false;

    }

    // 游戏到达截止时间
    public boolean overtime(int time) {
        // 每个关卡设置 60 秒时间
        if (time > setting.deadline) {
            return true;
        }
        return false;
    }

    public static ArrayList<GoldData> getGoldSets() {
        return goldSets;
    }

    class MyThread extends Thread{
        // 玩家1号还是2号
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



