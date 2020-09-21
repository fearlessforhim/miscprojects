/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

import java.util.Comparator;

public class GreedyComparator implements Comparator<StateNode>{
    
    @Override
    public int compare(StateNode a, StateNode b)
    {
        
        if (a.hVal < b.hVal)
        {
            return -1;
        }
        if (a.hVal > b.hVal)
        {
            return 1;
        }
        return 0;
    }
}