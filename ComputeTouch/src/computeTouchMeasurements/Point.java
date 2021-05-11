package computeTouchMeasurements;

public class Point {
    
    public int X;    
    public int Y;    
    public double T;
    
    public Point(int x, int y) {
    	X = x;
    	Y = y;
    }
    
    public Point(int x, int y, double t) {
    	X = x;
    	Y = y;
    	T = t;
    }
    
    public final String ToString() {
        return String.format("X={0} Y={1} T={2}", this.X, this.Y, this.T);
    }
}