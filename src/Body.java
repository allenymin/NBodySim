import java.util.ArrayList;

public class Body {
	
	private static final double G = 6.673E3; //Modified gravitational constant for the purpose of accelerated simulation
	private static int bodyCounter = 0;
	private int bodyID;
	private double mass, radius;
	private Vector position, velocity, acceleration;
	
	public Body(double x, double y, double m, double r, Vector v) {
		this.position = new Vector(x, y);
		this.mass = m;
		this.radius = r;
		this.velocity = v;
		this.acceleration = new Vector(0,0);
		this.bodyID = bodyCounter++;
	}
	/*
	 * This function merges two bodies together while conserving momentum
	 */
	public void mergeBody(Body b) {
		Vector a = new Vector(this.velocity, this.mass);
		Vector c = new Vector(b.getVelocity(), b.getMass());
		a.addVector(c);
		this.mass += b.getMass();
		this.velocity = new Vector(a, (1/this.mass));
		this.radius++;
	}
	
	public double getMass() {
		return this.mass;
	}
	
	public Vector getPosition() {
		return this.position;
	}
	
	public double getRadius(){
		return this.radius;
	}
	
	public Vector getVelocity() {
		return this.velocity;
	}
	
	public Vector getAcceleration() {
		return this.acceleration;
	}
	
	public int getID() {
		return this.bodyID;
	}
	/*
	 * Calculates gravitational acceleration caused by a body b
	 */
	public Vector gravitationalAcceleration(Body b) {
		Vector my_pos = this.position;
		Vector their_pos = b.getPosition();
		double dx = their_pos.getX() - my_pos.getX();
		double dy = their_pos.getY() - my_pos.getY();
		Vector accel = new Vector(dx, dy);
		if (dx == 0 && dy == 0) return accel;
		double distance = accel.getMagnitude();
		double a = (G*b.getMass()) / (distance*distance);
		accel.setMagnitude(a);
		return accel;
	}
	/*
	 * Calculates total acceleration from all bodies in the list others on this body
	 */
	public Vector getAcceleration(ArrayList<Body> others) {
		Vector accel = new Vector(0,0);
		for (Body b : others) {
			if (this.bodyID != b.getID()) accel.addVector(gravitationalAcceleration(b));
		}
		return accel;
	}
	
	public void setAccel(Vector a) {
		acceleration = a;
	}
	
	public void addVelocity(Vector v) {
		velocity.addVector(v);
	}
	
	public void addPosition(Vector p) {
		position.addVector(p);
	}
	
}
