package strategyPattern;

import strategyPattern.strategies.FastStrategy;
import strategyPattern.strategies.IgnoreStrategy;
import strategyPattern.strategies.MediumStrategy;
import strategyPattern.strategies.SlowStrategy;

public class StrategyBuilder {
    
    public static Strategy build(int i){
        if(i > 7){
            //use a resource intensive algorithm for faster speed
            return new FastStrategy();
        }
        
        if (i > 4){
            //use an algorithm that balances speed and power
            return new MediumStrategy();
        }
        
        if(i > 0){
            //use an algorithm that reduces power consumption but takes more time
            return new SlowStrategy();
        }
        
        return new IgnoreStrategy();
    }
}
