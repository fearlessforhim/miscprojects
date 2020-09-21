/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package freecellsolver;

/**
 *
 * @author Jonathan
 */
import java.util.*;

public class Problem {

    Queue<StateNode> frontier;//this Queue will either stay a queue or become a priority queue depending on the algorithm chosen
    Queue<StateNode> explored;//this Queue will store all visited nodes, it is used to check for goal state
    HashMap<String, Integer> exploredHash = new HashMap<String, Integer>();//this hash is used to quickly track what has been explored
    HashMap<String, Integer> frontierHash = new HashMap<String, Integer>();//this hash is used to quickly track what is scheduled to be visited
    StateNode reachedGoal;//when an algorithm has found the goal, that state will be added to this state
    Heuristic h = new Heuristic();//Parent heuristic class
    Printer p = new Printer();//printer class
    Long startTime, algTime, totalTime;//Long values used for tracking time

    public Problem(DataBean newDB) {
        startTime = System.currentTimeMillis();

        switch (newDB.alg) {//determine algorithm to run
            case 1:
                reachedGoal = DepthFirstSearch(newDB.initState);
                break;
            case 2:
                reachedGoal = BreadthFirstSearch(newDB.initState);
                break;
            case 3:
                reachedGoal = UniformCostSearch(newDB.initState);
                break;
            case 4:
                reachedGoal = GreedyBestFirstSearch(newDB.initState);
                break;
            case 5:
                reachedGoal = ASearch(newDB.initState);
                break;
        }

        algTime = System.currentTimeMillis() - startTime;

        switch (newDB.outputLev) {//determine output level
            case 1:
                p.printDistance(reachedGoal);
                break;
            case 2:
                p.printActions(reachedGoal, -1);
                break;
            case 3:
                p.printStateSequence(reachedGoal);
                break;
        }

        totalTime = System.currentTimeMillis() - startTime;

        System.out.printf("Time to run algorithm: " + algTime + " milliseconds\n");
        System.out.printf("Time to execute Free Cell Solver: " + totalTime + " milliseconds\n");
    }

    /*
     * for freecell, there are very few unsolvable puzzles,
     * so we assume the current puzzle is solvable.
     * Therefore, we omit any method that determines if
     * the goal state is reachable
     */
    public void addToFrontier(StateNode currentState) {

        if (!frontierHash.containsKey(currentState.key) && !exploredHash.containsKey(currentState.key)) {
            frontier.add(currentState);
            frontierHash.put(currentState.key, currentState.hVal);
        }//end if
    }//end addToFrontier

    public void removeFromFrontier(StateNode visitedState) {

        frontier.remove(visitedState);//remove the node from the frontier
        frontierHash.remove(visitedState.key);//remove from frontierHash
        explored.add(visitedState);//add the node to explored
        exploredHash.put(visitedState.key, visitedState.hVal);//add to exploredHash
    }

    public ArrayList<StateNode> checkAdjacents(StateNode currState) {

        StateNode adjVal;//new StateNode of values to modify
        ArrayList<StateNode> adjValArrayList = new ArrayList<StateNode>();//list of adjacent states

        /*
         * search through current cascades for possible moves
         */
        adjVal = cloneState(currState);
        for (int y = 0; y < adjVal.casc.size(); y++) {//for each cascade
            if (!adjVal.casc.get(y).isEmpty()) {//if cascade is non-empty
                Card lastInCurrCasc = adjVal.casc.get(y).get(adjVal.casc.get(y).size() - 1);//get last card in current cascade

                /*
                 * first, for currentState, add adjValues for moves within cascades
                 */
                for (int x = 0; x < adjVal.casc.size(); x++) {//for each remaining cascade
                    if (x != y) {//if cascades are not the same
                        if (!adjVal.casc.get(x).isEmpty()) {//if dest cascade is non-empty
                            Card lastInDestCasc = adjVal.casc.get(x).get(adjVal.casc.get(x).size() - 1);//get last card in dest cascade

                            if (validCascMove(lastInCurrCasc, lastInDestCasc)) {//if moving this card to destination is a valid move
                                moveCascToCasc(adjVal, y, x);//move card, create new adjacent value from new state
                                h.setHVal(adjVal);
                                adjVal.createHashKey();
                                adjValArrayList.add(adjVal);//add new state to adjacent states
                                adjVal = cloneState(currState);

                            }
                        } else {//dest cascade is empty (any move valid)
                            moveCascToCasc(adjVal, y, x);//move card
                            h.setHVal(adjVal);
                            adjVal.createHashKey();
                            adjValArrayList.add(adjVal);//add new state to adjacent states
                            adjVal = cloneState(currState);

                        }//end else
                    }//end if x != y
                }//end for x


                /*
                 * next, for currentState, add adjacent states where move is valid to foundation
                 */
                if (hasFoundationMove(adjVal.foundation, lastInCurrCasc)) {

                    cascToFoundMove(adjVal, y);//make move, create new adjVal
                    h.setHVal(adjVal);
                    adjVal.createHashKey();
                    adjValArrayList.add(adjVal);//add new adjVal to list
                    adjVal = cloneState(currState);
                }


                /*
                 * now, for currentState, add adjacent state where move is valid to the free cells
                 */
                if (currState.freeCells.size() < 4) {//if freeCells have one open
                    //move last card in current cascade to free cell
                    Card srcCard = adjVal.casc.get(y).get(adjVal.casc.get(y).size() - 1);
                    adjVal.move = srcCard.sVal+srcCard.suit + " to free cell";
                    adjVal.freeCells.add(adjVal.casc.get(y).remove(adjVal.casc.get(y).size() - 1));

                    //add adjval to arrayList
                    adjVal.createHashKey();
                    h.setHVal(adjVal);
                    adjValArrayList.add(adjVal);
                    adjVal = cloneState(currState);
                }//end if freeCells size < 4
            }//end if y is non-empty
        }//end for y


        /*
         * finally, check for possible moves FROM free cells 
         */

        for (int x = 0; x < adjVal.freeCells.size(); x++) {//for each card in free cells

            //check for valid moves from free cells to cascades
            for (int y = 0; y < adjVal.casc.size(); y++) {
                ArrayList<Card> currCasc = adjVal.casc.get(y);
                if (currCasc.size() > 0) {
                    if (validCascMove(adjVal.freeCells.get(x), currCasc.get(currCasc.size() - 1))) {//if moving card from freecell to cascade is valid

                        Card srcCard = adjVal.freeCells.get(x);
                        Card destCard = currCasc.get(currCasc.size() - 1);
                        adjVal.move = srcCard.sVal+srcCard.suit + " to " + destCard.sVal+destCard.suit;
                        
                        currCasc.add(adjVal.freeCells.get(x));//add freecell card to cascade
                        adjVal.freeCells.remove(adjVal.freeCells.get(x));//remove freecell card
                        adjVal.createHashKey();
                        h.setHVal(adjVal);
                        adjValArrayList.add(adjVal);//add adjVal to arrayList
                        adjVal = cloneState(currState);
                    }//end if
                }
            }//end for cascade


            //check for valid move from freecells to foundations
            if (hasFoundationMove(adjVal.foundation, adjVal.freeCells.get(x))) {//if there is a valid move to foundation

                freeCellToFoundMove(adjVal, x);//make foundation move
                adjVal.createHashKey();
                h.setHVal(adjVal);
                adjValArrayList.add(adjVal);//add new adjVal to list
                adjVal = cloneState(currState);
            }
        }//end for card

        return adjValArrayList;//return arrayList of adjacent values
    }

    /*
     * The following methods are search algorithms
     */
    public StateNode BreadthFirstSearch(StateNode initialState) {
        frontier = new LinkedList<StateNode>();
        explored = new LinkedList<StateNode>();
        StateNode baseState;
        ArrayList<StateNode> adjacentStates;
        addToFrontier(initialState);
        while (!frontier.isEmpty()) {//for each state in frontier (while frontier is not empty)
            baseState = frontier.element();//use first node in frontier as starting point
            adjacentStates = checkAdjacents(baseState);//search adjacent states

            for (int i = 0; i < adjacentStates.size(); i++) {
                if (adjacentStates.get(i) != null) {
                    addToFrontier(adjacentStates.get(i));//add all adjacent states to frontier
                }
            }



            removeFromFrontier(baseState);//remove baseState from frontier

            System.out.printf("Frontier Size: %d, Explored Size: %d\n", frontier.size(), explored.size());

            //check goal state
            for (StateNode node : explored) {
                if (node.hVal == 0) {
                    return node;
                }
            }
        }
        return null;
    }

    public StateNode DepthFirstSearch(StateNode initialState) {
        StateNode baseState;
        ArrayList<StateNode> adjacentStates;
        addToFrontier(initialState);

        adjacentStates = checkAdjacents(initialState);//get adjacent states
        for (int i = 0; i < adjacentStates.size(); i++) {//for all adjacent states
            addToFrontier(adjacentStates.get(i));//add first state to frontier
            DepthFirstSearch(adjacentStates.get(i));//recurse on first state
            removeFromFrontier(adjacentStates.get(i));//remove first state from frontier (add to explored)

            for (StateNode node : explored) {
                if (node.hVal == 0) {
                    return node;
                }
            }
        }
        return null;
    }

    public StateNode ASearch(StateNode initialState) {
        //TODO ASearch heuristic
        frontier = new PriorityQueue(1, new AstarComparator());
        explored = new PriorityQueue(1, new AstarComparator());


        StateNode baseState;
        ArrayList<StateNode> adjacentStates;
        addToFrontier(initialState);
        while (!frontier.isEmpty()) {//for each state in frontier (while frontier is not empty)

            baseState = frontier.element();//use first node in frontier as starting point
            adjacentStates = checkAdjacents(baseState);//search adjacent states

            for (int i = 0; i < adjacentStates.size(); i++) {
                if (adjacentStates.get(i) != null) {
                    addToFrontier(adjacentStates.get(i));//add all adjacent states to frontier
                }
            }

            removeFromFrontier(baseState);//remove baseState from frontier

            //TODO check goal state
            for (StateNode node : explored) {
                if (node.hVal == 0) {
                    h.setHVal(node);
                    return node;
                }
            }
        }
        return null;
    }

    public StateNode UniformCostSearch(StateNode initialState) {
        frontier = new PriorityQueue(1, new UniformCostComparator());
        explored = new PriorityQueue(1, new UniformCostComparator());

        StateNode baseState;
        ArrayList<StateNode> adjacentStates;
        addToFrontier(initialState);
        while (!frontier.isEmpty()) {//for each state in frontier (while frontier is not empty)
            baseState = frontier.element();//use first node in frontier as starting point
            adjacentStates = checkAdjacents(baseState);//search adjacent states

            for (int i = 0; i < adjacentStates.size(); i++) {
                if (adjacentStates.get(i) != null) {
                    addToFrontier(adjacentStates.get(i));//add all adjacent states to frontier
                }
            }

            removeFromFrontier(baseState);//remove baseState from frontier

            //check goal state
            for (StateNode node : explored) {
                if (node.hVal == 0) {
                    return node;
                }
            }
        }
        return null;
    }

    public StateNode GreedyBestFirstSearch(StateNode initialState) {
        //TODO greedyComaparator
        frontier = new PriorityQueue(1, new GreedyComparator());
        explored = new PriorityQueue(1, new GreedyComparator());

        StateNode baseState;
        ArrayList<StateNode> adjacentStates;
        addToFrontier(initialState);
        while (!frontier.isEmpty()) {//for each state in frontier (while frontier is not empty)
            baseState = frontier.element();//use first node in frontier as starting point
            adjacentStates = checkAdjacents(baseState);//search adjacent states

            for (int i = 0; i < adjacentStates.size(); i++) {
                if (adjacentStates.get(i) != null) {
                    addToFrontier(adjacentStates.get(i));//add all adjacent states to frontier
                }
            }

            removeFromFrontier(baseState);//remove baseState from frontier

            //TODO check goal state
            for (StateNode node : explored) {
                if (node.hVal == 0) {
                    return node;
                }
            }
        }
        return null;
    }

    /*
     * The following methods are support methods for this class
     */
    public boolean validCascMove(Card startCard, Card destCard) {


        //test whether the card to be moved is opposite color of the dest card
        if (startCard.iVal == destCard.iVal - 1) {
            if (startCard.suit.equals("H") && (destCard.suit.equals("C") || destCard.suit.equals("S"))) {
                return true;
            }
            if (startCard.suit.equals("D") && (destCard.suit.equals("C") || destCard.suit.equals("S"))) {
                return true;
            }
            if (startCard.suit.equals("C") && (destCard.suit.equals("D") || destCard.suit.equals("H"))) {
                return true;
            }
            if (startCard.suit.equals("S") && (destCard.suit.equals("D") || destCard.suit.equals("H"))) {
                return true;
            }
        }

        return false;
    }

    public boolean hasFoundationMove(ArrayList<ArrayList<Card>> currFound, Card startCard) {



        if (startCard.iVal == 1) {
            return true;//if card is an ace, valid move is available
        }
        for (ArrayList<Card> thisFound : currFound) {//for each non-empty foundation
            Card topCard = thisFound.get(thisFound.size() - 1);//get top card of foundation

            //if startCard is one value greater than and is of same suit as topCard, valid move is possible
            if ((startCard.iVal == topCard.iVal + 1) && startCard.suit.equalsIgnoreCase(topCard.suit)) {
                return true;
            }
        }
        return false;
    }

    ;
    public void cascToFoundMove(StateNode currState, int s) {
        ArrayList<Card> currCasc = currState.casc.get(s);

        //if card in question is an ace, immediately add to new foundation
        if (currCasc.get(currCasc.size() - 1).iVal == 1) {
            currState.move = currCasc.get(currCasc.size() - 1).sVal+currCasc.get(currCasc.size() - 1).suit + " to foundation";
            ArrayList<Card> newFound = new ArrayList<Card>();
            newFound.add(currCasc.remove(currCasc.size() - 1));
            currState.foundation.add(newFound);
            return;
        }
        //find correct foundation in for loop
        if (!currState.foundation.isEmpty()) {
            for (ArrayList<Card> currFounds : currState.foundation) {//foundations exist, check bottom card for suit
                if (currCasc.isEmpty()) {
                    return;
                }
                if (currFounds.get(0).suit.equalsIgnoreCase(currCasc.get(currCasc.size() - 1).suit))//if suits match
                {
                    Card srcCard = currCasc.get(currCasc.size() - 1);
                    Card destCard = currFounds.get(currFounds.size() - 1);
                    currState.move = srcCard.sVal+srcCard.suit + " to " + destCard.sVal+destCard.suit;
                    currFounds.add(currCasc.remove(currCasc.size() - 1));//move card to foundation
                    
                    return;
                }
            }
        }
    }

    public void moveCascToCasc(StateNode currState, int s, int d) {
        //create new state with same values as current state
        ArrayList<Card> srcCasc = currState.casc.get(s);
        ArrayList<Card> destCasc = currState.casc.get(d);
        Card srcCard = srcCasc.get(srcCasc.size() - 1);
        Card destCard;
        if (!destCasc.isEmpty()) {
            destCard = destCasc.get(destCasc.size() - 1);
        } else {
            destCard = new Card("cascade", 0, "empty ");
        }

        //move card on new state
        destCasc.add(srcCasc.remove(srcCasc.size() - 1));
        currState.move = srcCard.sVal + srcCard.suit + " to " + destCard.sVal + destCard.suit;
    }

    public void freeCellToFoundMove(StateNode currentState, int s) {

        if (currentState.freeCells.get(s).iVal == 1) {
            ArrayList<Card> temp = new ArrayList<Card>();
            temp.add(currentState.freeCells.remove(s));
            currentState.foundation.add(temp);
        } else {

            for (int x = 0; x < currentState.foundation.size(); x++) {
                //if current freecell card has same suit as current foundation card, move card
                if (s == currentState.freeCells.size()) {
                    return;
                }
                if (currentState.foundation.get(x).get(0).suit.equalsIgnoreCase(currentState.freeCells.get(s).suit)) {
                    currentState.move = currentState.freeCells.get(s).sVal + currentState.freeCells.get(s).suit + " to "
                            + currentState.foundation.get(x).get(currentState.foundation.get(x).size() - 1).sVal
                            + currentState.foundation.get(x).get(currentState.foundation.get(x).size() - 1).suit;
                    currentState.foundation.get(x).add(currentState.freeCells.remove(s));
                    return;
                }
            }
        }
    }

    public StateNode cloneState(StateNode node) {
        ArrayList<ArrayList<Card>> cascClone = new ArrayList<ArrayList<Card>>();
        ArrayList<ArrayList<Card>> foundClone = new ArrayList<ArrayList<Card>>();
        ArrayList<Card> freeCellClone = new ArrayList<Card>();

        for (int x = 0; x < node.casc.size(); x++) {
            cascClone.add(new ArrayList<Card>());
            for (int y = 0; y < node.casc.get(x).size(); y++) {
                cascClone.get(x).add((Card) node.casc.get(x).get(y).clone());
            }//end for y
        }//end for x

        for (int x = 0; x < node.foundation.size(); x++) {
            foundClone.add(new ArrayList<Card>());
            for (int y = 0; y < node.foundation.get(x).size(); y++) {
                foundClone.get(x).add((Card) node.foundation.get(x).get(y).clone());
            }//end for y
        }//end for x

        for (int y = 0; y < node.freeCells.size(); y++) {
            freeCellClone.add((Card) node.freeCells.get(y).clone());
        }

        StateNode clone = new StateNode(foundClone, freeCellClone, cascClone, node, node.cost + 1);
        h.setHVal(clone);
        clone.createHashKey();
        return clone;
    }//end method
}
