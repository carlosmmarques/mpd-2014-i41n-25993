package binder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


public class BinderFields extends AbstractBinder {

    /**
     * Implementation of Hook Method from AbstractBinder.
     */
	@Override
    <T> boolean bindMember(T target, String key, Object fValue) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                Field[] fields = target.getClass().getDeclaredFields();
        for (Field f : fields) {
            String fName = f.getName();
            if (fName.equals(key)) {
                Class<?> fType = f.getType();
                f.setAccessible(true);
                if (fType.isPrimitive()) {
                    fType = f.get(target).getClass();
                }
                /*
                 * Verifica se o tipo do campo (fType) é tipo base do tipo de fValue.
                 * Nota: Tipo base inclui superclasses ou superinterfaces.
                 */
                if (fType.isAssignableFrom(fValue.getClass())) {
                    f.set(target, fValue);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
