package goldMiner;

public class JudegResult {
	
	Setting setting = new Setting();
	
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
		for (int i = 0; i < MyPanel.goldSets.size(); i++) {
			if (MyPanel.goldSets.get(i) != null) {
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
}
