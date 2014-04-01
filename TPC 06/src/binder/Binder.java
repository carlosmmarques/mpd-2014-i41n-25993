package binder;

import static utils.SneakyUtils.throwAsRTException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class Binder {
	
    private final BindMember [] bms;

    /**
     * 
     * Constructor - Receives a set of parameters of type BindMember
     *  This approach is based upon the Strategy Pattern
     *  This Binder will try to Bind values to TargetObjects using 
     *   the BindMembers passed to the constructor.
     * @param bms
     */
    public Binder(BindMember...bms) {
        this.bms = bms;
    }
    
    /**
	 * getFieldsValues 
	 * 
	 * @param o - The instance of the object upon which to perform the operation;
	 * @return Map<String, Object> with the names and values of the fields of
	 * an instance of an object.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> getFieldsValues(Object o) throws IllegalArgumentException, IllegalAccessException{
		if (o == null) throw new IllegalArgumentException("O objecto não pode ser nulo");
		HashMap<String, Object> map = new HashMap<String, Object>();
		Class<?> klass = o.getClass();
		for (Field f : klass.getDeclaredFields()){
			if (!f.isAccessible()) f.setAccessible(true);
			map.put(f.getName(), f.get(o));
		}
		return map;
	}
	
	/**
	 * 
	 * checkForParameterlessConstructor
	 * 
	 * @param targetClass - The class to check. If it does not 
	 * @throws RuntimeException
	 */
	public static <T> void checkForParameterlessConstructor(Class<T> targetClass) throws RuntimeException {
		for (Constructor<?> ctor : targetClass.getDeclaredConstructors()){
			if (ctor.getGenericParameterTypes().length == 0){
				if (!ctor.isAccessible()) ctor.setAccessible(true);
			} else {
				throw new RuntimeException("Class does not have a parameterless constructor.");
			}
		}
	}

	/**
	 * 
	 * bindto - returns an instance of <T>, given a target Class<T> and 
	 *  map of Fields and / or Properties.
	 * 
	 * @param targetClass
	 * @param vals
	 * @return
	 */
	public <T> T bindTo(Class<T> targetClass, Map<String, Object> vals)
    {
        try {
            if (targetClass == null || vals == null) {
                throw new IllegalArgumentException();
            }
            checkForParameterlessConstructor(targetClass);
            T target = targetClass.newInstance();
            for (Map.Entry<String, Object> e : vals.entrySet()) {
                for (BindMember bm : bms) {
                    if(bm.bind(target, e.getKey(), e.getValue()))
                        break;
                }
            
            }
            return target;
        } catch (InstantiationException | IllegalAccessException ex) {
            throwAsRTException(ex);
        }
        throw new IllegalStateException();
    }

}
