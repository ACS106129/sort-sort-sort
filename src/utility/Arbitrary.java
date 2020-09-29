package utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Arbitrary {

    public static String[] generateNumber(int amount, Number floor, Number ceil) {
        String[] numbers = new String[amount];
        BigDecimal bdFloor = new BigDecimal(floor.toString());
        BigDecimal bdCeil = new BigDecimal(ceil.toString());
        for (int i = 0; i < amount; ++i) {
            numbers[i] = String.valueOf(new DecimalFormat("#.###")
                    .format(new BigDecimal(Math.random()).multiply((bdCeil.subtract(bdFloor))).add(bdFloor)));
        }
        return numbers;
    }
}