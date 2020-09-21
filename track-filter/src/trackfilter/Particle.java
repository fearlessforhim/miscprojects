/*
 * Class: Particle
 * Purpose: This class holds information about each
 *  particle in the algorithm
 */
package trackfilter;

/**
 *
 * @author Jonathan
 */
public class Particle {

    private double x, y, heading, w; //p:position, w:weight
    
    public Particle(double x, double y, double h, double w){
        this.x = x;
        this.y = y;
        this.heading = h;
        this.w = w;
    }

    public double getW() {
        return w;
    }

    protected void setW(double w) {
        this.w = w;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the heading
     */
    public double getHeading() {
        return heading;
    }

    /**
     * @param heading the heading to set
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }
}
