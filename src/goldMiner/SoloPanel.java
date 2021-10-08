package goldMiner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SoloPanel extends JPanel {
	/**
	 * 创建一个集合，存储所有金子
	 */
	static ArrayList<GoldData> goldSets = new ArrayList<GoldData>();
	/**
	 * 创建我自己，比拟金子，继承金子的属性
	 */
	GoldData mySelf = new GoldData();
	//定义图片
	BufferedImage goldImg;
	BufferedImage background;
	BufferedImage mySelfImg;
	BufferedImage clawImg;
	// 定义爪子位置
	/**
	 * 细线初始点横坐标
	 */
	int clawX0;
	/**
	 * 细线初始点纵坐标
	 */
	int clawY0;
	/**
	 * 细线结束点横坐标，及爪子横坐标
	 */
	double clawX;
	/**
	 * 细线结束点纵坐标，及爪子纵坐标
	 */
	double clawY;
	/**
	 * 细线旋转速度
	 */
	int clawSpeed;
	/**
	 * 细线伸缩速度
	 */
	int stretchSpeed;
	/**
	 * 爪子是否处于旋转准备状态标志
	 */
	boolean clawSpin;
	/**
	 * 爪子是否可以伸缩标志
	 * true:可以向下伸
	 * false:可以向上收回
	 */
	boolean clawStretch;
	/**
	 * 细线的斜率
	 */
	double k;
	double b;
	/**
	 * 空收回标志
	 * true: 没有抓到东西，空收回
	 * false: 其它状态
	 */
	boolean backclaw;
	// 按钮组件
	JButton pauseJButton;

	/**
	 * 被抓金子索引
	 */
	int beGrab;
	/**
	 * 是否抓到金子
	 * true: 抓到金子了
	 * flase: 没抓到金子
	 */
	boolean grabSucceed;
	
	
//	JudegResult judegResult = new JudegResult();
	Setting setting = new Setting();

	//无参构造，初始化一些数据
	public SoloPanel() {
		restoration();
		initData();
		setting.initSetting();
		
		//缓冲图片
		try {
			goldImg = ImageIO.read(getClass().getResource("gold.png"));
			background = ImageIO.read(getClass().getResource("background.jpg"));
			mySelfImg = ImageIO.read(getClass().getResource("mySelf.png"));
			clawImg = ImageIO.read(getClass().getResource("claw1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 描述：初始化界面组件
	 */
	public void initPanel() {
		this.setLayout(null);
		pauseJButton = new JButton("pause");
		pauseJButton.setBounds(1000, 50, 100, 60);
		// this.add(pauseJButton);
	}

	public void paint(Graphics g) {
		super.paint(g);
		// 画背景图
		g.drawImage(background, 0, 0, SoloMode.getWeight(), SoloMode.getHeight(), null);
		// 画我自己
		g.drawImage(mySelfImg, mySelf.goldX, mySelf.goldY, mySelf.goldSize + 100, mySelf.goldSize, null);
		//画出所有金子
		for (int i = 0; i < goldSets.size(); i++) {
			GoldData golds = goldSets.get(i);
			g.drawImage(goldImg, golds.goldX, golds.goldY, golds.goldSize, golds.goldSize, null);
		}
		// 设置2D画笔
		Graphics2D g2 = (Graphics2D) g;
		//设置画笔颜色
		g2.setColor(Color.BLACK);
		//设置线条粗细
		g2.setStroke(new BasicStroke(3.0f));
		// 画细线
		g2.drawLine(clawX0, clawY0, (int) clawX, (int) clawY);
		// 画爪子
		g.drawImage(clawImg, (int) clawX - 50, (int) clawY - 40, 100, 100, null);
		// 设置字体属性
		g.setFont(new Font("宋体", Font.BOLD, 25));
		// 画得分
		g.drawString("得分:" + setting.getGrade(), 1000, 50);
		// 画目标得分
		g.drawString("目标得分:" + setting.getTargetScore(), 1000, 110);
		// 画时间
		g.drawString("时间:" + setting.getTime() / 1000, 20, 110);
		// 画关卡
		g.drawString("第" + setting.getMyClass() + "关", 20, 50);
	}

	/**
	 * 
	 * 描述：初始化数据
	 */
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
		mySelf.goldSize = 100;
		// 初始化我自己的位置，放在中间
		mySelf.goldX = (SoloMode.getWeight() / 2) - (mySelf.goldSize);
		mySelf.goldY = 50;
	}

	/**
	 * 
	 * 描述：释放爪子
	 */
	public void releaseClaw() {
		// clawY = -k * clawX - b;
		
		if (clawX <= clawX0) {
			clawX -= 2;
			
		} else {
			clawX += 2;
		}
		// 直线方程
		clawY = k * (clawX - clawX0) + clawY0;
	}
	/**
	 * 
	 * 描述：收回爪子
	 */
	public void withdrawClaw() {
		if (clawX <= clawX0) {
			clawX += stretchSpeed;
		}
		if (clawX > clawX0) {
			clawX -= stretchSpeed;
		}
		//直线方程
		clawY = k * (clawX - clawX0) + clawY0;
	}
	/**
	 * 
	 * 描述：判断爪子是否收回成功
	 * @return 如果爪子收回成功，返回true
	 */
	public boolean isWithdrawSucceed() {
		if (clawX >= clawX0 - 20 && clawX <= clawX0 + 20 && clawY <= clawY0 + 20) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * 描述：恢复爪子设置
	 */
	public void restoration() {
		
		//初始化位置坐标
		clawX = 600 - 20;
		clawY = 250 - 20;
		clawX0 = SoloMode.getWeight() / 2 - 20;
		clawY0 = 150 - 20;
		//初始化爪子抓取速度
		clawSpeed = 1;
		//初始化抓到物品之后的伸缩速度
		stretchSpeed = 3;
		//初始化爪子为旋转状态
		clawSpin = true;
		//初始化爪子伸缩状态为可伸长
		clawStretch = true;
		//初始化空收回标志位
		backclaw = false;
		//初始化抓到金子的标志
		grabSucceed = false;
	}

	public void action() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				clawSpin = false;
				// 求出斜率k
				k = (clawY - clawY0) / (clawX - clawX0);
				b = clawY - (k * clawX);
			}

		});
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
				if (keyCode == KeyEvent.VK_SPACE) {
					clawSpin = false;
					// 求出斜率k
					k = (clawY - clawY0) / (clawX - clawX0);
				}
			}

		});
		//程序主循环
		while (true) {
			// 如果过关了，就下一关
			if(pass()) {
				nextClass();
			}
			if (gameOver()) {
				int result = JOptionPane.showConfirmDialog(null, "游戏失败，再来一局？", "游戏结果", JOptionPane.YES_NO_OPTION);
				if (result == 0) {// 再来一句，刷新数据
					restoration();
					initData();
					setting.initSetting();
					repaint();
				} else {// 退出
					System.exit(0);
				}
			}
			
			
			//准备阶段，爪子及线在半圆弧上移动
			if (clawSpin) {
				// 圆的方程
				clawY = (int) (Math
						.sqrt(10000 - ((clawX - (SoloMode.getWeight() / 2 - 20)) * (clawX - (SoloMode.getWeight() / 2 - 20)))))
						+ 150 - 20;
				//随着x坐标变化
				clawX -= clawSpeed;
				//当到达极限时,变向走，也就是只走半圆
				if (clawX == 480) {
					clawSpeed *= -1;
				}
				if (clawX == 680) {
					clawSpeed *= -1;
				}
			} else { //爪子伸缩阶段
				// 如果越界，则收回爪子
				if (clawY >= 800 || clawX <= 0 || clawX >= SoloMode.getWeight() + 100) {
					clawStretch = false;
					backclaw = true;
				}
				//当可以向下伸长时
				if (clawStretch) {
					// 释放爪子
					releaseClaw();
					// 判断有没有抓到金子
					for (int i = 0; i < goldSets.size(); i++) {
						GoldData gold = goldSets.get(i);
						// 如果爪子碰到金子
						if (clawX >= gold.goldX && clawX <= gold.goldX + gold.goldSize && clawY >= gold.goldY
								&& clawY <= gold.goldY + gold.goldSize) {
							// 则伸缩标志=收回
							clawStretch = false;
							// 记录被抓金子的索引
							beGrab = i;
							// 抓到标志置true
							grabSucceed = true;
							break;
						}
					}
				} else { //当可以收回爪子时
					// 如果没有抓到东西，空收回
					if (backclaw) {
						//收回爪子
						withdrawClaw();
						// 当收到自己位置时,复位
						if (clawX >= clawX0 - 20 && clawX <= clawX0 + 20 && clawY <= clawY0 + 20) {
							restoration();
						}
					}
					// 如果抓到金子，则收回爪子，及抓到的金子
					if (grabSucceed) {
						// 获得抓到的那个金子
						GoldData gold = goldSets.get(beGrab);
						// 如果物品太大，收线速度变慢
						if (gold.goldSize >= 70) {
							stretchSpeed = 2;
						}
						if (gold.goldSize >= 120) {
							stretchSpeed = 1;
						}
						// 收回爪子
						withdrawClaw();
						// 收回碰到的物品
						gold.goldX = (int) clawX - 50;
						gold.goldY = (int) clawY;
						// 当收到自己位置时，消除此金子
						if (isWithdrawSucceed()) {
							goldSets.remove(beGrab);
							// 重置爪子
							restoration();
							// 增加得分
							updateGrade(gold);
						}
					}
				}
			}
			
			//刷新间隔为20ms刷新一次
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 刷新时间
			setting.setTime(setting.getTime() + 20);
			//刷新（重新绘图）
			repaint();
		}
	}
	// 下一关
	public void nextClass() {
		restoration();
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

}
