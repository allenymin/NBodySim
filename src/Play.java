

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

public class Play extends JFrame {
	private Sim sim;
	public JLabel numBody;
	public JLabel mass;
	JRadioButton euler;
	JRadioButton rk4;
	
	private void initGUI(){
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
		sidePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		numBody = new JLabel();
		numBody.setText("Number of bodies: 0");
		sidePanel.add(numBody);
		mass = new JLabel();
		mass.setText("Mass of body under cursor:      ");
		sidePanel.add(mass);
		euler = new JRadioButton("Euler Integration");
		rk4 = new JRadioButton("Runge Kutta 4 Integration");
		rk4.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(euler);
		group.add(rk4);
		sidePanel.add(euler);
		sidePanel.add(rk4);
		add(sidePanel, BorderLayout.LINE_END);
		add(sim);
		setTitle("NBody Simulator");
        setSize(800, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void update() {
		numBody.setText("Number of bodies: "+sim.getNumBody());
		mass.setText("Mass of body under cursor: "+sim.getMassOfCursor());
		repaint();
	}
	
	public Play(Sim s) {
		sim = s;
		initGUI();
	}
	
	/*
	 * Main method that holds the heart of the simulation
	 */
	public static void main (String[] s)
	{
		double dt = 1.000/60;
		Sim sim = new Sim(dt);
		Play play = new Play(sim);
		play.setVisible(true);
		double currTime, nextTime;
		while (true) {
			nextTime = (double) (System.nanoTime() / 1000000000.0) + dt;
			if (play.euler.isSelected()) sim.update(1);
			else sim.update(2);
			play.update();
			currTime = (double) System.nanoTime() / 1000000000.0;
			if (currTime < nextTime) {
				long sleepTime = (long) ((nextTime - currTime) * 1000);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			} else {
				System.out.println("slow frame");
			}
		}
	}
	
	
}
