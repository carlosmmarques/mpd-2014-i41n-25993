package binder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BinderProps extends AbstractBinder {

    @Override
    <T> boolean bindMember(T target, String key, Object v) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                Method[] ms = target.getClass().getMethods();
        for (Method m : ms) {
            String mName = m.getName();
            if (!mName.equalsIgnoreCase("set" + key)) {
                continue;
            }
            Class<?>[] paramsKlasses = m.getParameterTypes();
            if (paramsKlasses.length != 1) {
                continue;
            }
            Class<?> propType = WrapperUtils.toWrapper(paramsKlasses[0]);
            if (propType.isAssignableFrom(v.getClass())) {
                m.setAccessible(true);
                m.invoke(target, v);
                return true;
            }
        }
        return false;
    }

}
