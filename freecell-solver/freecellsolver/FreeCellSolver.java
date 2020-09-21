/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

/**
 *
 * @author Jonathan
 */
public class FreeCellSolver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // read file
        ReadFile rf = new ReadFile();
        DataBean db = rf.readFile(args[0]);
        Problem problemClass = new Problem(db);
        //pass in data from file to problem class
        System.out.println("Finished");
    }
}
