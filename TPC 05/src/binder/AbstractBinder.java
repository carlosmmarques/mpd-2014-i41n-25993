package binder;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class AbstractBinder {
	
    /**
     * 
     * bindTo - Returns an instance of type <T>, given a target Class<T> and a Map of Fields and / or
     *  Properties values
     * 
     * @param targetClass - The target Class 
     * @param vals - A Map of Fields and / or Properties values.
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
	public <T> T bindTo(Class<T> targetClass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (targetClass == null || vals == null) {
            throw new IllegalArgumentException();
        }
        T target = targetClass.newInstance();
        for (Map.Entry<String, Object> e : vals.entrySet()) {
            bindMember(target, e.getKey(), e.getValue());
        }
        return target;
    }

    /**
     * 
     * bindMember - A Hook Method to be Implemented in specific classes which extend 
     *  this Class
     * 
     * @param target - The Target instance
     * @param key - The name of the Field and / or Property to be binded against the Target Instance
     * @param value - The value <Object> of the Field and / or Property to be binded against the Target Instance
     * @return - True if binding of value to corresponding Field and / or Property was successful. 
     *  False otherwise.
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
	abstract <T> boolean bindMember(T target, String key, Object value) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;


}
