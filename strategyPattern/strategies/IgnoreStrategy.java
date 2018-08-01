package strategyPattern.strategies;

import strategyPattern.ProcessableThing;
import strategyPattern.Strategy;

import java.util.List;

public class IgnoreStrategy implements Strategy {
    @Override
    public void processThings(List<ProcessableThing> list) throws InterruptedException {
        System.out.println("There is nothing to process! Done!");
    }

    @Override
    public long processSpeed() {
        return 0;
    }

    @Override
    public int processPower() {
        return 0;
    }
}
