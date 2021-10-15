package goldMiner;

/**
 * @����������һ��������
 *
 */
public class GoldData {
	int goldX;
	int goldY;
	int goldSize;
	int dir;
	int moveSpeed;
	int grade;

	/**
	 *  ��������
	 * 1: 50-80
	 * 2: 80-110
	 * 3: 110-140
	 * 4: ��ʯ
	 * 5: ʯͷ
	 */
	int kind;
	
	public GoldData() {
		this.goldX = (int)(Math.random()*1200 + 1);
		this.goldY = (int)(Math.random()*500 + 200);
		this.dir = (int)(Math.random()*2 + 1);
		this.moveSpeed = 5;
		this.grade = 0;
		this.kind = (int)(Math.random()*5 + 1);
		setGoldSize();
	}
	// ���ý��Ӵ�С
	public void setGoldSize() {
		if (this.kind == 1) {
			this.goldSize = (int)(Math.random() * 30 + 50);
		}
		if (this.kind == 2) {
			this.goldSize = (int)(Math.random() * 30 + 80);
		}
		if (this.kind == 3) {
			this.goldSize = (int)(Math.random() * 30 + 110);
		}
		if (this.kind == 4) {
			this.goldSize = 30;
		}
		if (this.kind == 5) {
			this.goldSize = (int)(Math.random() * 30 + 30);
		}
	}
}
