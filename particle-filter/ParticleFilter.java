/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package particlefilter;

/**
 *
 * @author Jonathan
 */
import java.util.ArrayList;
import java.util.Random;

public class ParticleFilter {

    static ArrayList<Particle> S;//set of particles
    static double norm;//norm of particle weights
    static int d[] = {1, 0, 0, 0};//state of doors
    static Random rand = new Random();//random number generator

    /* function: main
     * purpose: this function runs the particle filter algorithm
     * params: (String[] args) command line arguments
     * returns: void
     */
    public static void main(String[] args) {

        init(10000);//initialize 1000 particles

        sense(true);
        move();
        sense(true);
        move();
        sense(false);
        printState();

    }//end method

    /* function: init
     * purpose: this function initializes the set of particles to
     *  be uniform across the 4 doors
     * params: (int numOfParticles) number of particles in filter
     * returns: void
     */
    public static void init(int numOfParticles) {
        norm = 0.0;
        S = new ArrayList<Particle>(numOfParticles);

        for (int i = 0; i < numOfParticles; i++) {
            double p = (double) i;
            Particle newParticle = new Particle();
            newParticle.setW(.5);
            newParticle.setP(4.0 * (p / numOfParticles));
            S.add(newParticle);
        }
    }

    /* function: sense
     * purpose: this function modifies the weights of each particle dending on whether
     *  or not the robot has 'sensed' a door
     * params: (boolean door) whether or not a door is sensed
     * return: void
     */
    public static void sense(boolean door) {
        norm = 0.0;
        if (door) {
            for (int x = 0; x < S.size(); x++) {
                if (0.0 <= S.get(x).getP() && S.get(x).getP() < 1.0) {
                    S.get(x).setW(S.get(x).getW() * (7.0 / 8.0));
                } else {
                    S.get(x).setW(S.get(x).getW() * (1.0 / 8.0));
                }
                norm += S.get(x).getW();
            }
            normalize();
        } else {
            for (int x = 0; x < S.size(); x++) {
                if (1.0 <= S.get(x).getP() && S.get(x).getP() < 4.0) {
                    S.get(x).setW(S.get(x).getW() * (7.0 / 8.0));
                } else {
                    S.get(x).setW(S.get(x).getW() * (1.0 / 8.0));
                }
                norm += S.get(x).getW();
            }
            normalize();
        }
    }

    
    /* function: move
     * purpose: this function moves the robot a random distance of 0.5 to 1 space
     * params: none
     * returns: void
     */
    public static void move() {
        double movement = rand.nextDouble();//get random move amount

        if (movement <= .5) {
            movement += .5;//if movement is less than 1/2, add 1/2 to keep within specified range
        }

        for (int x = 0; x < S.size(); x++) {
            S.get(x).setP(S.get(x).getP() + movement);
        }
    }

    /* function: normalize
     * purpose: this funnction normalizes all the weights in the particle set
     * params: none
     * returns: void
     */
    public static void normalize() {
        for (int i = 0; i < S.size(); i++) {
            S.get(i).setW(S.get(i).getW() / norm);
        }
    }

    
    /* function: printState
     * purpose: this function prints the weights of each bin
     * params: none
     * returns: void
     */
    public static void printState() {
        System.out.print("====================================================================================\n");
        double tempWeight = 0.0;
        for (double i = 0; i < 4; i += .1) {

            System.out.printf("%.1f", i);
            for (int j = 0; j < S.size(); j++) {
                double normP = S.get(j).getP() * 100;

                normP = Math.floor(normP);

                normP = normP / 100;

                if (i < normP && normP < i + .1) {
                    tempWeight += S.get(j).getW();
                }
            }
            double w = 0.0;
            while (w < tempWeight) {
                System.out.print("x");
                w += .005;
            }
            tempWeight = 0.0;
            System.out.println();
        }
        System.out.print("\n====================================================================================");

    }
}
