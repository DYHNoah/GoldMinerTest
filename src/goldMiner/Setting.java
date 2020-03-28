package goldMiner;

public class Setting {
	
	static int time; // 游戏时间
	static int deadline; // 截止时间
	static int targetScore; // 目标分数
	static int myClass; // 关卡
	static int grade; // 当前分数
	
	/**
	 * 初始化数据
	 * 描述：
	 */
	public void initSetting() {
		
		targetScore = 500;
		myClass = 1;
	    time = 0;
		grade = 0;
		deadline = 60000;
		
	}
	// 过关之后的设置
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
