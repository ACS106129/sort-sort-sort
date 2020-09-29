package filter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Filter of generic data type
 */
public abstract class Filter<DataType> {

    public final Class<DataType> genericType;

    @SuppressWarnings("unchecked")
    public Filter() {
        Type type = getClass().getGenericSuperclass();
        while (!(type instanceof ParameterizedType) || ((ParameterizedType) type).getRawType() != Filter.class) {
            if (type instanceof ParameterizedType) {
                type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
            } else {
                type = ((Class<?>) type).getGenericSuperclass();
            }
        }
        genericType = (Class<DataType>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    /**
     * @param target target of data type
     * @return true if accept success
     */
    public abstract boolean accept(DataType target);
}