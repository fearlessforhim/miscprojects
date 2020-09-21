/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

import java.util.Comparator;

/**
 *
 * @author Jonathan
 */
public class UniformCostComparator implements Comparator<StateNode> {

    @Override
    public int compare(StateNode a, StateNode b) {
        if (a.cost < b.cost) {
            return -1;
        }
        if (a.cost > b.cost) {
            return 1;
        }
        return 0;
    }
}
