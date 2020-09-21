/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

import java.util.ArrayList;

/**
 *
 * @author Jonathan
 */
public class Printer {

    int distFromGoal = 0;
    
    public void printState(StateNode state, int distFromGoal) {

        System.out.printf("\nState\nDistance from goal state:%d\n|", distFromGoal);


        /*print out free cells*/
        int remSpaces = 4;
        for (Card freeCard : state.freeCells) {
            if (freeCard.isRed) {
                System.out.printf("\u001B31;1m%s%s|", freeCard.sVal, freeCard.suit);
            } else {
                System.out.printf("%s%s|", freeCard.sVal, freeCard.suit);
            }
            remSpaces--;
        }
        while (remSpaces > 0) {
            System.out.print(" |");
            remSpaces--;
        }

        System.out.print("   |");

        /*print out top foundation cards*/
        remSpaces = 4;
        for (ArrayList<Card> f : state.foundation) {
            if (f.get(f.size() - 1).isRed) {
                System.out.printf("\u001B31;1m%s%s|", f.get(f.size() - 1).sVal, f.get(f.size() - 1).suit);
            } else {
                System.out.printf("%s%s|", f.get(f.size() - 1).sVal, f.get(f.size() - 1).suit);
            }
            remSpaces--;
        }

        while (remSpaces > 0) {
            System.out.print(" |");
            remSpaces--;
        }

        System.out.printf("\n\n");

        //find largest cascade
        int largeCasc = 0;
        for (ArrayList<Card> c : state.casc) {
            if (c.size() > largeCasc) {
                largeCasc = c.size();
            }
        }

        for (int x = 0; x <= largeCasc; x++) {
            System.out.print("|");
            for (int y = 0; y < state.casc.size(); y++) {

                try {
                    Card currCard = state.casc.get(y).get(x);
                    if (currCard.isRed) {
                        System.out.printf("\u001B31;1m%s%s|", currCard.sVal, currCard.suit);
                    } else {
                        System.out.printf("%s%s|", currCard.sVal, currCard.suit);
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.print("  |");
                }
            }
            System.out.printf("\n\n");
        }
    }

    public void printDistance(StateNode goalNode) {
        StateNode currentNode = goalNode;
        int dist = 0;

        while (currentNode.prevState != null) {
            dist++;
            currentNode = currentNode.prevState;
        }

        System.out.printf("Number of moves to goal:%d\n", dist);
    }

    public int printActions(StateNode goalNode, int distFromGoal) {
        if (goalNode.prevState != null) {
            printActions(goalNode.prevState, ++distFromGoal);
        }else{
            return distFromGoal;
        }
        System.out.printf("Move %s\n", goalNode.move);
        printState(goalNode, distFromGoal);
        return distFromGoal;
    }

    public void printStateSequence(StateNode goalNode) {
        StateNode currentNode = goalNode;
        int dist = 0;

        while (goalNode != null) {
            printState(goalNode, dist);
            goalNode = goalNode.prevState;
            dist++;
        }

    }
}
