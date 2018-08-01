package kaprekar;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by joncameron on 10/14/16.
 */
public class KaprekarRoutine {

    public static void main(String[] args) {
        assertThat(reverseInteger(1000), is(1));
        assertThat(reverseInteger(1234), is(4321));
        assertThat(reverseInteger(9834), is(4389));

        assertThat(largestInteger(1234), is(4));
    }


    private static Integer reverseInteger(Integer i) {
        String reversedString = new StringBuilder(String.valueOf(i)).reverse().toString();
        return Integer.valueOf(reversedString);
    }

    private static Integer largestInteger(Integer i) {
        Integer largestInt = 0;
        for (int j = 1; j <= 1000; j*=10) {
            Integer currentInt = i%(i/j);
            if(currentInt > largestInt){
                largestInt = currentInt;
            }
        }
        return largestInt;
    }
}
