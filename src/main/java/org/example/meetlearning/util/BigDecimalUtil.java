package org.example.meetlearning.util;


import java.math.BigDecimal;

public class BigDecimalUtil {


    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return nullOrZero(v1).add(nullOrZero(v2));
    }

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return nullOrZero(v1).subtract(nullOrZero(v2));
    }

    public static BigDecimal nullOrZero(BigDecimal v1) {
        return v1 == null ? BigDecimal.ZERO : v1;
    }

    public static Boolean gtZero(BigDecimal v1) {
        return nullOrZero(v1).compareTo(BigDecimal.ZERO) > 0;
    }

    public static Boolean lteThan(BigDecimal v1, BigDecimal v2) {
        return nullOrZero(v1).compareTo(nullOrZero(v2)) <= 0;
    }

    public static Boolean gteThan(BigDecimal v1, BigDecimal v2) {
        return nullOrZero(v1).compareTo(nullOrZero(v2)) >= 0;
    }

    public static Boolean eqZero(BigDecimal v1) {
        return nullOrZero(v1).compareTo(BigDecimal.ZERO) == 0;
    }
}
