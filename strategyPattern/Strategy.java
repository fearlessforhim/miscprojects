package strategyPattern;

import java.util.List;

public interface Strategy {
    void processThings(List<ProcessableThing> list) throws InterruptedException;
    long processSpeed();
    int processPower();
}
