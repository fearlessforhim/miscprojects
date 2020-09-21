/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

/**
 *
 * @author Jonathan
 */
public class AstarComparator implements java.util.Comparator<StateNode> {

    @Override
    public int compare(StateNode a, StateNode b) {
        if ((a.hVal + a.cost) < (b.hVal + b.cost)) {
            return -1;
        }
        if ((a.hVal + a.cost) > (b.hVal + b.cost)) {
            return 1;
        }
        return 0;
    }
}