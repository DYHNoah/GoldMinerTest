package goldMiner;

import javax.swing.JFrame;

public class SoloMode {
	private static int height = 800;
	private static int weight = 1200;

	public static void main(String[]  args) {
		new SoloMode().start();
	}

	public void start(){
		// TODO Auto-generated method stub
		JFrame jf = new JFrame();
		jf.setLayout(null);
		jf.setBounds(300, 100, SoloMode.weight, SoloMode.height);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SoloPanel mp = new SoloPanel();

		jf.setContentPane(mp);
		jf.setVisible(true);
		mp.action();

	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		SoloMode.height = height;
	}

	public static int getWeight() {
		return weight;
	}

	public static void setWeight(int weight) {
		SoloMode.weight = weight;
	}

}