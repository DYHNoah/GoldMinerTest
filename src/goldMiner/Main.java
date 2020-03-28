package goldMiner;

import javax.swing.JFrame;

public class Main{
	private static int height = 800;
	private static int weight = 1200;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame jf = new JFrame();
		jf.setBounds(300, 100, Main.weight, Main.height);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MyPanel mp = new MyPanel();
		jf.add(mp);
		jf.setVisible(true);
		mp.action();
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		Main.height = height;
	}

	public static int getWeight() {
		return weight;
	}

	public static void setWeight(int weight) {
		Main.weight = weight;
	}

}