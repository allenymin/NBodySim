

import java.awt.Dimension;
import java.util.ArrayList;

public class Space {
	/*
	 * Small struct just to store information about the collision animation
	 */
	public class Collision {
		public int frame;
		public Vector position;
		
		public Collision(Vector p) {
			position = new Vector(p, 1);
			frame = 0;
		}
	}
	
	private ArrayList<Body> bodies;
	private ArrayList<Collision> collisions;
	Dimension window;
	
	public Space(Dimension w)
	{
		bodies = new ArrayList<Body>();
		collisions = new ArrayList<Collision>();
		window = w;
	}
	
	public void setWindow(Dimension d) {
		window = d;
	}
	
	public void addBody(Body b)
	{
		bodies.add(b);
	}
	
	public void removeBody(int index)
	{
		bodies.remove(index);
	}
	
	public ArrayList<Body> getBodies()
	{
		return bodies;
	}
	
	public ArrayList<Collision> getCollisionAnims() {
		return collisions;
	}
	
	public void doWallCollision() {
		for (int i = 0; i < bodies.size(); i++) {
			Body b = bodies.get(i);
			double x = b.getPosition().getX();
			double y = b.getPosition().getY();
			Vector v = b.getVelocity();
			if (x < b.getRadius()) v.setX(Math.abs(v.getX()));
			if (y < b.getRadius()) v.setY(Math.abs(v.getY()));
			if (x > window.getWidth()-b.getRadius()) v.setX(-Math.abs(v.getX()));
			if (y > window.getHeight()-b.getRadius()) v.setY(-Math.abs(v.getY()));
		}
	}
	
	public void doBodyCollision() {
		ArrayList<Body> removeList = new ArrayList<Body>();
		for (int i = 0; i < bodies.size()-1; i++) {
			Body b = bodies.get(i);
			for (int j = i+1; j < bodies.size(); j++) {
				Body bo = bodies.get(j);
				if (collide(b, bo)) {
					if (b.getMass() >= bo.getMass()) {
						b.mergeBody(bo);
						collisions.add(new Collision(bo.getPosition()));
						removeList.add(bo);
					} else {
						bo.mergeBody(b);
						collisions.add(new Collision(b.getPosition()));
						removeList.add(b);
					}
				}
			}
		}
		bodies.removeAll(removeList);
	}
	
	public boolean collide(Body b1, Body b2) {
		Vector p1 = b1.getPosition();
		Vector p2 = b2.getPosition();
		double dist = Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
		if (dist < (b1.getRadius() + b2.getRadius())) return true;
		return false;
	}
	
	public void doCollision() {
		doWallCollision();
		doBodyCollision();
	}
	
	public void updateColAnim() {
		for (int i = collisions.size()-1; i >= 0; i--) {
			collisions.get(i).frame++;
			if (collisions.get(i).frame > 40) collisions.remove(i);
		}
	}
	
	public void updateAccel(ArrayList<Body> bds) {
		for (int i = 0; i < bds.size(); i++) {
			Body b = bds.get(i);
			Vector accel = new Vector(0,0);
			for (int j = 0; j < bds.size(); j++) {
				Body bo = bds.get(j);
				if (i != j) {
					accel.addVector(b.gravitationalAcceleration(bo));
				}
			}
			b.setAccel(accel);
		}
	}
	
	public void updateVelocity(double dt) {
		for (int i = 0; i < bodies.size(); i++) {
			Body b = bodies.get(i);
			b.addVelocity(new Vector(b.getAcceleration(), dt));
		}
	}
	
	public void updatePosition(double dt) {
		for (int i = 0; i < bodies.size(); i++) {
			Body b = bodies.get(i);
			b.addPosition(new Vector(b.getVelocity(), dt));
		}
	}

	public void updateEuler(double dt) {
		//Simple Euler integration
		updateColAnim();
		doCollision();
		updatePosition(dt);
		updateVelocity(dt);
		updateAccel(bodies);
	}

	public void integrate(ArrayList<Body> x, ArrayList<Body> y, double dt) {
		for (int i = 0; i < x.size(); i++) {
			Body z = x.get(i);
			z.addPosition(new Vector(y.get(i).getVelocity(), dt));
			z.addVelocity(new Vector(y.get(i).getAcceleration(), dt));
		}
	}
	
	public void updateRK4(double dt) {
		updateColAnim();
		doCollision();
		//assume bodies accels are updated for the current state
		//We have a. it is the accels and velocities on bodies at the current state
		//To get b we integrate half a time step ahead using Euler integration
		
		ArrayList<Body> a = new ArrayList<Body>(bodies);
		updateAccel(a);
		ArrayList<Body> b = new ArrayList<Body>(bodies);
		integrate(b, a, dt*0.5);
		updateAccel(b);
		//Now we have b, need to get c.
		ArrayList<Body> c = new ArrayList<Body>(bodies);
		integrate(c, b, dt*0.5);
		updateAccel(c);
		//Now we have c, need to get d.
		ArrayList<Body> d = new ArrayList<Body>(bodies);
		integrate(d, c, dt);
		updateAccel(d);
		//Now we have d, need to update whole list now.
		for (int i = 0; i < bodies.size(); i++) {
			Vector vel = new Vector(a.get(i).getVelocity(), 1.0/6.0);
			vel.addVector(new Vector(b.get(i).getVelocity(), 1.0/3.0));
			vel.addVector(new Vector(c.get(i).getVelocity(), 1.0/3.0));
			vel.addVector(new Vector(d.get(i).getVelocity(), 1.0/6.0));
			bodies.get(i).addPosition(new Vector(vel, dt));
			
			Vector accel = new Vector(a.get(i).getAcceleration(), 1.0/6.0);
			vel.addVector(new Vector(b.get(i).getAcceleration(), 1.0/3.0));
			vel.addVector(new Vector(c.get(i).getAcceleration(), 1.0/3.0));
			vel.addVector(new Vector(d.get(i).getAcceleration(), 1.0/6.0));
			bodies.get(i).addVelocity(new Vector(accel, dt));
		}
		
	}
	
}
