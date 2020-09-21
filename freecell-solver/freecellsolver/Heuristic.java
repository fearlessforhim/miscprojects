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
public class Heuristic {

    int tempHVal;
    int topHeartInFound = 0;
    int topDiamondInFound = 0;
    int topSpadeInFound = 0;
    int topClubInFound = 0;

    public void setHVal(StateNode state) {
        state.hVal = 0;
        topHeartInFound = 0;
        topDiamondInFound = 0;
        topSpadeInFound = 0;
        topClubInFound = 0;
        
        //for each foundation, get the top card value for later heuristic calculations
        for (ArrayList<Card> f : state.foundation) {
            if (f.get(0).suit.equals("H")) {
                topHeartInFound = f.get(f.size() - 1).iVal;
            } else if (f.get(0).suit.equals("D")) {
                topDiamondInFound = f.get(f.size() - 1).iVal;
            } else if (f.get(0).suit.equals("S")) {
                topSpadeInFound = f.get(f.size() - 1).iVal;
            } else if (f.get(0).suit.equals("C")) {
                topClubInFound = f.get(f.size() - 1).iVal;
            }
        }

        /*for each card in freeCells, apply a heuristic of the difference between
         * this cards value and the top card value in the corresponding suit foundation
         */
        for (Card c : state.freeCells) {
            c.cardHVal = 0;
            if (c.suit.equals("H")) {
                c.cardHVal += c.iVal - topHeartInFound;
            } else if (c.suit.equals("D")) {
                c.cardHVal += c.iVal - topDiamondInFound;
            } else if (c.suit.equals("S")) {
                c.cardHVal += c.iVal - topSpadeInFound;
            } else if (c.suit.equals("C")) {
                c.cardHVal += c.iVal - topClubInFound;
            }
            state.hVal += c.cardHVal;
        }
        
        /*for each card in the cascades, apply a heuristic of the number of out of order
         * cards covering it, then add to it the difference between the current cards value
         * and the top card value in the corresponding suit foundation
         */
        for (int x = 0; x < state.casc.size(); x++) {
            for (int y = state.casc.get(x).size() - 1; y >= 0; y--) {
                Card currentCard = state.casc.get(x).get(y);
                currentCard.cardHVal = 0;

                if (y == state.casc.get(x).size() - 1) {//if current card is at bottom of cascade

                    if (currentCard.suit.equalsIgnoreCase("H")) {
                        currentCard.cardHVal = currentCard.iVal - topHeartInFound;
                    } else if (currentCard.suit.equalsIgnoreCase("D")) {
                        currentCard.cardHVal = currentCard.iVal - topDiamondInFound;
                    } else if (currentCard.suit.equalsIgnoreCase("S")) {
                        currentCard.cardHVal = currentCard.iVal - topSpadeInFound;
                    } else if (currentCard.suit.equalsIgnoreCase("C")) {
                        currentCard.cardHVal = currentCard.iVal - topClubInFound;
                    }
                } else {//current card is covered by one or more cards
                    Card coveringCard = state.casc.get(x).get(y + 1);

                    /*if covering card is opposite color of current card and value is one less than current card*/
                    if ((currentCard.isRed && !coveringCard.isRed) && (currentCard.iVal - 1 == coveringCard.iVal)) {
                        currentCard.cardHVal = 0;
                    } else {
                        currentCard.cardHVal = coveringCard.cardHVal + 1;
                        
                        if (currentCard.suit.equalsIgnoreCase("H")) {
                            currentCard.cardHVal += currentCard.iVal - topHeartInFound;
                        } else if (currentCard.suit.equalsIgnoreCase("D")) {
                            currentCard.cardHVal += currentCard.iVal - topDiamondInFound;
                        } else if (currentCard.suit.equalsIgnoreCase("S")) {
                            currentCard.cardHVal += currentCard.iVal - topSpadeInFound;
                        } else if (currentCard.suit.equalsIgnoreCase("C")) {
                            currentCard.cardHVal += currentCard.iVal - topClubInFound;
                        }//end if else
                    }//end else
                }//end else
                state.hVal += currentCard.cardHVal;
            }//end for y
        }//end for x
    }//end method
}//end class
