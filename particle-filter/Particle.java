/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particlefilter;

/**
 *
 * @author Jonathan
 */
public class Particle {

    private double p, w; //p:position, w:weight

    public double getP() {
        return p;
    }

    public double getW() {
        return w;
    }

    protected void setP(double p) {
        if (p > 4) {
            this.p = p - 4;
        } else {
            this.p = p;
        }
    }

    protected void setW(double w) {
        this.w = w;
    }
}
