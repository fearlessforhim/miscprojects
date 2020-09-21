/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

/**
 *
 * @author Jonathan
 */
public class CardConverter {
    
    public Card convert(String[] data){
        String suit;
        String sValue;
        Integer iValue = 0;
        
        suit = data[2];
        sValue = data[1];
        
        try{
            iValue = Integer.parseInt(sValue);
            
        }catch(Exception e){
            if(sValue.equalsIgnoreCase("J")){
                iValue = 11;//numberic value of Jack
            }else
                if(sValue.equalsIgnoreCase("Q")){
                    iValue = 12;//numeric value of queen
                }else
                    if(sValue.equalsIgnoreCase("K")){
                        iValue = 13;//numeric value of King
                    }else
                        if(sValue.equalsIgnoreCase("T")){
                            iValue = 10;
                        }else
                            if(sValue.equalsIgnoreCase("A")){
                                iValue = 1;
                            }
        }
        
        Card newCard = new Card(suit, iValue, sValue);
        return newCard;
    }
    
}
