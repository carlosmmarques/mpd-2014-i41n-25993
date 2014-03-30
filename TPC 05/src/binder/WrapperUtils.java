package binder;

import java.util.HashMap;
import java.util.Map;

public class WrapperUtils {

    final static Map<Class<?>, Class<?>> wrappers = new HashMap<>();

    static {
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(short.class, Short.class);
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(int.class, Integer.class);

    }

    public static Class<?> toWrapper(Class<?> c) {
        return c.isPrimitive() ? wrappers.get(c) : c;
    }
}
