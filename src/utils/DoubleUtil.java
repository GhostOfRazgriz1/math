package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class DoubleUtil {
    private DoubleUtil() {
    }

    public static double round(double value, int places) {
        assert places > 0: "Invalid input of places!";

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
