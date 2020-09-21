/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

/**
 *
 * @author Jonathan
 */
public class Card implements Cloneable {

    String suit;
    Integer iVal;
    String sVal;
    boolean isRed;
    int cardHVal;

    public Card(String s, Integer iV, String sV) {
        suit = s;
        iVal = iV;
        sVal = sV;

        if (sV.equalsIgnoreCase("H") || sV.equalsIgnoreCase("D")) {
            isRed = true;
        }
    }

    @Override
    public Card clone() {
        Card c = new Card(suit, iVal, sVal);
        return c;
    }
}
