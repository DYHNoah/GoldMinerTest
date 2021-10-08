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
	 * ����һ�����ϣ��洢���н���
	 */
	static ArrayList<GoldData> goldSets = new ArrayList<GoldData>();
	/**
	 * �������Լ���������ӣ��̳н��ӵ�����
	 */
	GoldData mySelf = new GoldData();
	//����ͼƬ
	BufferedImage goldImg;
	BufferedImage background;
	BufferedImage mySelfImg;
	BufferedImage clawImg;
	// ����צ��λ��
	/**
	 * ϸ�߳�ʼ�������
	 */
	int clawX0;
	/**
	 * ϸ�߳�ʼ��������
	 */
	int clawY0;
	/**
	 * ϸ�߽���������꣬��צ�Ӻ�����
	 */
	double clawX;
	/**
	 * ϸ�߽����������꣬��צ��������
	 */
	double clawY;
	/**
	 * ϸ����ת�ٶ�
	 */
	int clawSpeed;
	/**
	 * ϸ�������ٶ�
	 */
	int stretchSpeed;
	/**
	 * צ���Ƿ�����ת׼��״̬��־
	 */
	boolean clawSpin;
	/**
	 * צ���Ƿ����������־
	 * true:����������
	 * false:���������ջ�
	 */
	boolean clawStretch;
	/**
	 * ϸ�ߵ�б��
	 */
	double k;
	double b;
	/**
	 * ���ջر�־
	 * true: û��ץ�����������ջ�
	 * false: ����״̬
	 */
	boolean backclaw;
	// ��ť���
	JButton pauseJButton;

	/**
	 * ��ץ��������
	 */
	int beGrab;
	/**
	 * �Ƿ�ץ������
	 * true: ץ��������
	 * flase: ûץ������
	 */
	boolean grabSucceed;
	
	
//	JudegResult judegResult = new JudegResult();
	Setting setting = new Setting();

	//�޲ι��죬��ʼ��һЩ����
	public SoloPanel() {
		restoration();
		initData();
		setting.initSetting();
		
		//����ͼƬ
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
	 * ��������ʼ���������
	 */
	public void initPanel() {
		this.setLayout(null);
		pauseJButton = new JButton("pause");
		pauseJButton.setBounds(1000, 50, 100, 60);
		// this.add(pauseJButton);
	}

	public void paint(Graphics g) {
		super.paint(g);
		// ������ͼ
		g.drawImage(background, 0, 0, SoloMode.getWeight(), SoloMode.getHeight(), null);
		// �����Լ�
		g.drawImage(mySelfImg, mySelf.goldX, mySelf.goldY, mySelf.goldSize + 100, mySelf.goldSize, null);
		//�������н���
		for (int i = 0; i < goldSets.size(); i++) {
			GoldData golds = goldSets.get(i);
			g.drawImage(goldImg, golds.goldX, golds.goldY, golds.goldSize, golds.goldSize, null);
		}
		// ����2D����
		Graphics2D g2 = (Graphics2D) g;
		//���û�����ɫ
		g2.setColor(Color.BLACK);
		//����������ϸ
		g2.setStroke(new BasicStroke(3.0f));
		// ��ϸ��
		g2.drawLine(clawX0, clawY0, (int) clawX, (int) clawY);
		// ��צ��
		g.drawImage(clawImg, (int) clawX - 50, (int) clawY - 40, 100, 100, null);
		// ������������
		g.setFont(new Font("����", Font.BOLD, 25));
		// ���÷�
		g.drawString("�÷�:" + setting.getGrade(), 1000, 50);
		// ��Ŀ��÷�
		g.drawString("Ŀ��÷�:" + setting.getTargetScore(), 1000, 110);
		// ��ʱ��
		g.drawString("ʱ��:" + setting.getTime() / 1000, 20, 110);
		// ���ؿ�
		g.drawString("��" + setting.getMyClass() + "��", 20, 50);
	}

	/**
	 * 
	 * ��������ʼ������
	 */
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
		mySelf.goldSize = 100;
		// ��ʼ�����Լ���λ�ã������м�
		mySelf.goldX = (SoloMode.getWeight() / 2) - (mySelf.goldSize);
		mySelf.goldY = 50;
	}

	/**
	 * 
	 * �������ͷ�צ��
	 */
	public void releaseClaw() {
		// clawY = -k * clawX - b;
		
		if (clawX <= clawX0) {
			clawX -= 2;
			
		} else {
			clawX += 2;
		}
		// ֱ�߷���
		clawY = k * (clawX - clawX0) + clawY0;
	}
	/**
	 * 
	 * �������ջ�צ��
	 */
	public void withdrawClaw() {
		if (clawX <= clawX0) {
			clawX += stretchSpeed;
		}
		if (clawX > clawX0) {
			clawX -= stretchSpeed;
		}
		//ֱ�߷���
		clawY = k * (clawX - clawX0) + clawY0;
	}
	/**
	 * 
	 * �������ж�צ���Ƿ��ջسɹ�
	 * @return ���צ���ջسɹ�������true
	 */
	public boolean isWithdrawSucceed() {
		if (clawX >= clawX0 - 20 && clawX <= clawX0 + 20 && clawY <= clawY0 + 20) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * �������ָ�צ������
	 */
	public void restoration() {
		
		//��ʼ��λ������
		clawX = 600 - 20;
		clawY = 250 - 20;
		clawX0 = SoloMode.getWeight() / 2 - 20;
		clawY0 = 150 - 20;
		//��ʼ��צ��ץȡ�ٶ�
		clawSpeed = 1;
		//��ʼ��ץ����Ʒ֮��������ٶ�
		stretchSpeed = 3;
		//��ʼ��צ��Ϊ��ת״̬
		clawSpin = true;
		//��ʼ��צ������״̬Ϊ���쳤
		clawStretch = true;
		//��ʼ�����ջر�־λ
		backclaw = false;
		//��ʼ��ץ�����ӵı�־
		grabSucceed = false;
	}

	public void action() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				clawSpin = false;
				// ���б��k
				k = (clawY - clawY0) / (clawX - clawX0);
				b = clawY - (k * clawX);
			}

		});
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
				if (keyCode == KeyEvent.VK_SPACE) {
					clawSpin = false;
					// ���б��k
					k = (clawY - clawY0) / (clawX - clawX0);
				}
			}

		});
		//������ѭ��
		while (true) {
			// ��������ˣ�����һ��
			if(pass()) {
				nextClass();
			}
			if (gameOver()) {
				int result = JOptionPane.showConfirmDialog(null, "��Ϸʧ�ܣ�����һ�֣�", "��Ϸ���", JOptionPane.YES_NO_OPTION);
				if (result == 0) {// ����һ�䣬ˢ������
					restoration();
					initData();
					setting.initSetting();
					repaint();
				} else {// �˳�
					System.exit(0);
				}
			}
			
			
			//׼���׶Σ�צ�Ӽ����ڰ�Բ�����ƶ�
			if (clawSpin) {
				// Բ�ķ���
				clawY = (int) (Math
						.sqrt(10000 - ((clawX - (SoloMode.getWeight() / 2 - 20)) * (clawX - (SoloMode.getWeight() / 2 - 20)))))
						+ 150 - 20;
				//����x����仯
				clawX -= clawSpeed;
				//�����Ｋ��ʱ,�����ߣ�Ҳ����ֻ�߰�Բ
				if (clawX == 480) {
					clawSpeed *= -1;
				}
				if (clawX == 680) {
					clawSpeed *= -1;
				}
			} else { //צ�������׶�
				// ���Խ�磬���ջ�צ��
				if (clawY >= 800 || clawX <= 0 || clawX >= SoloMode.getWeight() + 100) {
					clawStretch = false;
					backclaw = true;
				}
				//�����������쳤ʱ
				if (clawStretch) {
					// �ͷ�צ��
					releaseClaw();
					// �ж���û��ץ������
					for (int i = 0; i < goldSets.size(); i++) {
						GoldData gold = goldSets.get(i);
						// ���צ����������
						if (clawX >= gold.goldX && clawX <= gold.goldX + gold.goldSize && clawY >= gold.goldY
								&& clawY <= gold.goldY + gold.goldSize) {
							// ��������־=�ջ�
							clawStretch = false;
							// ��¼��ץ���ӵ�����
							beGrab = i;
							// ץ����־��true
							grabSucceed = true;
							break;
						}
					}
				} else { //�������ջ�צ��ʱ
					// ���û��ץ�����������ջ�
					if (backclaw) {
						//�ջ�צ��
						withdrawClaw();
						// ���յ��Լ�λ��ʱ,��λ
						if (clawX >= clawX0 - 20 && clawX <= clawX0 + 20 && clawY <= clawY0 + 20) {
							restoration();
						}
					}
					// ���ץ�����ӣ����ջ�צ�ӣ���ץ���Ľ���
					if (grabSucceed) {
						// ���ץ�����Ǹ�����
						GoldData gold = goldSets.get(beGrab);
						// �����Ʒ̫�������ٶȱ���
						if (gold.goldSize >= 70) {
							stretchSpeed = 2;
						}
						if (gold.goldSize >= 120) {
							stretchSpeed = 1;
						}
						// �ջ�צ��
						withdrawClaw();
						// �ջ���������Ʒ
						gold.goldX = (int) clawX - 50;
						gold.goldY = (int) clawY;
						// ���յ��Լ�λ��ʱ�������˽���
						if (isWithdrawSucceed()) {
							goldSets.remove(beGrab);
							// ����צ��
							restoration();
							// ���ӵ÷�
							updateGrade(gold);
						}
					}
				}
			}
			
			//ˢ�¼��Ϊ20msˢ��һ��
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ˢ��ʱ��
			setting.setTime(setting.getTime() + 20);
			//ˢ�£����»�ͼ��
			repaint();
		}
	}
	// ��һ��
	public void nextClass() {
		restoration();
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

}
