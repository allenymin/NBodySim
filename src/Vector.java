
/*
 * Vector class to store information about 2-d vectors
 */
public class Vector {
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(Vector v, double scalar) {
		this.x = v.getX() * scalar;
		this.y = v.getY() * scalar;
	}
	
	public double getMagnitude() {
		return Math.sqrt(x*x + y*y);
	}
	
	public double getDirection() {
		double angle = Math.atan(y/x);
		if (x < 0) return angle + Math.PI;
		else return angle;
	}
	
	public void setMagnitude(double m) {
		double x, y;
		x = Math.cos(getDirection())*m;
		y = Math.sin(getDirection())*m;
		this.x = x;
		this.y = y;
	}
	
	public void setDirection(double d) {
		double x, y;
		x = Math.cos(d)*getMagnitude();
		y = Math.sin(d)*getMagnitude();
		this.x = x;
		this.y = y;
	}
	
	public void setX(double nx) {
		this.x = nx;
	}
	
	public void setY(double ny) {
		this.y = ny;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void addVector(Vector v) {
		x += v.getX();
		y += v.getY();
	}
}
