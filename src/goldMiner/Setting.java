package goldMiner;

public class Setting {
	
	static int time; // ��Ϸʱ��
	static int deadline; // ��ֹʱ��
	static int targetScore; // Ŀ�����
	static int myClass; // �ؿ�
	static int grade; // ��ǰ����
	
	/**
	 * ��ʼ������
	 * ������
	 */
	public void initSetting() {
		
		targetScore = 500;
		myClass = 1;
	    time = 0;
		grade = 0;
		deadline = 60000;
		
	}
	// ����֮�������
	public void passSetting() {
		time = 0;
		grade = 0;
		deadline = 60000;
	}
	
	
	public int getTime() {
		return time;
	}


	public void setTime(int time) {
		Setting.time = time;
	}


	public int getTargetScore() {
		return targetScore;
	}


	public void setTargetScore(int targetScore) {
		Setting.targetScore = targetScore;
	}


	public int getMyClass() {
		return myClass;
	}


	public void setMyClass(int myClass) {
		Setting.myClass = myClass;
	}


	public int getGrade() {
		return grade;
	}


	public void setGrade(int grade) {
		Setting.grade = grade;
	}

	

	
}
