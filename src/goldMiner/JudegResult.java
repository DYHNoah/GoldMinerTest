package goldMiner;

public class JudegResult {
	
	Setting setting = new Setting();
	
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
		for (int i = 0; i < MyPanel.goldSets.size(); i++) {
			if (MyPanel.goldSets.get(i) != null) {
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
}
