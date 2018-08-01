package strategyPattern.strategies;

import strategyPattern.ProcessableThing;
import strategyPattern.Strategy;

import java.util.List;

public class FastStrategy implements Strategy {

    @Override
    public void processThings(List<ProcessableThing> list) throws InterruptedException {
        for (ProcessableThing processableThing : list) {
            System.out.println(String.format("Processing a thing at %d%% CPU for %d second", processPower(), processSpeed()/1000));
            Thread.sleep(processSpeed());
            processableThing.process();
        }
    }

    @Override
    public long processSpeed() {
        return 1000;
    }

    @Override
    public int processPower() {
        return 80;
    }
}
