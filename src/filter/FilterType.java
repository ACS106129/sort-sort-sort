package filter;

import java.math.BigDecimal;
import java.math.BigInteger;

public enum FilterType {
    Number(Number.class, 0), Integer(Integer.class, 1), Long(Long.class, 2), BigInteger(BigInteger.class, 3),
    Float(Float.class, 4), Double(Double.class, 5), BigDecimal(BigDecimal.class, 6), Character(Character.class, 7),
    String(String.class, 8);

    private final Class<?> value;

    private final int order;

    private FilterType(Class<?> value, int order) {
        this.value = value;
        this.order = order;
    }

    public String toString() {
        return value.getSimpleName();
    }

    public Class<?> getValue() {
        return value;
    }

    public int getOrder() {
        return order;
    }

};