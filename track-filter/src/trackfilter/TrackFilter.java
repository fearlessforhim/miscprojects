/*
 * Class: TrackFilter
 * Purpose: This class holds the algorithm and main computations
 *  of the particle filter problem. It will calculate the position
 *  of the robot based on the number of timesteps, standard deviation,
 *  and number of particles given by the user.
 */
package trackfilter;

import java.util.Random;

/**
 *
 * @author Jonathan
 */
public class TrackFilter {

    /**
     * @param args the command line arguments
     */
    public static enum Position {

        CENTER, RIGHT, LEFT
    };
    static Random rand = new Random();
    static Robot robot;
    static Particle[] particles;
    static double stdDev;

    public static void main(String[] args) {

//        int timesteps = Integer.parseInt(args[1]);
//        stdDev = Double.parseDouble(args[0]);
//        int particleNum = Integer.parseInt(args[2]);
//        particles = new Particle[particleNum];
//        robot = new Robot(0, 0, 0);
        
        int timesteps = 10;
        stdDev = 0.01;
        int particleNum = 100;
        particles = new Particle[particleNum];
        robot = new Robot(0, 0, 0);

        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(0.0, 0.0, 0.0, 0.0);
        }

        Position sensorRead;

        double[] tempArray;
        double avgY, posP, negP;
        for (int x = 0; x < timesteps; x++) {
            double initialIntent = robot.heading;
            sensorRead = udpateRobot(robot.heading, stdDev);
            tempArray = particleUpdate(sensorRead, robot.yPos);
            robot.heading = tempArray[0];
            avgY = tempArray[1];
            posP = tempArray[2];
            negP = tempArray[3];
            System.out.printf("Timestep: %d || Robot: (%f,%f,%fπ) || Intent after Movement: %fπ || Avg Y particle: %f ||"
                    + " + Particles: %f || - Particles: %f\n\n",
                    x + 1, robot.xPos, robot.yPos, initialIntent / Math.PI, robot.heading, avgY, posP, negP);
        }
    }
    
    /* function: updateRobot
     * params: (double)heading, (double)stdDev
     * purpose: this function updates the robot's position based on
     *  the heading determined by previous particle readings. It then
     *  gets a reading based on the y coordinate of the robot with errors
     *  considered.
     * returns: (Position enum)reading
     */
    public static Position udpateRobot(double heading, double stdDev) {
        Position reading;
        double randAdj = 0, newHeading = 0;
        randAdj = stdDev * normalRand();
        newHeading = heading + randAdj;
        robot.xPos += (10 * Math.cos(newHeading));
        robot.yPos += (10 * Math.sin(newHeading));
        robot.heading += newHeading;

        reading = sensorRead(robot.yPos);

        return reading;
    }

    /* function: particleUpdate
     * params: (Position enum)pos, (double)yCoord
     * purpose: this function modifies the particles of the simulation
     *  by moving the coordinates according to the robot movement model.
     *  It returns a new heading based on the particles average position,
     *  the average position, and number of positive and negative particles.
     * returns: (double[]){newHeading, avgY, posP, negP
     */
    static double[] particleUpdate(Position pos, double yCoord) {
        double newHeading = 0.0;
        double totalWeight = 0.0;

        for (Particle p : particles) {
            if (0 < yCoord && yCoord < 2) {
                switch (pos) {
                    case CENTER:
                        p.setW(p.getW() + (1 - (yCoord / 2)));
                        break;
                    case LEFT:
                        p.setW(p.getW() + (yCoord / 2));
                        break;
                    case RIGHT:
                        break;
                    default:
                        System.exit(1);
                }//end switch
            }//end if y is between 0, 2
            else if (-2 < yCoord && yCoord < 0) {
                switch (pos) {
                    case CENTER:
                        p.setW(p.getW() + (1 + (yCoord / 2)));
                        break;
                    case LEFT:
                        break;
                    case RIGHT:
                        p.setW(p.getW() - (yCoord / 2));
                        break;
                    default:
                        System.exit(1);
                }//end switch
            }//end else if y is between -2,0
            else if (yCoord > 2) {
                switch (pos) {
                    case CENTER:
                        break;
                    case LEFT:
                        p.setW(p.getW() + 1);
                        break;
                    case RIGHT:
                        break;
                    default:
                        System.exit(1);
                }//end switch
            }//end else if y is greater than 2
            else if (yCoord < -2) {//if y is less than -2
                switch (pos) {
                    case CENTER:
                        break;
                    case LEFT:
                        break;
                    case RIGHT:
                        p.setW(p.getW() + 1);
                        break;
                    default:
                        System.exit(1);
                }//end switch
            }//end else if y is less than -2

            totalWeight += p.getW();//increment total weight
        }

        //normalize weights
        for (Particle p : particles) {
            p.setW(p.getW() / totalWeight);
        }

        //resample particles according to weights

        //move particles according to movement model
        for (Particle p : particles) {
            p.setX(p.getX() + (10 * Math.cos(robot.heading + (stdDev*normalRand()))));
            p.setY(p.getY() + Math.sin(robot.heading + (stdDev*normalRand())));
        }

        //print particle summary

        //compute new heading according to particle summary and get +,- particle count
        double posP = 0, negP = 0;
        double totalY = 0, totalX = 0, avgY, avgX;
        for (Particle p : particles) {
            totalY += p.getY();
            totalX += p.getX();

            if (p.getY() > 0) {
                posP++;
            } else {
                negP--;
            }
        }
        avgY = totalY / particles.length;
        avgX = totalX / particles.length;
        newHeading = Math.sin(avgY / 10);

        newHeading = -newHeading;

        double[] returnArray = {newHeading, avgY, posP, negP};
        return returnArray;
    }
    
    /* function: normalRand
     * params: none
     * purpose: this function calculates a random value with a mean
     *  of 6 and a standard deviation of 1. The return value is used
     *  as noise in the robot's sensors
     * returns: (double)normalRand
     * 
     */
    public static double normalRand() {
        double normalRand = 0;
        for (int i = 0; i < 12; i++) {
            normalRand += rand.nextDouble();
        }

        if (rand.nextDouble() > .5) {
            normalRand = -normalRand;
        }

        return normalRand;
    }

    /*function: sensorRead
     * params: y, y coordinate of the robot
     * purpose: this funciton takes in the y value of the robot and
     *  gets a reading of LEFT, CENTER, or RIGHT, based on probabilities
     *  set down in the assignment requirements
     * returns: (Position)enum
     */
    static Position sensorRead(double y) {
        double yVal = y;
        double randNum = rand.nextDouble();

        if (0 < yVal && yVal < 1) {
            if (randNum > (1 - (yVal / 2))) {
                return Position.CENTER;
            } else {
                return Position.LEFT;
            }
        } else if (-1 < yVal && yVal < 0) {
            if (randNum < (-yVal / 2)) {
                return Position.RIGHT;
            } else {
                return Position.CENTER;
            }
        } else if (yVal < -1) {
            return Position.RIGHT;
        } else if (yVal > 1) {
            return Position.LEFT;
        }

        return null;
    }
}
