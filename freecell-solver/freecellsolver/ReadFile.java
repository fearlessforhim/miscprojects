/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

/**
 *
 * @author Jonathan
 */
import java.io.*;
import java.util.*;

public class ReadFile {

    public DataBean readFile(String fileName) {
        DataBean data = new DataBean();
        StateNode initState = null;
        StateNode goalState = null;
        ArrayList<ArrayList<Card>> cascades = new ArrayList<ArrayList<Card>>();
        Heuristic h = new Heuristic();

        try {
            File txtFile = new File(fileName);
            BufferedReader buff = new BufferedReader(new FileReader(txtFile));

            String line;

            line = buff.readLine();
            data.alg = Integer.parseInt(line);

            line = buff.readLine();
            data.heur = Integer.parseInt(line);

            line = buff.readLine();
            data.outputLev = Integer.parseInt(line);

            CardConverter conv = new CardConverter();

            String[] lineArray;
            line = buff.readLine();
            /*read in cascades*/
            while (line != null) {
                lineArray = line.split(" ");//get array from current line
                ArrayList<Card> tempArrayList = new ArrayList<Card>();
                for (int x = 0; x < lineArray.length; x++) {//for every string value in array
                    /*create a card*/
                    Card newCard = conv.convert(lineArray[x].split(""));//called method will parse string and create card
                    tempArrayList.add(newCard);
                }
                cascades.add(tempArrayList);
                line = buff.readLine();
            }
            
            while(cascades.size()<8){
                cascades.add(new ArrayList<Card>());
            }

            data.initState = new StateNode(new ArrayList<ArrayList<Card>>(), new ArrayList<Card>(), cascades, null, 0);
            h.setHVal(data.initState);

        } catch (Exception e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
        
//        data = new DataBean();
//        data.alg = 5;
//        data.outputLev = 3;
//        data.heur = 2;
//        
//        data.initState.freeCells.add(new Card("H", 6, "6"));
//        data.initState.freeCells.add(new Card("D", 6, "6"));
//        
//        ArrayList<Card> temp = new ArrayList<Card>();
//        temp.add(new Card("S", 5, "5"));
//        data.initState.foundation.add(temp);
//        temp = new ArrayList<Card>();
//        temp.add(new Card("H", 5, "5"));
//        data.initState.foundation.add(temp);
//        temp = new ArrayList<Card>();
//        temp.add(new Card("C", 6, "6"));
//        data.initState.foundation.add(temp);
//        temp = new ArrayList<Card>();
//        temp.add(new Card("D", 4, "4"));
//        data.initState.foundation.add(temp);
//        
//        temp = new ArrayList<Card>();
//        temp.add(new Card("S", 6, "6"));
//        data.initState.casc.add(temp);
//        temp = new ArrayList<Card>();
//        temp.add(new Card("D", 5, "5"));
//        data.initState.casc.add(temp);
//        h.setHVal(data.initState);
        return data;
    }
}
