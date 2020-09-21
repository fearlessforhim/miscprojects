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
public class StateNode {

    ArrayList<ArrayList<Card>> foundation = new ArrayList<ArrayList<Card>>(0);
    ArrayList<Card> freeCells = new ArrayList<Card>();
    ArrayList<ArrayList<Card>> casc = new ArrayList<ArrayList<Card>>(8);
    Integer hVal = 0;
    StateNode prevState;
    Integer cost = 0;
    String key = "";
    String move;

    public StateNode(ArrayList<ArrayList<Card>> newFound,
            ArrayList<Card> newCells,
            ArrayList<ArrayList<Card>> newCascades,
            StateNode newPreviousState, int newCost) {

        foundation = newFound;
        freeCells = newCells;
        casc = newCascades;
        prevState = newPreviousState;
        cost = newCost;

        createHashKey();
    }

    public StateNode() {
        //empty method
    }

    public void createHashKey() {

        key = "|";

        for (ArrayList<Card> f : foundation) {
            for (Card card : f) {
                key += (card.sVal + card.suit);
            }
            key += "|";
        }

        key += "|";
        for (Card card : freeCells) {
            key += (card.sVal + card.suit);
            key += "|";
        }

        key += "|";
        for (ArrayList<Card> cc : casc) {
            for (Card card : cc) {
                key += (card.sVal + card.suit);
            }
            key += "|";
        }
    }
}