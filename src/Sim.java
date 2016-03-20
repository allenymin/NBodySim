

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Sim  extends JPanel implements MouseMotionListener, MouseListener{
	
	private Space space;
	double dt;
	private int mx, my;
	private int cmx, cmy;
	boolean mouse;

	public Sim(double delta){
		mouse = false;
		this.dt = delta;
		space = new Space(this.getSize());
		addMouseListener(this);
		addMouseMotionListener(this);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	/*
	 * Update method called each frame. Depending on the button selection, Euler integeration
	 * or Runge Kutta 4 integration is used to update
	 */
	public void update(int t) {
		space.setWindow(this.getSize());
		if (t == 1) space.updateEuler(dt);
		else space.updateRK4(dt);
		repaint();
	}

    public void draw(Graphics g)
    {
    	Graphics2D g2d = (Graphics2D) g;
    	ArrayList<Body> bodies = space.getBodies();
    	Color m = new Color(200, 200, 200, 150);
		g2d.setColor(m);
    	for (Space.Collision col : space.getCollisionAnims()) {
    		int radius = col.frame * 2;
    		if (radius > 50) radius = 50;
    		g2d.fillOval((int) (col.position.getX()-radius), (int) (col.position.getY()-radius), radius * 2, radius * 2);
    	}
    	for (Body x : bodies)
    	{
    		int red = 0;
    		int green = 255;
    		int mass = (int) x.getMass();
    		if (mass <= 255) red += mass;
    		else {
    			red = 255;
    			mass -= 255;
    			green -= mass;
    			if (green < 0) green = 0;
    		}
    		g2d.setColor(new Color(red, green, 0));
    		double r = x.getRadius();
    		double px = x.getPosition().getX()-r;
    		double py = x.getPosition().getY()-r;
    		g2d.fillOval((int)px, (int)py, (int)(r*2), (int)(r*2));
    		
    	}
    	if (mouse) {
    		g.setColor(Color.BLACK);
    		g.drawLine(mx, my, cmx, cmy);
    	}
    }

	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		cmx = mx;
		cmy = my;
		mouse = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Vector v = new Vector(mx-e.getX(), my-e.getY());
		Body b = new Body(e.getX(), e.getY(), 5, 5, v);
		space.addBody(b);
		mouse = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		cmx = e.getX();
		cmy = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		cmx = e.getX();
		cmy = e.getY();
	}
	
	public int getNumBody() {
		return space.getBodies().size();
	}
	
	public String getMassOfCursor() {
		for (int i = 0; i < space.getBodies().size(); i++) {
			Body b = space.getBodies().get(i);
			double dx = cmx - b.getPosition().getX();
			double dy = cmy - b.getPosition().getY();
			double dist = Math.sqrt(dx*dx + dy*dy);
			if (dist <= b.getRadius()) return Double.toString(b.getMass());
		}
		return "     ";
	}

}
