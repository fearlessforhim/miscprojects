/*
 * Class: Robot
 * Purpose: This class hold the information for the robot
 *  in the algorithm
 */
package trackfilter;

/**
 *
 * @author Jonathan
 */
public class Robot {
    double xPos = 0;
    double yPos = 0;
    double heading = 0.0;
    
    public Robot(int x, int y, int h){
        xPos = x;
        yPos = y;
        heading = h;
    }
}
