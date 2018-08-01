package strategyPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        while (true) {
            List<ProcessableThing> processableThings = buildRandomLengthList();
            
            System.out.println(String.format("We need to process %d things", processableThings.size()));
            
            Strategy strategy = StrategyBuilder.build(processableThings.size());

            try {
                strategy.processThings(processableThings);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\n\nREADY FOR NEXT LIST\n\n");
        }
    }

    private static List<ProcessableThing> buildRandomLengthList() {
        int listLength = new Random().nextInt(10);
        List<ProcessableThing> list = new ArrayList<>();
        for (int x = 0; x < listLength; x++) {
            list.add(new ProcessableThing());
        }
        return list;
    }
}
